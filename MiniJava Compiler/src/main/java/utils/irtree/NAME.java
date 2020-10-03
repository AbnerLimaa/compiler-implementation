package utils.irtree;

import utils.irtree.abstractions.Exp;
import utils.irtree.abstractions.ExpList;
import utils.temp.Label;
import utils.temp.Temp;
import utils.visitor.CodeVisitor;

public class NAME extends Exp {
    public Label label;
    public NAME(Label l) {label=l;}
    public ExpList kids() {return null;}
    public Exp build(ExpList kids) {return this;}

    @Override
    public Temp accept(CodeVisitor v) {
        return v.visit(this);
    }
}