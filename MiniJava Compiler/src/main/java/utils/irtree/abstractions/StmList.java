package utils.irtree.abstractions;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class StmList {
  public Stm head;
  public StmList tail;
  public StmList(Stm h, StmList t) {head=h; tail=t;}
  public String toString() {
        return "StmList(" + ( head != null ? head.toString() : "null") + ", " + (tail != null ? tail.toString() : "null") + ")";
  }

  public static List<Stm> transformIntoJavaList(StmList stmList) {
    List<Stm> transformed = new ArrayList<Stm>();
    if (stmList != null) {
      transformed.add(stmList.head);
      StmList listTail = stmList.tail;
      while (listTail != null) {
        transformed.add(listTail.head);
        listTail = listTail.tail;
      }
    }
    return transformed;
  }

  public static StmList transformIntoStmList(List<Stm> stmList) {
    ListIterator<Stm> listIterator = stmList.listIterator(stmList.size());
    StmList newStmList = null;
    while (listIterator.hasPrevious()) {
      newStmList = new StmList(listIterator.previous(), newStmList);
    }
    return newStmList;
  }
}
