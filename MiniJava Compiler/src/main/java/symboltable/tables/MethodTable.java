package symboltable.tables;

import symboltable.SymbolTable;
import symboltable.entries.MethodEntry;
import symboltable.entries.ParamEntry;

import java.util.*;

public class MethodTable implements SymbolTable<MethodEntry> {

    private Dictionary<String, MethodEntry> table = new Hashtable<>();
    private List<MethodEntry> list = new ArrayList<>();

    @Override
    public void addEntry(String symbol, MethodEntry entry)
    {
        this.table.put(symbol, entry);
        this.list.add(entry);
    }

    @Override
    public MethodEntry searchEntry(String symbol) {
        return this.table.get(symbol);
    }

    @Override
    public List<MethodEntry> getEntries() {
        return this.list;
    }

    @Override
    public String toString() {
        StringBuilder label = new StringBuilder("\t\tMethods:");
        Enumeration<String> keys = table.keys();
        while (keys.hasMoreElements()) {
            String element = keys.nextElement();
            label.append("\n").append(table.get(element).toString());
        }
        return label.append("\n").toString();
    }
}
