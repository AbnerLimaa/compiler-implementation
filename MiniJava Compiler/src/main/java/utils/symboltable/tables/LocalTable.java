package utils.symboltable.tables;

import utils.symboltable.SymbolTable;
import utils.symboltable.entries.LocalEntry;

import java.util.*;

public class LocalTable implements SymbolTable<LocalEntry> {

    private Dictionary<String, LocalEntry> table = new Hashtable<>();
    private List<LocalEntry> list = new ArrayList<>();

    @Override
    public void addEntry(String symbol, LocalEntry entry) {
        this.table.put(symbol, entry);
        this.list.add(entry);
    }

    @Override
    public LocalEntry searchEntry(String symbol) {
        return this.table.get(symbol);
    }

    @Override
    public List<LocalEntry> getEntries() {
        return this.list;
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
