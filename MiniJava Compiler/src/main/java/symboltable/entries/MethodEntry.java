package symboltable.entries;

import symboltable.SymbolTable;
import symboltable.SymbolTableEntry;
import symboltable.tables.LocalTable;
import symboltable.tables.ParamTable;
import syntaxtree.Type;

public class MethodEntry implements SymbolTableEntry {

    private String symbol;
    private Type returnType;
    private SymbolTable<ParamEntry> paramTable = new ParamTable();
    private SymbolTable<LocalEntry> localTable = new LocalTable();

    public MethodEntry(String symbol, Type returnType) {
        this.symbol = symbol;
        this.returnType = returnType;
    }

    @Override
    public String getSymbol() {
        return this.symbol;
    }

    public SymbolTable<ParamEntry> getParamTable() {
        return this.paramTable;
    }

    public SymbolTable<LocalEntry> getLocalTable() {
        return this.localTable;
    }

    public Type getReturnType() {
        return this.returnType;
    }

    @Override
    public Boolean hasChild() {
        return true;
    }

    @Override
    public String toString() {
        StringBuilder label = new StringBuilder("\t\t\t" + returnType.toString() + " " + symbol + "\n");
        label.append(paramTable.toString());
        label.append(localTable.toString());
        return label.toString();
    }
}
