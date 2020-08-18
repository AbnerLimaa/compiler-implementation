import parser.MiniJavaParser;

import java.io.File;
import java.io.FileReader;
import java.io.Reader;

public class Program {
    public static void main(String[] args) {
        try {
            File input_program = new File(args[0]);
            input_program.createNewFile();
            Reader targetReader = new FileReader(input_program);
            MiniJavaParser.parse(targetReader);
            targetReader.close();
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
