package symboltable.tables;

import symboltable.SymbolTable;
import symboltable.entries.ClassEntry;

import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;

public class ClassTable implements SymbolTable<ClassEntry> {

    private Dictionary<String, ClassEntry> table = new Hashtable<>();

    @Override
    public void addEntry(String symbol, ClassEntry entry) {
        table.put(symbol, entry);
    }

    @Override
    public ClassEntry searchEntry(String symbol) {
        return table.get(symbol);
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
