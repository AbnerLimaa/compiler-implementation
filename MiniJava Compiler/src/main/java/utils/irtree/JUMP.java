package utils.irtree;

import utils.irtree.abstractions.Exp;
import utils.irtree.abstractions.ExpList;
import utils.irtree.abstractions.Stm;
import utils.temp.Label;
import utils.temp.LabelList;
import utils.visitor.CodeVisitor;

public class JUMP extends Stm {
    public Exp exp;
    public LabelList targets;
    public JUMP(Exp e, LabelList t) {exp=e; targets=t;}
    public JUMP(Label target) {
        this(new NAME(target), new LabelList(target,null));
    }
    public ExpList kids() {return new ExpList(exp,null);}
    public Stm build(ExpList kids) {
        return new JUMP(kids.head,targets);
    }

    @Override
    public void accept(CodeVisitor v) {
        v.visit(this);
    }
}
