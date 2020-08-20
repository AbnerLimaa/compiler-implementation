package symboltable.entries;

import symboltable.SymbolTable;
import symboltable.SymbolTableEntry;
import symboltable.tables.FieldTable;
import symboltable.tables.MethodTable;

public class ClassEntry implements SymbolTableEntry {

    private String symbol;
    private SymbolTable<FieldEntry> fieldTable = new FieldTable();
    private SymbolTable<MethodEntry> methodTable = new MethodTable();

    public ClassEntry(String symbol) {
        this.symbol = symbol;
    }

    @Override
    public String getSymbol() {
        return this.symbol;
    }

    public SymbolTable<FieldEntry> getFieldTable() {
        return this.fieldTable;
    }

    public SymbolTable<MethodEntry> getMethodTable() {
        return this.methodTable;
    }

    @Override
    public Boolean hasChild() {
        return true;
    }

    @Override
    public String toString() {
        StringBuilder label = new StringBuilder("\t" + symbol + "\n");
        label.append(fieldTable.toString());
        label.append(methodTable.toString());
        return label.toString();
    }
}
