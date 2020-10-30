package utils.assem;

import utils.temp.*;

public abstract class Instr {
    public String assem;
    public abstract TempList use();
    public abstract TempList def();
    public abstract Targets jumps();

    private Temp nthTemp(TempList l, int i) {
        if (i==0) return l.head;
        else return nthTemp(l.tail,i-1);
    }

    private Label nthLabel(LabelList l, int i) {
        if (i==0) return l.head;
        else return nthLabel(l.tail,i-1);
    }

    public void replaceUse(Temp olduse, Temp newuse) {
        if (use() != null)
            for (int i = 0; i < use().totalSize(); i++)
                if (use().getValue(i) == olduse) use().setValue(newuse, i);
    }
    public void replaceDef(Temp olddef, Temp newdef) {
        if (def() != null)
            for (int i = 0; i< def().totalSize(); i++)
                if (def().getValue(i) == olddef) def().setValue(newdef, i);
    }

    public String format(TempMap m) {
        TempList dst = def();
        TempList src = use();
        Targets j = jumps();
        LabelList jump = (j==null)?null:j.labels;
        StringBuffer s = new StringBuffer();
        int len = assem.length();
        for(int i=0; i<len; i++)
            if (assem.charAt(i)=='`')
                switch(assem.charAt(++i)) {
                    case 's': {int n = Character.digit(assem.charAt(++i),10);
                        s.append(m.tempMap(nthTemp(src,n)));
                    }
                    break;
                    case 'd': {int n = Character.digit(assem.charAt(++i),10);
                        s.append(m.tempMap(nthTemp(dst,n)));
                    }
                    break;
                    case 'j': {int n = Character.digit(assem.charAt(++i),10);
                        s.append(nthLabel(jump,n).toString());
                    }
                    break;
                    case '`': s.append('`');
                        break;
                    default: throw new Error("bad Assem format");
                }
            else s.append(assem.charAt(i));

        return s.toString();
    }


}
