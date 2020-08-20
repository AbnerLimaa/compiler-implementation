package symboltable;

public interface SymbolTable<T extends SymbolTableEntry> {

    void addEntry(String symbol, T entry);

    T searchEntry(String symbol);
}
