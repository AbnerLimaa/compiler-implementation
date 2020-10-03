package utils.irtree;

import utils.irtree.abstractions.Exp;
import utils.irtree.abstractions.ExpList;
import utils.temp.Temp;
import utils.visitor.CodeVisitor;

public class CALL extends Exp {
    public Exp func;
    public ExpList args;
    public CALL(Exp f, ExpList a) {func=f; args=a;}
    public ExpList kids() {return new ExpList(func,args);}
    public Exp build(ExpList kids) {
        return new CALL(kids.head,kids.tail);
    }

    @Override
    public Temp accept(CodeVisitor v) {
        return v.visit(this);
    }
}
