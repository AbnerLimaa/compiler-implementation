package utils.irtree.abstractions;

import utils.temp.Label;

import java.util.ArrayList;
import java.util.List;

public class ExpList {
    public Exp head;
    public ExpList tail;
    public ExpList(Exp h, ExpList t) {head=h; tail=t;}

    public static ExpList createExpList(List<Exp> expList) {
        if (expList != null) {
            ExpList list = null;
            if (!expList.isEmpty()) {
                int size = expList.size();
                List<Exp> newExpList = null;
                if (size > 1) {
                    newExpList = new ArrayList<>(size - 1);
                    for(int i = 1; i < size; i++) {
                        newExpList.add(expList.get(i));
                    }
                }
                list = new ExpList(expList.get(0), createExpList(newExpList));
            }
            return list;
        }
        return null;
    }

    public List<Exp> createListOfExp() {
        List<Exp> list = null;
        int size = totalSize();
        if (size > 0) {
            list = new ArrayList<>();
            list.add(0, head);
            if (size > 1 && tail != null) {
                List<Exp> tailList = tail.createListOfExp();
                list.addAll(tailList);
            }
        }
        return list;
    }

    public int totalSize() {
        int thisSize = 0;
        if (head != null)
            thisSize = 1;
        if (tail != null)
            return thisSize + tail.totalSize();
        return thisSize;
    }
}
