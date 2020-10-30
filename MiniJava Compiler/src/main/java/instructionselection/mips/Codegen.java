package instructionselection.mips;

import utils.assem.Instr;
import utils.assem.OPER;
import utils.irtree.*;
import utils.irtree.abstractions.Exp;
import utils.temp.Label;
import utils.temp.LabelList;
import utils.temp.Temp;
import utils.temp.TempList;
import utils.visitor.CodeVisitor;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

public class Codegen implements CodeVisitor
{
	private MipsFrame frame;
	private ListIterator<Instr> insns;
	public Codegen(MipsFrame f, ListIterator<Instr> i)
	{
		frame = f;
		insns = i;
	}

        public void visit(SEQ n) { throw new Error(); }
	public Temp visit(ESEQ n) { throw new Error(); }

	private void emit(Instr inst)
	{
		insns.add(inst);
	}

	static Instr OPER(String a, Temp[] d, Temp[] s, List<Label> j)
	{
		TempList listD = TempList.createTempList(d);
		TempList listS = TempList.createTempList(s);
		LabelList listJ = LabelList.createLabelList(j);
		return new OPER("\t" + a, listD, listS, listJ);
	}

	static Instr OPER(String a, Temp[] d, Temp[] s)
	{
		return OPER(a, d, s, null);
	}
	static Instr MOVE(String a, Temp d, Temp s)
	{
		return new utils.assem.MOVE("\t" + a, d, s);
	}

	private static CONST CONST16(Exp e)
	{
		if (e instanceof CONST) {
			CONST c = (CONST)e;
			int value = c.value;
			if (value == (short)value)
				return c;
		}
		return null;
	}

	private static boolean constant(BINOP e) {
		CONST left = CONST16(e.left);
		CONST right = CONST16(e.right);
		if (left == null)
			return right != null;
		switch (e.binop) {
			case BINOP.PLUS:
			case BINOP.MUL:
			case BINOP.AND:
			case BINOP.OR:
			case BINOP.XOR:
				if (right == null) {
					e.left = e.right;
					e.right = left;
				}
				return true;
		}
		return false;
	}

	public void visit(MOVE s) {
		// MOVE(MEM, Exp)
		if (s.dst instanceof MEM) {
			MEM mem = (MEM)s.dst;

			// MOVE(MEM(+ Exp CONST), Exp)
			if (mem.exp instanceof BINOP) {
				BINOP b = (BINOP)mem.exp;
				if (b.binop == BINOP.PLUS && constant(b)) {
					int right = ((CONST)b.right).value;
					Temp left = (b.left instanceof TEMP) ?
							((TEMP)b.left).temp : b.left.accept(this);
					String off = Integer.toString(right);
					if (left == frame.FP) {
						left = frame.SP;
						off += "+" + frame.name + "_framesize";
					}
					emit(OPER("sw `s0, " + off + "(`s1)", null,
							new Temp[]{s.src.accept(this), left}));
					return;
				}
			}

			// MOVE(MEM(CONST), Exp)
			CONST exp = CONST16(mem.exp);
			if (exp != null) {
				emit(OPER("sw `s0, " + exp.value + "(`s1)", null,
						new Temp[]{s.src.accept(this), frame.ZERO}));
				return;
			}

			// MOVE(MEM(TEMP), Exp)
			if (mem.exp instanceof TEMP) {
				Temp temp = ((TEMP)mem.exp).temp;
				if (temp == frame.FP) {
					emit(OPER("sw `s0, " + frame.name + "_framesize" + "(`s1)",
							null,
							new Temp[]{s.src.accept(this), frame.SP}));
					return;
				}
			}

			// MOVE(MEM(Exp), Exp)
			emit(OPER("sw `s0, (`s1)", null,
					new Temp[]{s.src.accept(this), mem.exp.accept(this)}));
			return;
		}

		Temp dst = ((TEMP)s.dst).temp;

		// MOVE(TEMP, MEM)
		if (s.src instanceof MEM) {
			MEM mem = (MEM)s.src;

			// MOVE(TEMP, MEM(+ Exp CONST))
			if (mem.exp instanceof BINOP) {
				BINOP b = (BINOP)mem.exp;
				if (b.binop == BINOP.PLUS && constant(b)) {
					int right = ((CONST)b.right).value;
					Temp left = (b.left instanceof TEMP) ?
							((TEMP)b.left).temp : b.left.accept(this);
					String off = Integer.toString(right);
					if (left == frame.FP) {
						left = frame.SP;
						off += "+" + frame.name + "_framesize";
					}
					emit(OPER("lw `d0, " + off + "(`s0)",
							new Temp[]{dst}, new Temp[]{left}));
					return;
				}
			}

			// MOVE(TEMP, MEM(CONST))
			CONST exp = CONST16(mem.exp);
			if (exp != null) {
				emit(OPER("lw `d0, " + exp.value + "(`s0)",
						new Temp[]{dst}, new Temp[]{frame.ZERO}));
				return;
			}

			// MOVE(TEMP, MEM(TEMP))
			if (mem.exp instanceof TEMP) {
				Temp temp = ((TEMP)mem.exp).temp;
				if (temp == frame.FP) {
					emit(OPER("lw `d0, " + frame.name + "_framesize" + "(`s0)",
							new Temp[]{dst}, new Temp[]{frame.SP}));
					return;
				}
			}

			// MOVE(TEMP, MEM(Exp))
			emit(OPER("lw `d0, (`s0)",
					new Temp[]{dst}, new Temp[]{mem.exp.accept(this)}));
			return;
		}

		// MOVE(TEMP, Exp)
		emit(MOVE("move `d0, `s0", dst, s.src.accept(this)));
	}

	public void visit(EXP s) { s.exp.accept(this); }

	public void visit(JUMP s) {
		List<Label> list = s.targets.createListOfLabel();
		if (s.exp instanceof NAME) {
			NAME name = (NAME)s.exp;
			// JUMP(Tree.NAME, List<Label>)
			emit(OPER("b " + name.label.toString(), null, null, list));
			return;
		}
		// JUMP(Exp, List<Label>)
		emit(OPER("jr `s0", null, new Temp[]{s.exp.accept(this)}, list));
		return;
	}

	private static boolean constant(CJUMP s) {
		CONST left = CONST16(s.left);
		CONST right = CONST16(s.right);
		if (left == null)
			return right != null;
		if (right == null) {
			s.left = s.right;
			s.right = left;
			switch (s.relop) {
				case CJUMP.EQ:
				case CJUMP.NE:
					break;
				case CJUMP.LT:
					s.relop = CJUMP.GT;
					break;
				case CJUMP.GE:
					s.relop = CJUMP.LE;
					break;
				case CJUMP.GT:
					s.relop = CJUMP.LT;
					break;
				case CJUMP.LE:
					s.relop = CJUMP.GE;
					break;
				case CJUMP.ULT:
					s.relop = CJUMP.UGT;
					break;
				case CJUMP.UGE:
					s.relop = CJUMP.ULE;
					break;
				case CJUMP.UGT:
					s.relop = CJUMP.ULT;
					break;
				case CJUMP.ULE:
					s.relop = CJUMP.UGE;
					break;
				default:
					throw new Error("bad relational operation");
			}
		}
		return true;
	}

	private static String[] CJUMP_ARRAY = new String[10];
	static {
		CJUMP_ARRAY[CJUMP.EQ ] = "beq";
		CJUMP_ARRAY[CJUMP.NE ] = "bne";
		CJUMP_ARRAY[CJUMP.LT ] = "blt";
		CJUMP_ARRAY[CJUMP.GT ] = "bgt";
		CJUMP_ARRAY[CJUMP.LE ] = "ble";
		CJUMP_ARRAY[CJUMP.GE ] = "bge";
		CJUMP_ARRAY[CJUMP.ULT] = "bltu";
		CJUMP_ARRAY[CJUMP.ULE] = "bleu";
		CJUMP_ARRAY[CJUMP.UGT] = "bgtu";
		CJUMP_ARRAY[CJUMP.UGE] = "bgeu";
	}

	public void visit(CJUMP s) {
		List<Label> targets = new LinkedList<Label>();
		targets.add(s.iftrue);
		targets.add(s.iffalse);
		if (constant(s)) {
			int right = ((CONST)s.right).value;
			// CJUMP(op, Exp, CONST, Label, Label)
			emit(OPER(CJUMP_ARRAY[s.relop] + " `s0, " + right + ", "
							+ s.iftrue.toString(),
					null, new Temp[]{s.left.accept(this)}, targets));
			return;
		}

		// CJUMP(op, Exp, Exp, Label, Label)
		emit(OPER(CJUMP_ARRAY[s.relop] + " `s0, `s1 " + s.iftrue.toString(),
				null, new Temp[]{s.left.accept(this), s.right.accept(this)},
				targets));
		return;
	}

	public void visit(LABEL l) {
		emit(new utils.assem.LABEL(l.label.toString() + ":", l.label));
		return;
	}

	public Temp visit(CONST e) {
		if (e.value == 0)
			return frame.ZERO;
		Temp t = new Temp();
		emit(OPER("li `d0, " + e.value, new Temp[]{t}, null));
		return t;
	}

	public Temp visit(NAME e) {
		Temp t = new Temp();
		emit(OPER("la `d0, " + e.label.toString(), new Temp[]{t}, null));
		return t;
	}

	public Temp visit(TEMP e) {
		if (e.temp == frame.FP) {
			Temp t = new Temp();
			emit(OPER("addu `d0, `s0, " + frame.name + "_framesize",
					new Temp[]{t}, new Temp[]{frame.SP}));
			return t;
		}
		return e.temp;
	}

	private static String[] BINOP_ARRAY = new String[10];
	static {
		BINOP_ARRAY[BINOP.PLUS   ] = "add";
		BINOP_ARRAY[BINOP.MINUS  ] = "sub";
		BINOP_ARRAY[BINOP.MUL    ] = "mulo";
		BINOP_ARRAY[BINOP.DIV    ] = "div";
		BINOP_ARRAY[BINOP.AND    ] = "and";
		BINOP_ARRAY[BINOP.OR     ] = "or";
		BINOP_ARRAY[BINOP.LSHIFT ] = "sll";
		BINOP_ARRAY[BINOP.RSHIFT ] = "srl";
		BINOP_ARRAY[BINOP.ARSHIFT] = "sra";
		BINOP_ARRAY[BINOP.XOR    ] = "xor";
	}

	private static int shift(int i) {
		int shift = 0;
		if ((i >= 2) && ((i & (i - 1)) == 0)) {
			while (i > 1) {
				shift += 1;
				i >>= 1;
			}
		}
		return shift;
	}

	public Temp visit(BINOP e) {
		Temp t = new Temp();
		if (constant(e)) {
			int right = ((CONST)e.right).value;
			switch (e.binop) {
				case BINOP.PLUS:
				{
					Temp left = (e.left instanceof TEMP) ?
							((TEMP)e.left).temp : e.left.accept(this);
					String off = Integer.toString(right);
					if (left == frame.FP) {
						left = frame.SP;
						off += "+" + frame.name + "_framesize";
					}
					emit(OPER("add `d0, `s0, " + off, new Temp[]{t},
							new Temp[]{left}));
					return t;
				}
				case BINOP.MUL:
				{
					int shift = shift(right);
					if (shift != 0) {
						emit(OPER("sll `d0, `s0, " + shift, new Temp[]{t},
								new Temp[]{e.left.accept(this)}));
						return t;
					}
					emit(OPER(BINOP_ARRAY[e.binop] + " `d0, `s0, " + right,
							new Temp[]{t}, new Temp[]{e.left.accept(this)}));
					return t;
				}
				case BINOP.DIV:
				{
					int shift = shift(right);
					if (shift != 0) {
						emit(OPER("sra `d0, `s0, " + shift, new Temp[]{t},
								new Temp[]{e.left.accept(this)}));
						return t;
					}
					emit(OPER(BINOP_ARRAY[e.binop] + " `d0, `s0, " + right,
							new Temp[]{t}, new Temp[]{e.left.accept(this)}));
					return t;
				}
				default:
					emit(OPER(BINOP_ARRAY[e.binop] + " `d0, `s0, " + right,
							new Temp[]{t}, new Temp[]{e.left.accept(this)}));
					return t;
			}
		}
		emit(OPER(BINOP_ARRAY[e.binop] + " `d0, `s0, `s1", new Temp[]{t},
				new Temp[]{e.left.accept(this), e.right.accept(this)}));
		return t;
	}

	public Temp visit(MEM e) {
		Temp t = new Temp();

		// MEM(+ Exp CONST)
		if (e.exp instanceof BINOP) {
			BINOP b = (BINOP)e.exp;
			if (b.binop == BINOP.PLUS && constant(b)) {
				int right = ((CONST)b.right).value;
				Temp left = (b.left instanceof TEMP) ?
						((TEMP)b.left).temp : b.left.accept(this);
				String off = Integer.toString(right);
				if (left == frame.FP) {
					left = frame.SP;
					off += "+" + frame.name + "_framesize";
				}
				emit(OPER("lw `d0, " + off + "(`s0)", new Temp[]{t},
						new Temp[]{left}));
				return t;
			}
		}

		// MEM(CONST)
		CONST exp = CONST16(e.exp);
		if (exp != null) {
			emit(OPER("lw `d0, " + exp.value + "(`s0)", new Temp[]{t},
					new Temp[]{frame.ZERO}));
			return t;
		}

		// MEM(TEMP)
		if (e.exp instanceof TEMP) {
			Temp temp = ((TEMP)e.exp).temp;
			if (temp == frame.FP) {
				emit(OPER("lw `d0, " + frame.name + "_framesize" + "(`s0)",
						new Temp[]{t}, new Temp[]{frame.SP}));
				return t;
			}
		}

		// MEM(Exp)
		emit(OPER("lw `d0, (`s0)", new Temp[]{t},
				new Temp[]{e.exp.accept(this)}));
		return t;
	}

	public Temp visit(CALL s) {
		Iterator<Exp> args = s.args.createListOfExp().iterator();

		LinkedList<Temp> uses = new LinkedList<Temp>();

		Temp V0 = args.next().accept(this);
		if (V0 != frame.ZERO) {
			emit(MOVE("move `d0, `s0", frame.V0, V0));
			uses.add(frame.V0);
		}

		int offset = 0;

		if (args.hasNext()) {
			offset += frame.wordSize();
			emit(MOVE("move `d0, `s0", frame.A0, args.next().accept(this)));
			uses.add(frame.A0);
		}
		if (args.hasNext()) {
			offset += frame.wordSize();
			emit(MOVE("move `d0, `s0", frame.A1, args.next().accept(this)));
			uses.add(frame.A1);
		}
		if (args.hasNext()) {
			offset += frame.wordSize();
			emit(MOVE("move `d0, `s0", frame.A2, args.next().accept(this)));
			uses.add(frame.A2);
		}
		if (args.hasNext()) {
			offset += frame.wordSize();
			emit(MOVE("move `d0, `s0", frame.A3, args.next().accept(this)));
			uses.add(frame.A3);
		}
		while (args.hasNext()) {
			offset += frame.wordSize();
			emit(OPER("sw `s0, " + offset + "(`s1)", null,
					new Temp[]{args.next().accept(this), frame.SP}));
		}

		if (offset > frame.maxArgOffset)
			frame.maxArgOffset = offset;

		if (s.func instanceof NAME) {
			emit(OPER("jal " + ((NAME)s.func).label.toString(),
					frame.calldefs, uses.toArray(new Temp[]{})));
			return frame.V0;
		}
		uses.addFirst(s.func.accept(this));
		emit(OPER("jal `s0", frame.calldefs, uses.toArray(new Temp[]{})));
		return frame.V0;
	}

	
}
