package symboltable.tables;

import symboltable.SymbolTable;
import symboltable.entries.FieldEntry;

import java.util.*;

public class FieldTable implements SymbolTable<FieldEntry> {

    private Dictionary<String, FieldEntry> table = new Hashtable<>();
    private List<FieldEntry> list = new ArrayList<>();

    @Override
    public void addEntry(String symbol, FieldEntry entry)
    {
        this.table.put(symbol, entry);
        this.list.add(entry);
    }

    @Override
    public FieldEntry searchEntry(String symbol) {
        return this.table.get(symbol);
    }

    @Override
    public List<FieldEntry> getEntries() {
        return this.list;
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
