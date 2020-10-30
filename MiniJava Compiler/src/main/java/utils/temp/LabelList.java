package utils.temp;

import java.util.ArrayList;
import java.util.List;

public class LabelList {
    public Label head;
    public LabelList tail;
    public LabelList(Label h, LabelList t) {head=h; tail=t;}

    public static LabelList createLabelList(List<Label> labelList) {
        if (labelList != null) {
            LabelList list = null;
            if (!labelList.isEmpty()) {
                int size = labelList.size();
                List<Label> newLabelList = null;
                if (size > 1) {
                    newLabelList = new ArrayList<>(size - 1);
                    for(int i = 1; i < size; i++) {
                        newLabelList.add(labelList.get(i));
                    }
                }
                list = new LabelList(labelList.get(0), createLabelList(newLabelList));
            }
            return list;
        }
        return null;
    }

    public List<Label> createListOfLabel() {
        List<Label> list = null;
        int size = totalSize();
        if (size > 0) {
            list = new ArrayList<>();
            list.add(0, head);
            if (size > 1 && tail != null) {
                List<Label> tailList = tail.createListOfLabel();
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
