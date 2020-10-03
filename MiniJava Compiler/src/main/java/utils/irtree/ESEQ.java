package utils.irtree;

import utils.irtree.abstractions.Exp;
import utils.irtree.abstractions.ExpList;
import utils.irtree.abstractions.Stm;
import utils.temp.Temp;
import utils.visitor.CodeVisitor;

public class ESEQ extends Exp {
    public Stm stm;
    public Exp exp;
    public ESEQ(Stm s, Exp e) {stm=s; exp=e;}
    public ExpList kids() {throw new Error("kids() not applicable to ESEQ");}
    public Exp build(ExpList kids) {throw new Error("build() not applicable to ESEQ");}

    @Override
    public Temp accept(CodeVisitor v) {
        return v.visit(this);
    }
}
