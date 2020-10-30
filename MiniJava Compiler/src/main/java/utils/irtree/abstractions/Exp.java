package utils.irtree.abstractions;

import utils.temp.Temp;
import utils.visitor.CodeVisitor;

abstract public class Exp {
	abstract public ExpList kids();
	abstract public Exp build(ExpList kids);
	public abstract Temp accept(CodeVisitor v);
	abstract public String toString();
}

