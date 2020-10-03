package utils.irtree;

import utils.irtree.abstractions.Exp;
import utils.irtree.abstractions.ExpList;
import utils.temp.Temp;
import utils.visitor.CodeVisitor;

public class TEMP extends Exp {
    public Temp temp;
    public TEMP(Temp t) {temp=t;}
    public ExpList kids() {return null;}
    public Exp build(ExpList kids) {return this;}

    @Override
    public Temp accept(CodeVisitor v) {
        return v.visit(this);
    }
}