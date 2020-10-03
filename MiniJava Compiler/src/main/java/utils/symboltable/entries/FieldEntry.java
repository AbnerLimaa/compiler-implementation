package utils.symboltable.entries;

import utils.symboltable.SymbolTableEntry;
import utils.syntaxtree.Type;

public class FieldEntry implements SymbolTableEntry {

    private String symbol;
    private Type type;

    public FieldEntry(String symbol, Type type) {
        this.symbol = symbol;
        this.type = type;
    }

    @Override
    public String getSymbol() {
        return this.symbol;
    }

    public Type getType() {
        return this.type;
    }

    @Override
    public Boolean hasChild() {
        return false;
    }

    @Override
    public String toString() {
        return type.toString() + " " + symbol;
    }
}
