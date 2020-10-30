package irtreetranslation;

import utils.irtree.abstractions.Stm;

public class DataFrag extends Frag {
    private String data;
    public DataFrag(String data) {
        this.data = data;
    }

    @Override
    public Stm getBody() { return null; }
}
