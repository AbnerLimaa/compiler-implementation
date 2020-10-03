package utils.symboltable.tables;

import utils.symboltable.SymbolTable;
import utils.symboltable.entries.ParamEntry;

import java.util.*;

public class ParamTable implements SymbolTable<ParamEntry> {

    private Dictionary<String, ParamEntry> table = new Hashtable<>();
    private List<ParamEntry> list = new ArrayList<>();

    @Override
    public void addEntry(String symbol, ParamEntry entry)
    {
        this.table.put(symbol, entry);
        this.list.add(entry);
    }

    @Override
    public ParamEntry searchEntry(String symbol) {
        return this.table.get(symbol);
    }

    @Override
    public List<ParamEntry> getEntries() {
        return this.list;
    }

    @Override
    public String toString() {
        StringBuilder label = new StringBuilder("\t\t\t\tParams:");
        Enumeration<String> keys = table.keys();
        while (keys.hasMoreElements()) {
            String element = keys.nextElement();
            label.append("\n\t\t\t\t\t").append(table.get(element).toString());
        }
        return label.append("\n").toString();
    }
}
