import parser.MiniJavaParser;
import utils.symboltable.SymbolTable;
import utils.symboltable.entries.ClassEntry;
import typechecking.BuildSymbolTableVisitor;
import typechecking.TypeCheckVisitor;

import java.io.File;
import java.io.FileReader;
import java.io.Reader;

public class Program {

    public static void main(String[] args) {
        try {
            File input_program = new File(args[0]);
            Reader reader = new FileReader(input_program);
            utils.syntaxtree.Program p = new MiniJavaParser(reader).Program();
            visitSyntaxTree(p);
            reader.close();
            System.out.println("Syntax is okay");
        }
        catch (Exception e) {
            System.out.println("Syntax is not okay: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void visitSyntaxTree(utils.syntaxtree.Program p) {
        SymbolTable<ClassEntry> symbolTable = visitSymbolTable(p);
        visitTypeChecking(p, symbolTable);
    }

    private static  SymbolTable<ClassEntry> visitSymbolTable(utils.syntaxtree.Program p) {
        BuildSymbolTableVisitor buildSymbolTableVisitor = new BuildSymbolTableVisitor();
        p.accept(buildSymbolTableVisitor);
        buildSymbolTableVisitor.printSymbolTable();
        return buildSymbolTableVisitor.getSymbolTable();
    }

    private static void visitTypeChecking(utils.syntaxtree.Program p, SymbolTable<ClassEntry> symbolTable) {
        TypeCheckVisitor typeCheckVisitor = new TypeCheckVisitor(symbolTable);
        p.accept(typeCheckVisitor);
        typeCheckVisitor.printErrors();
    }
}
