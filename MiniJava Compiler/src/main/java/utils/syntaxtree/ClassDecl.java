package utils.syntaxtree;

import utils.visitor.*;

public abstract class ClassDecl {

  public Identifier i;

  public abstract void accept(Visitor v);
  public abstract Type accept(TypeVisitor v);
  public abstract MethodDeclList getMethods();
}
