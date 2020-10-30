package utils.irtree;

import utils.irtree.abstractions.Exp;
import utils.irtree.abstractions.ExpList;
import utils.temp.Temp;
import utils.visitor.CodeVisitor;

public class CONST extends Exp {
  public int value;
  public CONST(int v) {value=v;}
  public ExpList kids() {return null;}
  public Exp build(ExpList kids) {return this;}

  @Override
  public Temp accept(CodeVisitor v) {
    return v.visit(this);
  }

  public String toString() {
      return "CONST(" + value + ")";
  }
}
