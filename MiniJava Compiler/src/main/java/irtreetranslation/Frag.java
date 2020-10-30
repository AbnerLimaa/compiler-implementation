package irtreetranslation;

import utils.irtree.abstractions.Stm;

public abstract class Frag {
    public Frag next;
    public abstract Stm getBody();
}
