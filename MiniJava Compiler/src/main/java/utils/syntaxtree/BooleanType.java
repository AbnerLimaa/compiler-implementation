package utils.syntaxtree;

import utils.visitor.*;

public class BooleanType extends Type {
  public void accept(Visitor v) {
    v.visit(this);
  }

  public Type accept(TypeVisitor v) {
    return v.visit(this);
  }

  @Override
  public String toString() {
    return "boolean";
  }

}
