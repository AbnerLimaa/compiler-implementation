package utils.irtree;

import utils.irtree.abstractions.Exp;
import utils.irtree.abstractions.ExpList;
import utils.irtree.abstractions.Stm;
import utils.visitor.CodeVisitor;

public class MOVE extends Stm {
  public Exp dst, src;
  public MOVE(Exp d, Exp s) {dst=d; src=s;}
  public ExpList kids() {
        if (dst instanceof MEM)
	   return new ExpList(((MEM)dst).exp, new ExpList(src,null));
	else return new ExpList(src,null);
  }
  public Stm build(ExpList kids) {
        if (dst instanceof MEM)
	   return new MOVE(new MEM(kids.head), kids.tail.head);
	else return new MOVE(dst, kids.head);
  }
    @Override
    public void accept(CodeVisitor v) {
        v.visit(this);
    }
  public String toString() {
      return "MOVE(" + (dst != null ? dst.toString() : "null") + ", " + (src != null ? src.toString() : "null") + ")";
  }
}
