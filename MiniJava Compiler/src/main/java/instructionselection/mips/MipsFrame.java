package instructionselection.mips;

import utils.assem.Instr;
import utils.assem.OPER;
import utils.frame.Access;
import utils.frame.Frame;
import utils.irtree.*;
import utils.irtree.abstractions.Exp;
import utils.irtree.abstractions.ExpList;
import utils.irtree.abstractions.Stm;
import utils.symboltable.Symbol;
import utils.temp.Label;
import utils.temp.Temp;
import utils.temp.TempList;

import java.util.*;

public class MipsFrame extends Frame {

	//Mini Java Library will be appended to end of
	//program
	public String programTail() {

		return
						"   .text             \n" +
						"_initArray:          \n" +
						"   sll $a0,$a0,2     \n" +
						"   li $v0,9          \n" +
						"   syscall           \n" +
						"   move $a2,$v0      \n" +
						"   b Lrunt2          \n" +
						"   Lrunt1:           \n" +
						"   sw $a1,($a2)      \n" +
						"   sub $a0,$a0,4     \n" +
						"   add $a2,$a2,4     \n" +
						"   Lrunt2:           \n" +
						"   bgtz $a0, Lrunt1  \n" +
						"   j $ra             \n" +
						"                     \n" +
						"_initRecord:         \n" +
						"   li $v0,9          \n" +
						"   syscall           \n" +
						"   move $a2,$v0      \n" +
						"   b Lrunt4          \n" +
						"   Lrunt3:           \n" +
						"   sw $0,($a2)       \n" +
						"   sub $a0,$a0,4     \n" +
						"   add $a2,$a2,4     \n" +
						"   Lrunt4:           \n" +
						"   bgtz $a0, Lrunt3  \n" +
						"   j $ra             \n" +
						"                     \n" +
						"_stringEqual:        \n" +
						"   beq $a0,$a1,Lrunt10\n" +
						"   lw  $a2,($a0)     \n" +
						"   lw  $a3,($a1)     \n" +
						"   addiu $a0,$a0,4   \n" +
						"   addiu $a1,$a1,4   \n" +
						"   beq $a2,$a3,Lrunt11\n" +
						"   Lrunt13:          \n" +
						"   li  $v0,0         \n" +
						"   j $ra             \n" +
						"   Lrunt12:          \n" +
						"   lbu  $t0,($a0)    \n" +
						"   lbu  $t1,($a1)    \n" +
						"   bne  $t0,$t1,Lrunt13\n" +
						"   addiu $a0,$a0,1   \n" +
						"   addiu $a1,$a1,1   \n" +
						"   addiu $a2,$a2,-1  \n" +
						"   Lrunt11:          \n" +
						"   bgez $a2,Lrunt12  \n" +
						"   Lrunt10:          \n" +
						"   li $v0,1          \n" +
						"   j $ra             \n" +
						"                     \n" +
						"_print:              \n" +
						"   lw $a1,0($a0)     \n" +
						"   add $a0,$a0,4     \n" +
						"   add $a2,$a0,$a1   \n" +
						"   lb $a3,($a2)      \n" +
						"   sb $0,($a2)       \n" +
						"   li $v0,4          \n" +
						"   syscall           \n" +
						"   sb $a3,($a2)      \n" +
						"   j $ra             \n" +
						"                     \n" +
						"_flush:              \n" +
						"	j $ra             \n" +
						"                     \n" +
						".data                \n" +
						"Runtconsts:          \n" +
						".space 2048          \n" +
						"Runtempty: .word 0   \n" +
						"                     \n" +
						".text                \n" +
						"                     \n" +
						"_main:               \n" +
						"   li $a0,0          \n" +
						"   la $a1,Runtconsts \n" +
						"   li $a2,1          \n" +
						"   Lrunt20:          \n" +
						"   sw $a2,($a1)      \n" +
						"   sb $a0,4($a1)     \n" +
						"   addiu $a1,$a1,8   \n" +
						"   slt $a3,$a0,256   \n" +
						"   bnez $a3,Lrunt20  \n" +
						"   li $a0,0          \n" +
						"   j t_main          \n" +
						"                     \n" +
						"_ord:                \n" +
						"   lw $a1,($a0)      \n" +
						"   li $v0,-1         \n" +
						"   beqz $a1,Lrunt5   \n" +
						"   lbu $v0,4($a0)    \n" +
						"   Lrunt5:           \n" +
						"	j $ra             \n" +
						"                     \n" +
						".data                \n" +
						"Lrunt30: .asciiz \"character out of range\\n\"\n" +
						".text                \n" +
						"                     \n" +
						"chr:                 \n" +
						"   andi $a1,$a0,255  \n" +
						"   bnez $a1,Lrunt31  \n" +
						"   sll  $a0,$a0,3    \n" +
						"   la   $v0,Runtconsts($a0)\n" +
						"   j $ra             \n" +
						"   Lrunt31:          \n" +
						"   li   $v0,4        \n" +
						"   la   $a0,Lrunt30  \n" +
						"   syscall           \n" +
						"   li   $v0,10       \n" +
						"   syscall           \n" +
						"                     \n" +
						"_size:               \n" +
						"   lw $v0,($a0)      \n" +
						"	j $ra             \n" +
						"                     \n" +
						".data                \n" +
						"Lrunt40:  .asciiz \"substring out of bounds\\n\"\n" +
						"                     \n" +
						"_substring:          \n" +
						"   lw $t1,($a0)      \n" +
						"   bltz $a1,Lrunt41  \n" +
						"   add $t2,$a1,$a2   \n" +
						"   sgt $t3,$t2,$t1   \n" +
						"   bnez $t3,Lrunt41  \n" +
						"   add $t1,$a0,$a1   \n" +
						"   bne $a2,1,Lrunt42 \n" +
						"   lbu $a0,($t1)     \n" +
						"   b chr             \n" +
						"   Lrunt42:          \n" +
						"   bnez $a2,Lrunt43  \n" +
						"   la  $v0,Lruntempty\n" +
						"   j $ra             \n" +
						"   Lrunt43:          \n" +
						"   addi $a0,$a2,4    \n" +
						"   li   $v0,9        \n" +
						"   syscall           \n" +
						"   move $t2,$v0      \n" +
						"   Lrunt44:          \n" +
						"   lbu  $t3,($t1)    \n" +
						"   sb   $t3,($t2)    \n" +
						"   addiu $t1,1       \n" +
						"   addiu $t2,1       \n" +
						"   addiu $a2,-1      \n" +
						"   bgtz $a2,Lrunt44  \n" +
						"   j $ra             \n" +
						"   Lrunt41:          \n" +
						"   li   $v0,4        \n" +
						"   la   $a0,Lrunt40  \n" +
						"   syscall           \n" +
						"   li   $v0,10       \n" +
						"	syscall           \n" +
						"                     \n" +
						"_concat:             \n" +
						"   lw $t0,($a0)      \n" +
						"   lw $t1,($a1)      \n" +
						"   beqz $t0,Lrunt50  \n" +
						"   beqz $t1,Lrunt51  \n" +
						"   addiu  $t2,$a0,4  \n" +
						"   addiu  $t3,$a1,4  \n" +
						"   add  $t4,$t0,$t1  \n" +
						"   addiu $a0,$t4,4   \n" +
						"   li $v0,9          \n" +
						"   syscall           \n" +
						"   addiu $t5,$v0,4   \n" +
						"   sw $t4,($v0)      \n" +
						"   Lrunt52:          \n" +
						"   lbu $a0,($t2)     \n" +
						"   sb  $a0,($t5)     \n" +
						"   addiu $t2,1       \n" +
						"   addiu $t5,1       \n" +
						"   addiu $t0,-1      \n" +
						"   bgtz $t0,Lrunt52  \n" +
						"   Lrunt53:          \n" +
						"   lbu $a0,($t4)     \n" +
						"   sb  $a0,($t5)     \n" +
						"   addiu $t4,1       \n" +
						"   addiu $t5,1       \n" +
						"   addiu $t2,-1      \n" +
						"   bgtz $t2,Lrunt52  \n" +
						"   j $ra             \n" +
						"   Lrunt50:          \n" +
						"   move $v0,$a1      \n" +
						"   j $ra             \n" +
						"   Lrunt51:          \n" +
						"   move $v0,$a0      \n" +
						"   j $ra             \n" +
						"                     \n" +
						"_not:                \n" +
						"   seq $v0,$a0,0     \n" +
						"	j $ra             \n" +
						"                     \n" +
						".data                \n" +
						"getchbuf: .space 200    \n" +
						"getchptr: .word getchbuf\n" +
						"                     \n" +
						".text                \n" +
						"_getchar:            \n" +
						"    lw  $a0,getchptr \n" +
						"    lbu $v0,($a0)    \n" +
						"    add $a0,$a0,1    \n" +
						"    bnez $v0,Lrunt6  \n" +
						"    li $v0,8         \n" +
						"    la $a0,getchbuf  \n" +
						"    li $a1,200       \n" +
						"    syscall          \n" +
						"    la $a0,getchbuf  \n" +
						"    lbu $v0,($a0)    \n" +
						"    bnez $v0,Lrunt6  \n" +
						"    li $v0,-1        \n" +
						"    Lrunt6:          \n" +
						"    sw $a0,getchptr  \n" +
						"    j $ra            \n" +
						"    .text            \n" +
						"    .globl _halloc   \n" +
						"_halloc:             \n" +
						"    li $v0, 9        \n" +
						"    syscall          \n" +
						"    j $ra            \n" +
						"                     \n" +
						"    .text            \n" +
						"    .globl _printint \n" +
						"_printint:           \n" +
						"    li $v0, 1        \n" +
						"    syscall          \n" +
						"    la $a0, newl     \n" +
						"    li $v0, 4        \n" +
						"    syscall          \n" +
						"    j $ra            \n" +
						"                     \n" +
						"    .data            \n" +
						"    .align   0       \n" +
						"newl:    .asciiz \"\\n\"  \n" +
						"    .data           \n" +
						"    .align   0      \n" +
						"str_er:  .asciiz \" ERROR: abnormal termination\\n\" "+
						"                    \n" +
						"   .text           \n" +
						"   .globl _error    \n" +
						"_error:             \n" +
						"   li $v0, 4        \n" +
						"   la $a0, str_er   \n" +
						"   syscall          \n" +
						"   li $v0, 10       \n" +
						"	syscall          \n" ;
	}

	public Frame newFrame(Symbol name, List<Boolean> formals) {
		if (this.name != null) name = Symbol.symbol(this.name + "." + name);
		return new MipsFrame(name, formals);
	}

	public MipsFrame() {}

	private static HashMap<Symbol,Integer> functions = new HashMap<Symbol,Integer>();

	private MipsFrame(Symbol n, List<Boolean> f) {
		Integer count = functions.get(n);
		if (count == null) {
			count = new Integer(0);
			name = new Label(n);
		} else {
			count = new Integer(count.intValue() + 1);
			name = new Label(n + "." + count);
		}
		functions.put(n, count);
		actuals = new LinkedList<Access>();
		formals = new LinkedList<Access>();
		int offset = 0;
		Iterator<Boolean> escapes = f.iterator();
		formals.add(allocLocal(escapes.next().booleanValue()));
		actuals.add(new InReg(V0));
		for (int i = 0; i < argRegs.length; ++i) {
			if (!escapes.hasNext())
				break;
			offset += wordSize;
			actuals.add(new InReg(argRegs[i]));
			if (escapes.next().booleanValue())
				formals.add(new InFrame(offset));
			else
				formals.add(new InReg(new Temp()));
		}
		while (escapes.hasNext()) {
			offset += wordSize;
			Access actual = new InFrame(offset);
			actuals.add(actual);
			if (escapes.next().booleanValue())
				formals.add(actual);
			else
				formals.add(new InReg(new Temp()));
		}
	}

	private static final int wordSize = 4;
	public int wordSize() { return wordSize; }

	private int offset = 0;
	public Access allocLocal(boolean escape) {
		if (escape) {
			Access result = new InFrame(offset);
			offset -= wordSize;
			return result;
		} else
			return new InReg(new Temp());
	}

	static final Temp ZERO = new Temp(); // zero reg
	public static final Temp AT = new Temp(); // reserved for assembler
	static final Temp V0 = new Temp(); // function result
	static final Temp V1 = new Temp(); // second function result
	public static final Temp A0 = new Temp(); // argument1
	public static final Temp A1 = new Temp(); // argument2
	public static final Temp A2 = new Temp(); // argument3
	public static final Temp A3 = new Temp(); // argument4
	static final Temp T0 = new Temp(); // caller-saved
	static final Temp T1 = new Temp();
	static final Temp T2 = new Temp();
	static final Temp T3 = new Temp();
	static final Temp T4 = new Temp();
	static final Temp T5 = new Temp();
	static final Temp T6 = new Temp();
	static final Temp T7 = new Temp();
	static final Temp S0 = new Temp(); // callee-saved
	static final Temp S1 = new Temp();
	static final Temp S2 = new Temp();
	static final Temp S3 = new Temp();
	static final Temp S4 = new Temp();
	static final Temp S5 = new Temp();
	static final Temp S6 = new Temp();
	static final Temp S7 = new Temp();
	static final Temp T8 = new Temp(); // caller-saved
	static final Temp T9 = new Temp();
	static final Temp K0 = new Temp(); // reserved for OS kernel
	static final Temp K1 = new Temp(); // reserved for OS kernel
	static final Temp GP = new Temp(); // pointer to global area
	static final Temp SP = new Temp(); // stack pointer
	static final Temp S8 = new Temp(); // callee-save (frame pointer)
	static final Temp RA = new Temp(); // return address

	// Register lists: must not overlap and must include every register that
	// might show up in code
	public static final Temp[]
			// registers dedicated to special purposes
			specialRegs = { ZERO, AT, K0, K1, GP, SP },
	// registers to pass outgoing arguments
	argRegs	= { A0, A1, A2, A3 },
	// registers that a callee must preserve for its caller
	calleeSaves = { RA, S0, S1, S2, S3, S4, S5, S6, S7, S8 },
	// registers that a callee may use without preserving
	callerSaves = { T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, V0, V1 };

	static final Temp FP = new Temp(); // virtual frame pointer (eliminated)
	public Temp FP() { return FP; }
	public Temp RV() { return V0; }

	private static HashMap<String,Label> labels = new HashMap<String,Label>();
	public Exp externalCall(String s, List<Exp> args) {
		String func = s.intern();
		Label l = labels.get(func);
		if (l == null) {
			l = new Label("_" + func);
			labels.put(func, l);
		}
		args.add(0, new CONST(0));
		ExpList expList = ExpList.createExpList(args);
		return new CALL(new NAME(l), expList);
	}

	public Stm externalCallStm(String s, List<Exp> args) {
		String func = s.intern();
		Label l = labels.get(func);
		if (l == null) {
			l = new Label("_" + func);
			labels.put(func, l);
		}
		args.add(0, new CONST(0));
		ExpList expList = ExpList.createExpList(args);
		return new EXP(new CALL(new NAME(l), expList));

	}

	public String string(Label lab, String string) {
		int length = string.length();
		String lit = "";
		for (int i = 0; i < length; i++) {
			char c = string.charAt(i);
			switch(c) {
				case '\b': lit += "\\b"; break;
				case '\t': lit += "\\t"; break;
				case '\n': lit += "\\n"; break;
				case '\f': lit += "\\f"; break;
				case '\r': lit += "\\r"; break;
				case '\"': lit += "\\\""; break;
				case '\\': lit += "\\\\"; break;
				default:
					if (c < ' ' || c > '~') {
						int v = (int)c;
						lit += "\\" + ((v>>6)&7) + ((v>>3)&7) + (v&7);
					} else
						lit += c;
					break;
			}
		}
		return "\t.data\n\t.word " + length + "\n" + lab.toString()
				+ ":\t.asciiz\t\"" + lit + "\"";
	}

	private static final Label badPtr = new Label("BADPTR");
	public Label badPtr() {
		return badPtr;
	}

	private static final Label badSub = new Label("BADSUB");
	public Label badSub() {
		return badSub;
	}

	private static final
	HashMap<Temp,String> tempMap = new HashMap<Temp,String>(32);
	static {
		tempMap.put(ZERO, "$0");
		tempMap.put(AT,   "$at");
		tempMap.put(V0,   "$v0");
		tempMap.put(V1,   "$v1");
		tempMap.put(A0,   "$a0");
		tempMap.put(A1,   "$a1");
		tempMap.put(A2,   "$a2");
		tempMap.put(A3,   "$a3");
		tempMap.put(T0,   "$t0");
		tempMap.put(T1,   "$t1");
		tempMap.put(T2,   "$t2");
		tempMap.put(T3,   "$t3");
		tempMap.put(T4,   "$t4");
		tempMap.put(T5,   "$t5");
		tempMap.put(T6,   "$t6");
		tempMap.put(T7,   "$t7");
		tempMap.put(S0,   "$s0");
		tempMap.put(S1,   "$s1");
		tempMap.put(S2,   "$s2");
		tempMap.put(S3,   "$s3");
		tempMap.put(S4,   "$s4");
		tempMap.put(S5,   "$s5");
		tempMap.put(S6,   "$s6");
		tempMap.put(S7,   "$s7");
		tempMap.put(T8,   "$t8");
		tempMap.put(T9,   "$t9");
		tempMap.put(K0,   "$k0");
		tempMap.put(K1,   "$k1");
		tempMap.put(GP,   "$gp");
		tempMap.put(SP,   "$sp");
		tempMap.put(S8,   "$fp");
		tempMap.put(RA,   "$ra");
	}
	public String tempMap(Temp temp) {
		return tempMap.get(temp);
	}

	int maxArgOffset = 0;

	public List<Instr> codegen(List<Stm> stms) {
		List<Instr> insns = new java.util.LinkedList<Instr>();
		Codegen cg = new Codegen(this, insns.listIterator());
		for (java.util.Iterator<Stm> s = stms.iterator(); s.hasNext(); )
			s.next().accept(cg);
		procEntryExit2(insns);
		procEntryExit3(insns);
		return insns;
	}

	private static <R> void addAll(java.util.Collection<R> c, R[] a) {
		for (int i = 0; i < a.length; i++)
			c.add(a[i]);
	}

	// Registers live on return
	private static Temp[] returnSink = {};
	{
		LinkedList<Temp> l = new LinkedList<Temp>();
		l.add(V0);
		addAll(l, specialRegs);
		addAll(l, calleeSaves);
		returnSink = l.toArray(returnSink);
	}

	// Registers defined by a call
	static Temp[] calldefs = {};
	{
		LinkedList<Temp> l = new LinkedList<Temp>();
		l.add(RA);
		addAll(l, argRegs);
		addAll(l, callerSaves);
		calldefs = l.toArray(calldefs);
	}

	public static Temp[] functionResult = {};
	{
		LinkedList<Temp> l = new LinkedList<Temp>();
		l.add(RA);
		functionResult = l.toArray(functionResult);
	}

	private static Stm SEQ(Stm left, Stm right) {
		if (left == null)
			return right;
		if (right == null)
			return left;
		return new SEQ(left, right);
	}
	private static MOVE MOVE(Exp d, Exp s) {
		return new MOVE(d, s);
	}
	private static TEMP TEMP(Temp t) {
		return new TEMP(t);
	}

	private void
	assignFormals(Iterator<Access> formals,
				  Iterator<Access> actuals,
				  List<Stm> body)
	{
		if (!formals.hasNext() || !actuals.hasNext())
			return;
		Access formal = formals.next();
		Access actual = actuals.next();
		assignFormals(formals, actuals, body);
		body.add(0, MOVE(formal.exp(TEMP(FP)), actual.exp(TEMP(FP))));
	}

	private void assignCallees(int i, List<Stm> body)
	{
		if (i >= calleeSaves.length)
			return;
		Access a = allocLocal(!spilling);
		assignCallees(i+1, body);
		body.add(0, MOVE(a.exp(TEMP(FP)), TEMP(calleeSaves[i])));
		body.add(MOVE(TEMP(calleeSaves[i]), a.exp(TEMP(FP))));
	}

	private List<Access> actuals;

	private static Instr OPER(String a, Temp[] d, Temp[] s) {
		TempList listD = TempList.createTempList(d);
		TempList listS = TempList.createTempList(s);
		return new OPER(a, listD, listS, null);
	}

	public void procEntryExit1(List<Stm> body) {
		assignFormals(formals.iterator(), actuals.iterator(), body);
		assignCallees(0, body);
	}

	public void procEntryExit2(List<Instr> body) {
		body.add(OPER("#\treturn", null, returnSink));
	}

	public void procEntryExit3(List<Instr> body) {
		int frameSize = maxArgOffset - offset;
		ListIterator<Instr> cursor = body.listIterator();
		cursor.add(OPER("\t.text", null, null));
		cursor.add(OPER(name + ":", null, null));
		if (!name.toString().contains("main")) {
			cursor.add(OPER(name + "_framesize=" + frameSize, null, null));
			if (frameSize != 0) {
				cursor.add(OPER("\tsubu $sp, $sp, " + name + "_framesize",
						new Temp[]{SP}, new Temp[]{SP}));
				body.add(OPER("\taddu $sp, $sp, " + name + "_framesize",
						new Temp[]{SP}, new Temp[]{SP}));
			}
			body.add(OPER("\tj $ra", null, new Temp[]{RA}));
		}
	}

	private static Temp[] registers = {};
	{
		LinkedList<Temp> l = new LinkedList<Temp>();
		addAll(l, callerSaves);
		addAll(l, calleeSaves);
		addAll(l, argRegs);
		addAll(l, specialRegs);
		registers = l.toArray(registers);
	}

	public TempList registers() {
		return TempList.createTempList(registers);
	}

	private static Temp[] specialRegisters = {};
	{
		LinkedList<Temp> l = new LinkedList<Temp>();
		addAll(l, specialRegs);
		specialRegisters = l.toArray(specialRegisters);
	}

	public TempList specialRegisters() {
		return TempList.createTempList(specialRegisters);
	}

	public static Temp[] notSpecialRegisters = {};
	{
		LinkedList<Temp> l = new LinkedList<Temp>();
		addAll(l, callerSaves);
		addAll(l, calleeSaves);
		notSpecialRegisters = l.toArray(notSpecialRegisters);
	}

	public TempList notSpecialRegisters() {
		return TempList.createTempList(notSpecialRegisters);
	}

	private static boolean spilling = true ;
	// set spilling to true when the spill method is implemented
	public List<Temp> spill(List<Instr> insns, List<Temp> spillsList) {
		List<Temp> newTemps = new ArrayList<>();
		if (spillsList != null) {
			Temp[] spills = new Temp[spillsList.size()];
			for (int i = 0; i < spillsList.size(); i++)
				spills[i] = spillsList.get(i);
			if (!spilling) {
				for (Temp spill : spills) System.err.println("Need to spill " + spill);
				throw new Error("Spilling unimplemented");
			}
			else for (Temp spill : spills) {
				Exp exp = allocLocal(true).exp(TEMP(FP));
				for (ListIterator<Instr> i = insns.listIterator();
					 i.hasNext(); ) {
					Instr insn = i.next();
					TempList useList = insn.use();
					if (useList != null) {
						Temp[] use = useList.createTempArray();
						if (use != null)
							for (Temp temp : use) {
								if (temp == spill) {
									Temp t = new Temp();
									t.spillTemp = true;
									Stm stm = MOVE(TEMP(t), exp);
									i.previous();
									stm.accept(new Codegen(this, i));
									if (insn != i.next())
										throw new Error();
									insn.replaceUse(spill, t);
									newTemps.add(t);
									break;
								}
							}
					}
					TempList defList = insn.def();
					if (defList != null) {
						Temp[] def = defList.createTempArray();
						if (def != null)
							for (Temp temp : def) {
								if (temp == spill) {
									Temp t = new Temp();
									t.spillTemp = true;
									insn.replaceDef(spill, t);
									Stm stm = MOVE(exp, TEMP(t));
									stm.accept(new Codegen(this, i));
									newTemps.add(t);
									break;
								}
							}
					}
				}
			}
		}
		return newTemps;
	}
}








