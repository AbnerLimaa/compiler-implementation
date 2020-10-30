package utils.temp;

import java.util.HashSet;
import java.util.Arrays;

public class TempList {
    public Temp head;
    public TempList tail;
    public TempList(Temp h, TempList t) {head=h; tail=t;}

    public static TempList createTempList(Temp[] d) {
        if (d != null) {
            TempList list = null;
            if (d.length > 0) {
                Temp[] tempArray = null;
                if (d.length > 1) {
                    tempArray = new Temp[d.length - 1];
                    System.arraycopy(d, 1, tempArray, 0, d.length - 1);
                }
                list = new TempList(d[0], createTempList(tempArray));
            }
            return list;
        }
        return null;
    }

    public Temp[] createTempArray() {
        Temp[] array = null;
        int size = totalSize();
        if (size > 0) {
            array = new Temp[size];
            array[0] = head;
            if (size > 1 && tail != null) {
                Temp[] tailArray = tail.createTempArray();
                if (tailArray != null && tailArray.length - 1 >= 0)
                    System.arraycopy(tailArray, 0, array, 1, tailArray.length - 1);
            }
        }
        return array;
    }

    public HashSet<Temp> createTempSet () {
        Temp[] tempArray = this.createTempArray();
        HashSet<Temp> tempSet = new HashSet<Temp>(Arrays.asList(tempArray));

        return tempSet;
    }

    public int totalSize() {
        int thisSize = 0;
        if (head != null)
            thisSize = 1;
        if (tail != null)
            return thisSize + tail.totalSize();
        return thisSize;
    }

    public Temp getValue(int pos) {
        Temp result = null;
        if (pos >= 0) {
            if (pos == 0 && head != null)
                result = head;
            else if (tail != null)
                return tail.getValue(pos - 1);
        }
        return result;
    }

    public void setValue(Temp value, int pos) {
        if (pos >= 0) {
            if (pos == 0)
                head = value;
            else if (tail != null)
                tail.setValue(value,pos - 1);
        }
    }

    @Override
    public String toString() {
        String result = "";
        if (head != null)
            result = head.toString();
        if (tail != null)
            result = result + " " + tail.toString();
        return result;
    }
}

