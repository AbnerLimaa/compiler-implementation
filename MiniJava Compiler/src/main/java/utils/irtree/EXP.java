package utils.irtree;

import utils.irtree.abstractions.Exp;
import utils.irtree.abstractions.ExpList;
import utils.irtree.abstractions.Stm;
import utils.visitor.CodeVisitor;

public class EXP extends Stm {

    public Exp exp;
    public EXP(Exp e) {exp=e;}
    public ExpList kids() {return new ExpList(exp,null);}
    public Stm build(ExpList kids) {
        return new EXP(kids.head);
    }
    @Override
    public void accept(CodeVisitor v) {
        v.visit(this);
    }
    public String toString() {
        return "EXP(" + exp.toString() + ")";
    }
}
