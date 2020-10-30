package utils.syntaxtree;

import utils.visitor.*;

public abstract class Type {
  public abstract void accept(Visitor v);
  public abstract Type accept(TypeVisitor v);
}
