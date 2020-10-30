package utils.irtree;

import utils.irtree.abstractions.ExpList;
import utils.irtree.abstractions.Stm;
import utils.temp.Label;
import utils.visitor.CodeVisitor;

public class LABEL extends Stm {
  public Label label;
  public LABEL(Label l) {label=l;}
  public ExpList kids() {return null;}
  public Stm build(ExpList kids) {
    return this;
  }
  @Override
  public void accept(CodeVisitor v) {
    v.visit(this);
  }
  public String toString() {
      return "LABEL(" + label.toString() + ")";
  }
}
