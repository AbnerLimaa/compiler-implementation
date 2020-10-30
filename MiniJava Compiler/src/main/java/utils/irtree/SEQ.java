package utils.irtree;

import utils.irtree.abstractions.ExpList;
import utils.irtree.abstractions.Stm;
import utils.visitor.CodeVisitor;

public class SEQ extends Stm {
  public Stm left, right;
  public SEQ(Stm l, Stm r) { left=l; right=r; }
  public ExpList kids() {throw new Error("kids() not applicable to SEQ");}
  public Stm build(ExpList kids) {throw new Error("build() not applicable to SEQ");}
  @Override
  public void accept(CodeVisitor v) {
    v.visit(this);
  }
  public String toString() {
      return "SEQ(" + (left != null ? left.toString() : "null") + ", " + (right != null ? right.toString() : "null") + ")";
  }
}

