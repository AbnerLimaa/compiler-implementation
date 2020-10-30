package utils.frame;
import utils.assem.Instr;
import utils.irtree.abstractions.Exp;
import utils.irtree.abstractions.Stm;
import utils.temp.Label;
import utils.temp.Temp;
import utils.temp.TempList;
import utils.temp.TempMap;
import utils.symboltable.Symbol;

import java.util.List;

public abstract class Frame implements TempMap {
    public Label name;
    public List<Access> formals;
    public abstract Frame newFrame(Symbol name, List<Boolean> formals);
    public abstract Access allocLocal(boolean escape);
    public abstract Temp FP();
    public abstract int wordSize();
    public abstract Exp externalCall(String func, List<Exp> args);
    public abstract Stm externalCallStm(String func, List<Exp> args);
    public abstract Temp RV();
    public abstract String string(Label label, String value);
    public abstract Label badPtr();
    public abstract Label badSub();
    public abstract String tempMap(Temp temp);
    public abstract List<Instr> codegen(List<Stm> stms);
    public abstract void procEntryExit1(List<Stm> body);
    public abstract void procEntryExit2(List<Instr> body);
    public abstract void procEntryExit3(List<Instr> body);
    public abstract TempList registers();
    public abstract TempList specialRegisters();
    public abstract TempList notSpecialRegisters();
    public abstract List<Temp> spill(List<Instr> insns, List<Temp> spills);
    public abstract String programTail(); //append to end of target code
}

