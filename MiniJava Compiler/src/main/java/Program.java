import parser.MiniJavaParser;
import visitor.BuildSymbolTableVisitor;
import visitor.TypeCheckVisitor;

import java.io.File;
import java.io.FileReader;
import java.io.Reader;

public class Program {

    private static BuildSymbolTableVisitor buildSymbolTableVisitor = new BuildSymbolTableVisitor();
    private static TypeCheckVisitor typeCheckVisitor = new TypeCheckVisitor();

    public static void main(String[] args) {
        try {
            File input_program = new File(args[0]);
            Reader reader = new FileReader(input_program);
            syntaxtree.Program p = new MiniJavaParser(reader).Program();
            visitSyntaxTree(p);
            reader.close();
            System.out.println("Syntax is okay");
        }
        catch (Exception e) {
            System.out.println("Syntax is not okay: " + e.getMessage());
        }
    }

    private static void visitSyntaxTree(syntaxtree.Program p) {
        p.accept(buildSymbolTableVisitor);
        System.out.println(buildSymbolTableVisitor.getSymbolTable().toString());
    }
}
