package symboltable;

import java.util.List;

public interface SymbolTable<T extends SymbolTableEntry> {

    void addEntry(String symbol, T entry);

    T searchEntry(String symbol);

    List<T> getEntries();
}
