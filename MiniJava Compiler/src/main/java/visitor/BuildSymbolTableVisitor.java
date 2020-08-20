package visitor;

import symboltable.SymbolTable;
import symboltable.entries.*;
import symboltable.tables.ClassTable;
import syntaxtree.*;

public class BuildSymbolTableVisitor extends DepthFirstVisitor {

    private ClassDecl currentClass;
    private MethodDecl currentMethod;

    private SymbolTable<ClassEntry> symbolTable = new ClassTable();

    public SymbolTable<ClassEntry> getSymbolTable() {
        return this.symbolTable;
    }

    @Override
    public void visit(ClassDeclSimple n) {
        this.currentClass = n;
        String symbol = n.i.s;
        ClassEntry entry = new ClassEntry(symbol);
        symbolTable.addEntry(symbol, entry);
        super.visit(n);
        this.currentClass = null;
    }

    @Override
    public void visit(ClassDeclExtends n) {
        this.currentClass = n;
        String symbol = n.i.s;
        ClassEntry entry = new ClassEntry(symbol);
        symbolTable.addEntry(symbol, entry);
        super.visit(n);
        this.currentClass = null;
    }

    @Override
    public void visit(VarDecl n) {
        if (this.currentClass != null) {
            ClassEntry classEntry = symbolTable.searchEntry(this.currentClass.i.s);
            String symbol = n.i.s;
            Type type = n.t;
            if (classEntry != null) {
                if (this.currentMethod == null) {
                    FieldEntry fieldEntry = new FieldEntry(symbol, type);
                    classEntry.getFieldTable().addEntry(symbol, fieldEntry);
                }
                else {
                    String methodSymbol = this.currentMethod.i.s;
                    MethodEntry methodEntry = classEntry.getMethodTable().searchEntry(methodSymbol);
                    if (methodEntry != null) {
                        LocalEntry localEntry = new LocalEntry(symbol, type);
                        methodEntry.getLocalTable().addEntry(symbol, localEntry);
                    }
                }
            }
        }
        super.visit(n);
    }

    @Override
    public void visit(MethodDecl n) {
        this.currentMethod = n;
        if (this.currentClass != null) {
           ClassEntry classEntry = symbolTable.searchEntry(this.currentClass.i.s);
           if (classEntry != null) {
               String symbol = n.i.s;
               Type returnType = n.t;
               MethodEntry methodEntry = new MethodEntry(symbol, returnType);
               classEntry.getMethodTable().addEntry(symbol, methodEntry);
           }
        }
        super.visit(n);
        this.currentMethod = null;
    }

    @Override
    public void visit(Formal n) {
        if (this.currentClass != null && this.currentMethod != null) {
            ClassEntry classEntry = symbolTable.searchEntry(this.currentClass.i.s);
            if (classEntry != null) {
                String methodSymbol = this.currentMethod.i.s;
                MethodEntry methodEntry = classEntry.getMethodTable().searchEntry(methodSymbol);
                if (methodEntry != null) {
                    String symbol = n.i.s;
                    Type type = n.t;
                    ParamEntry paramEntry = new ParamEntry(symbol, type);
                    methodEntry.getParamTable().addEntry(symbol, paramEntry);
                }
            }
        }
        super.visit(n);
    }
}
