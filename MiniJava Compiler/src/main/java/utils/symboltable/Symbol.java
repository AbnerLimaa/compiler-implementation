package utils.symboltable;

public class Symbol {

    protected String name;

    public Symbol(String name) {
        this.name = name;
    }

    public static Symbol symbol(String name) {
        return new Symbol(name);
    }

    public String toString() {
        return name;
    }
}
