package utils.syntaxtree;
import utils.visitor.Visitor;
import utils.visitor.TypeVisitor;

public abstract class Type {
  public abstract void accept(Visitor v);
  public abstract Type accept(TypeVisitor v);
}
