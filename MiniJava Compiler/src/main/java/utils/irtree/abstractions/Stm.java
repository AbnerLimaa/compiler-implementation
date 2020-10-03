package utils.irtree.abstractions;

import utils.visitor.CodeVisitor;

abstract public class Stm {
    abstract public ExpList kids();
    abstract public Stm build(ExpList kids);
    public abstract void accept(CodeVisitor v);
}
