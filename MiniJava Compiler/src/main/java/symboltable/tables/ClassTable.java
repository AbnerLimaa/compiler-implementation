package symboltable.tables;

import symboltable.SymbolTable;
import symboltable.entries.ClassEntry;

import java.util.*;

public class ClassTable implements SymbolTable<ClassEntry> {

    private Dictionary<String, ClassEntry> table = new Hashtable<>();
    private List<ClassEntry> list = new ArrayList<>();

    @Override
    public void addEntry(String symbol, ClassEntry entry)
    {
        this.table.put(symbol, entry);
        this.list.add(entry);
    }

    @Override
    public ClassEntry searchEntry(String symbol) {
        return table.get(symbol);
    }

    @Override
    public List<ClassEntry> getEntries() {
        return this.list;
    }

    @Override
    public String toString() {
        StringBuilder label = new StringBuilder("Classes:");
        Enumeration<String> keys = table.keys();
        while (keys.hasMoreElements()) {
            String element = keys.nextElement();
            label.append("\n").append(table.get(element).toString());
        }
        return label.append("\n").toString();
    }
}
