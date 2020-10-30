import instructionselection.mips.MipsFrame;
import parser.MiniJavaParser;
import irtreetranslation.ProcFrag;
import registerallocation.RegAlloc;
import utils.assem.Instr;
import utils.assem.OPER;
import utils.frame.Frame;
import utils.symboltable.SymbolTable;
import utils.symboltable.entries.ClassEntry;
import utils.irtree.abstractions.StmList;
import typechecking.BuildSymbolTableVisitor;
import typechecking.TypeCheckVisitor;
import irtreetranslation.IrTreeBuilder;
import irtreetranslation.Frag;
import utils.canon.Canon;
import utils.canon.BasicBlocks;
import utils.canon.TraceSchedule;
import registerallocation.livenessanalysis.AssemFlowGraph;
import registerallocation.livenessanalysis.Liveness;
import utils.temp.CombineMap;
import utils.temp.DefaultMap;
import utils.temp.Temp;
import utils.temp.TempList;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Program {

    private static String originalFileName;

    public static void main(String[] args) {
        try {
            originalFileName = args[0].replace(".java", "");
            File input_program = new File(args[0]);
            Reader reader = new FileReader(input_program);
            utils.syntaxtree.Program p = new MiniJavaParser(reader).Program();
            SymbolTable<ClassEntry> symbolTable = visitSyntaxTree(p);
            List<Frag> fragments = buildIrTrees(p, symbolTable);
            List<TraceSchedule> traceSchedules = buildCanonicalTrees(fragments);
            List<String> code = generateCode(traceSchedules, fragments);
            createOutputFile(code);
            reader.close();
            System.out.println("Compilation Success");
        }
        catch (Exception e) {
            System.out.println("Compilation Fail: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static SymbolTable<ClassEntry> visitSyntaxTree(utils.syntaxtree.Program p) {
        BuildSymbolTableVisitor buildSymbolTableVisitor = new BuildSymbolTableVisitor();
        p.accept(buildSymbolTableVisitor);
        SymbolTable<ClassEntry> symbolTable = buildSymbolTableVisitor.getSymbolTable();
        TypeCheckVisitor typeCheckVisitor = new TypeCheckVisitor(symbolTable);
        p.accept(typeCheckVisitor);
        return symbolTable;
    }

    private static List<Frag> buildIrTrees(utils.syntaxtree.Program p, SymbolTable<ClassEntry> symbolTable) {
        IrTreeBuilder irTreeBuilder = new IrTreeBuilder(symbolTable);
        irTreeBuilder.buildIrTrees(p);
        return irTreeBuilder.getFragments();
    }

    private static List<TraceSchedule> buildCanonicalTrees(List<Frag> fragments) {
        List<TraceSchedule> list = new ArrayList<>();
        for (Frag frag : fragments) {
            StmList linearizedTree = Canon.linearize(frag.getBody());
            BasicBlocks basicBlocks = new BasicBlocks(linearizedTree);
            list.add(new TraceSchedule(basicBlocks));
        }
        return list;
    }

    private static List<String> generateCode(List<TraceSchedule> traceSchedules, List<Frag> fragments) {
        int i = 0;
        List<String> code = new ArrayList<>();
        for (TraceSchedule traceSchedule : traceSchedules) {
            Frame frame = ((ProcFrag) fragments.get(i)).frame;
            List<Instr> instrList = frame.codegen(StmList.transformIntoJavaList(traceSchedule.stms));
            RegAlloc regAlloc = new RegAlloc(frame, instrList);
            regAlloc.main();
            code.addAll(regAlloc.generateCode());
            if (i == 0) {
                code.add("    li $v0, 10       \n" +
                         "    syscall          \n");
            }
            i++;
        }
        return code;
    }

    private static void createOutputFile(List<String> code) throws IOException {
        if (code != null) {
            Frame frame = new MipsFrame();
            List<String> outputCode = new ArrayList<>();
            outputCode.add("    .globl main");
            outputCode.addAll(code);
            outputCode.add(frame.programTail());
            Path file = Paths.get(originalFileName + ".asm");
            Files.write(file, outputCode, StandardCharsets.UTF_8);
        }
    }
}
