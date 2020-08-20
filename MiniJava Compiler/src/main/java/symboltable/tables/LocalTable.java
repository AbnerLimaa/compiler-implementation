package symboltable.tables;

import symboltable.SymbolTable;
import symboltable.entries.LocalEntry;

import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;

public class LocalTable implements SymbolTable<LocalEntry> {

    private Dictionary<String, LocalEntry> table = new Hashtable<>();

    @Override
    public void addEntry(String symbol, LocalEntry entry) {
        this.table.put(symbol, entry);
    }

    @Override
    public LocalEntry searchEntry(String symbol) {
        return this.table.get(symbol);
    }

    @Override
    public String toString() {
        StringBuilder label = new StringBuilder("\t\t\t\tLocals:");
        Enumeration<String> keys = table.keys();
        while (keys.hasMoreElements()) {
            String element = keys.nextElement();
            label.append("\n\t\t\t\t\t").append(table.get(element).toString());
        }
        return label.append("\n").toString();
    }
}
