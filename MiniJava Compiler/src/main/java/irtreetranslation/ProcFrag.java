package irtreetranslation;

import utils.frame.Frame;
import utils.irtree.abstractions.Stm;

public class ProcFrag extends Frag {
    public Stm body;
    public Frame frame;

    ProcFrag(final Stm b, final Frame f) {
        body = b;
        frame = f;
    }

    @Override
    public Stm getBody() {
        return this.body;
    }
}
