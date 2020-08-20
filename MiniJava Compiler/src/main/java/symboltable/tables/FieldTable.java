package symboltable.tables;

import symboltable.SymbolTable;
import symboltable.entries.FieldEntry;

import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;

public class FieldTable implements SymbolTable<FieldEntry> {

    private Dictionary<String, FieldEntry> table = new Hashtable<>();

    @Override
    public void addEntry(String symbol, FieldEntry entry) {
        this.table.put(symbol, entry);
    }

    @Override
    public FieldEntry searchEntry(String symbol) {
        return this.table.get(symbol);
    }

    @Override
    public String toString() {
        StringBuilder label = new StringBuilder("\t\tFields:");
        Enumeration<String> keys = table.keys();
        while (keys.hasMoreElements()) {
            String element = keys.nextElement();
            label.append("\n\t\t\t").append(table.get(element).toString());
        }
        return label.append("\n").toString();
    }
}
