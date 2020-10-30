package irtreetranslation;

import utils.frame.Frame;
import utils.irtree.*;
import utils.irtree.abstractions.ExpList;
import utils.irtree.abstractions.*;
import utils.irtree.abstractions.Exp;
import instructionselection.mips.MipsFrame;
import utils.symboltable.SymbolTable;
import utils.symboltable.Symbol;
import utils.symboltable.entries.*;
import utils.syntaxtree.*;
import utils.temp.Label;
import utils.temp.Temp;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

public class IrTreeBuilder {

    private MipsFrame mipsFrame;
    private List<Frag> fragList;
    private Frame frame;
    private SymbolTable<ClassEntry> symbolTable;
    private HashMap<String, utils.irtree.abstractions.Exp> mapIdToMem;
    private String currClass;
    private String currMethod;

    public IrTreeBuilder(SymbolTable<ClassEntry> symbolTable) {
        this.fragList = new ArrayList<Frag>();
        this.mipsFrame = new MipsFrame();
        this.symbolTable = symbolTable; 
    }

    public MipsFrame getMipsFrame() {
        return this.mipsFrame;
    }

    public void procEntryExit(StmList stmList, MethodDecl method) {
        Stm translatedStmList = stmList != null ? createSEQNode(stmList.head, stmList.tail) : new EXP(new CONST(0));
        Exp returnExp = translateExpression(method.e);
        Stm translatedMethod = new MOVE(new TEMP(mipsFrame.RV()), new ESEQ(translatedStmList, returnExp));
        List<Stm> listOfStms = new ArrayList<Stm>();
        listOfStms.add(translatedMethod);
        frame.procEntryExit1(listOfStms);
        
        //stmList = new StmList(new LABEL(new Label(this.currClass + this.currMethod)), StmList.transformIntoStmList(listOfStms));
        stmList = StmList.transformIntoStmList(listOfStms);
        translatedMethod = createSEQNode(stmList.head, stmList.tail);
        //new Print(new PrintStream(System.out)).prStm(translatedMethod);

        fragList.add(new ProcFrag(translatedMethod, frame));
    }

    public List<Frag> getFragments() {
        return fragList;
    }

    public void buildIrTrees(Program p) {
        translateMainClass(p.m);
        for (int i = 0; i < p.cl.size(); i++) {
            if (p.cl.elementAt(i).getMethods() == null) continue;
            for (int j = 0; j < p.cl.elementAt(i).getMethods().size(); j++) {
                translate(p.cl.elementAt(i).getMethods().elementAt(j), p.cl.elementAt(i).i.s, p.cl.elementAt(i).getMethods().elementAt(j).i.s);
            }
        }
    }

    public void translateMainClass(MainClass mainClass) {
        List<Boolean> shouldParameterEscape = new ArrayList<Boolean>();
        shouldParameterEscape.add(false);
        this.frame = this.mipsFrame.newFrame(new Symbol("main"), shouldParameterEscape); //Symbol name, List<Boolean> formals
        StmList stmList = new StmList(translateStatement(mainClass.s), null);
        Stm translatedClass = createSEQNode(stmList.head, stmList.tail);
        fragList.add(new ProcFrag(translatedClass, frame));
    }

    public void translate(MethodDecl method, String currentClass, String currentMethod) {
        
        this.currClass = currentClass; this.currMethod = currentMethod;
        this.mapIdToMem = new HashMap<String, utils.irtree.abstractions.Exp>();
        List<Boolean> shouldParameterEscape = new ArrayList<Boolean>();
        int numberOfParameters = this.symbolTable.searchEntry(this.currClass).getMethodTable().searchEntry(this.currMethod).getParamTable().getEntries().size();
        for (int i = 0; i < numberOfParameters + 1; i++)
            shouldParameterEscape.add(false);
        this.frame = this.mipsFrame.newFrame(new Symbol(this.currClass + this.currMethod), shouldParameterEscape); //Symbol name, List<Boolean> formals

        for (int i = 0; i < method.vl.size(); i++) {

            utils.irtree.abstractions.Exp translatedVarDecl = frame.allocLocal(false).exp(new TEMP(frame.FP()));
            this.mapIdToMem.put(method.vl.elementAt(i).i.s, translatedVarDecl);
        }

        StmList stmList = null;
        for (int i = method.sl.size() - 1; i >=  0; i--) {
            stmList = new StmList(translateStatement(method.sl.elementAt(i)), stmList);
        }


        procEntryExit(stmList, method);
    }

    Stm createSEQNode(Stm head, StmList tail) {
        if (tail == null) 
            return head;
        return new SEQ(head, createSEQNode(tail.head, tail.tail));
    }

    public utils.irtree.abstractions.Stm translateStatement(utils.syntaxtree.Statement stm) {
        if (stm instanceof utils.syntaxtree.If) 
            return translateStatement((utils.syntaxtree.If) stm);
        else if (stm instanceof utils.syntaxtree.While)
            return translateStatement((utils.syntaxtree.While) stm);
        else if (stm instanceof utils.syntaxtree.Assign)
            return translateStatement((utils.syntaxtree.Assign) stm);
        else if (stm instanceof utils.syntaxtree.ArrayAssign)
            return translateStatement((utils.syntaxtree.ArrayAssign) stm);
        else if (stm instanceof utils.syntaxtree.Block)
            return translateStatement((utils.syntaxtree.Block) stm);
        else if (stm instanceof utils.syntaxtree.Print)
            return translateStatement((utils.syntaxtree.Print) stm);
        return null;
    }

    public Stm translateStatement(utils.syntaxtree.Print printStm) {
        List<Exp> listOfExps = new ArrayList<Exp>(Arrays.asList(translateExpression(printStm.e)));
        //System.out.println("FRAME: " + this.frame);
        return frame.externalCallStm("printint", listOfExps);
    }

    public Stm translateStatement(utils.syntaxtree.Block blockStm) {
        StmList stmList = null;
        for (int i = blockStm.sl.size() - 1; i >=  0; i--) {
            stmList = new StmList(translateStatement(blockStm.sl.elementAt(i)), stmList);
        } 
        return createSEQNode(stmList.head, stmList.tail);
    }

    public Stm translateStatement(If ifStm) {
        Label t = new Label();
        Label f = new Label();
        Label join = new Label();
        // A gramática do MiniJava permite qualquer expressão dentro de um if()
        // No caso de um operador <, faremos um CJUMP convencional
        // Mas se a expressão for outra, como uma atribuição ou uma adição
        // vamos forçar um erro. 
        Stm conditional = unCxExpression(ifStm.e, t, f);
        Stm thenStm = translateStatement(ifStm.s1);
        Stm elseStm = translateStatement(ifStm.s2);

        return new SEQ(conditional,
                new SEQ(
                    new SEQ(
                        new LABEL(t), new SEQ(thenStm, new JUMP(join))),
                    new SEQ(
                        new LABEL(f), new SEQ(elseStm, new LABEL(join)))
                    )
                );
            
    }

    public Stm translateStatement(While whileStm) {
        
        Label t = new Label();
        Label done = new Label();
        Label test = new Label();
        Stm conditional = unCxExpression(whileStm.e, t, done);
        Stm thenStm = translateStatement(whileStm.s);

        return new SEQ(new LABEL(test),
                new SEQ(conditional,
                    new SEQ(
                        new SEQ(
                            new LABEL(t),
                            new SEQ(
                                thenStm, new JUMP(test))
                            ), 
                        new LABEL(done))
                    ));
                     
    }

    public Stm translateStatement(Assign assignStm) {
        Exp identifierMemLocation = getMemLocation(assignStm.i.s);
        Exp exp1 = translateExpression(assignStm.e);

        return new MOVE(identifierMemLocation, exp1);
    }

    public Stm translateStatement(ArrayAssign arrayAssign) {
       Exp leftSide = translateExpression(new utils.syntaxtree.ArrayLookup(new IdentifierExp(arrayAssign.i.s), arrayAssign.e1));
       Exp rightSide = translateExpression(arrayAssign.e2);

       return new MOVE(leftSide, rightSide);
    }

    public Exp translateExpression(utils.syntaxtree.Exp exp) {
        if (exp instanceof utils.syntaxtree.Plus) 
           return translateExpression((utils.syntaxtree.Plus) exp);
        else if (exp instanceof utils.syntaxtree.Minus)
            return translateExpression((utils.syntaxtree.Minus) exp);
        else if (exp instanceof utils.syntaxtree.Times)
            return translateExpression((utils.syntaxtree.Times) exp);
        else if (exp instanceof utils.syntaxtree.LessThan)
            return translateExpression((utils.syntaxtree.LessThan) exp);
        else if (exp instanceof utils.syntaxtree.And)
            return translateExpression((utils.syntaxtree.And) exp);
        else if (exp instanceof utils.syntaxtree.Not)
            return translateExpression((utils.syntaxtree.Not) exp);
        else if (exp instanceof utils.syntaxtree.ArrayLookup)
            return translateExpression((utils.syntaxtree.ArrayLookup) exp);
        else if (exp instanceof utils.syntaxtree.Call)
            return translateExpression((utils.syntaxtree.Call) exp);
        else if (exp instanceof utils.syntaxtree.NewArray)
            return translateExpression((utils.syntaxtree.NewArray) exp);
        else if (exp instanceof utils.syntaxtree.NewObject)
            return translateExpression((utils.syntaxtree.NewObject) exp);
        else if (exp instanceof utils.syntaxtree.IntegerLiteral)
            return translateExpression((utils.syntaxtree.IntegerLiteral) exp);
        else if (exp instanceof utils.syntaxtree.IdentifierExp)
            return translateExpression((utils.syntaxtree.IdentifierExp) exp);
        else if (exp instanceof utils.syntaxtree.True)
            return translateExpression((utils.syntaxtree.True) exp);
        else if (exp instanceof utils.syntaxtree.False)
            return translateExpression((utils.syntaxtree.False) exp);
        else if (exp instanceof utils.syntaxtree.This)
            return translateExpression((utils.syntaxtree.This) exp);
        else if (exp instanceof utils.syntaxtree.ArrayLength)
            return translateExpression((utils.syntaxtree.ArrayLength) exp);
        return null;
    }
    
    

    public Exp translateExpression(utils.syntaxtree.ArrayLength arrayLength) {
        return getMemLocation( ( (utils.syntaxtree.IdentifierExp) arrayLength.e).s);
    }

    public Exp translateExpression(utils.syntaxtree.This thisKeyword) {
        return getMemLocation("this");
    }

    public Exp translateExpression(utils.syntaxtree.Plus plusExp) {
        
        Exp exp1 = translateExpression(plusExp.e1);
        Exp exp2 = translateExpression(plusExp.e2);

        return new BINOP(BINOP.PLUS, exp1, exp2);

    }

    public Exp translateExpression(utils.syntaxtree.Minus minusExp) {

        Exp exp1 = translateExpression(minusExp.e1);
        Exp exp2 = translateExpression(minusExp.e2);

        return new BINOP(BINOP.MINUS, exp1, exp2);

    }

    public Exp translateExpression(Times timesExp) {

        Exp exp1 = translateExpression(timesExp.e1);
        Exp exp2 = translateExpression(timesExp.e2);

        return new BINOP(BINOP.MUL, exp1, exp2);

    }
    
    public Exp translateExpression(LessThan lessThan) {
       Label t = new Label();
       Label f = new Label();
       Stm conditional = unCxExpression(lessThan, t, f);

       Temp r = new Temp();
       Label join = new Label();
       return new ESEQ(new SEQ(new SEQ(conditional,
                       new SEQ(new SEQ(new LABEL(t),
                               new SEQ(new MOVE(new TEMP(r), new CONST(1)),
                                   new JUMP(join))),
                               new SEQ(new LABEL(f),
                                   new SEQ(new MOVE(new TEMP(r), new CONST(0)),
                                       new JUMP(join))))),
                       new LABEL(join)),
                   new TEMP(r));

    }

    public utils.irtree.abstractions.Stm unCxExpression(utils.syntaxtree.Exp exp, Label t, Label f) {
        if (exp instanceof utils.syntaxtree.LessThan)
            return unCxExpression((utils.syntaxtree.LessThan) exp, t, f);
        else if (exp instanceof utils.syntaxtree.And)
            return unCxExpression((utils.syntaxtree.And) exp, t, f);
        else
            return new CJUMP(CJUMP.GT, new CONST(0), translateExpression(exp), t, f);
    }

    public Stm unCxExpression(LessThan lessThan, Label t, Label f) {
        return translateStatement(lessThan, t, f); 
    }

    public Exp translateExpression(utils.syntaxtree.True t) {
        return new CONST(1);
    }

    public Exp translateExpression(False f) {
        return new CONST(0);
    }

    public Exp translateExpression(And and) {

       Label t = new Label();
       Label f = new Label();
       Stm conditional = unCxExpression(and, t, f);

       Temp r = new Temp();
       Label join = new Label();
       return new ESEQ(new SEQ(new SEQ(conditional,
                       new SEQ(new SEQ(new LABEL(t),
                               new SEQ(new MOVE(new TEMP(r), new CONST(1)),
                                   new JUMP(join))),
                               new SEQ(new LABEL(f),
                                   new SEQ(new MOVE(new TEMP(r), new CONST(0)),
                                       new JUMP(join))))),
                       new LABEL(join)),
                   new TEMP(r));
    }

    public Stm unCxExpression(And and, Label t, Label f) {
        return translateStatement(and, t, f); 
    }


    public Stm translateStatement(LessThan lessThan, Label t, Label f) {
        Exp exp1 = translateExpression(lessThan.e1);
        Exp exp2 = translateExpression(lessThan.e2);

        return new CJUMP(CJUMP.LT, exp1, exp2, t, f);
    }

    public Stm translateStatement(And and, Label t, Label f) {
       
        Label z = new Label();
        Stm conditional = unCxExpression(and.e1, z, f);
        Stm conditional2 = unCxExpression(and.e2, t, f);

        return new SEQ(conditional,
                new SEQ(
                    new SEQ(
                        new LABEL(z), conditional2),
                        new LABEL(f)
                    )
                );

    }

    private Exp getMemLocation(String id) {
         //Este método deve, dado identificador de uma variável, retornar o NÓ MEM que especifica  a posição na memória
         //* ou nos registradores em que a variável se encontra .
         //* Uma varável pode ser uma variável local a um método, um parâmetro de um método ou um atributo do objeto 
         //* a que o método pertence. Para isso, vamos usar a tabela de símbolos. Primeiro vamos procurar a variável entre os atributos da classe.
         //* Se ela lá estiver, pegamos o ponteiro this passado como argumento ao método, e selecionamos essa variável. Para selecionar a variável, 
         //* acessamos a região da heap em que os atributos do objeto foram alocados, verificamos a posição p da variável na tabela de símbolos da classe,
         //* e retornamos o valor que está no endereço base + p * wordSize. Se a variável não estiver listada entre os atributos da classe, procuramos entre
         //* os argumentos. Se ela estiver lá, verificamos a posição em que ela aparece entre os argumentos, acessamos a lista formals do frame para obter o Access
         //* correspondente e chamamos o método exp para obter o nó MEM. Se ela não estiver entre os argumentos, procuramos entre as variáveis locais ao método. Se ela estiver
         //* lá, usamos a estrtura mapIdToMem para retornar o nó MEM gerado no momento da alocação. 
        //return this.mapIdToMem[id]; estrutura de dados que guarda associação entre id e endereços de memória alocados pelo nó MEM
        if (id == "this") 
            return this.frame.formals.get(0).exp(new TEMP(this.frame.FP()));
        
        utils.irtree.abstractions.Exp memLocation = this.mapIdToMem.get(id);
        if (memLocation != null)
            return memLocation; 

        SymbolTable<ParamEntry> currentParamTable = this.symbolTable.searchEntry(this.currClass).getMethodTable().searchEntry(this.currMethod).getParamTable();
        ParamEntry possibleParameterVariable = currentParamTable.searchEntry(id);
        if (possibleParameterVariable != null) {
            int parameterPosition = currentParamTable.getEntries().indexOf(possibleParameterVariable);
            return this.frame.formals.get(parameterPosition + 1).exp(new TEMP(this.frame.FP())); 
        }

        SymbolTable<FieldEntry> currentFieldTable = this.symbolTable.searchEntry(this.currClass).getFieldTable();
        FieldEntry possibleFieldVariable = currentFieldTable.searchEntry(id);

        if (possibleFieldVariable != null) {
            int offsetFromObjectBaseAddr = currentFieldTable.getEntries().indexOf(possibleFieldVariable);
            utils.irtree.abstractions.Exp baseAddress = this.frame.formals.get(0).exp(new TEMP(this.frame.FP()));
            int frameWordSize = this.frame.wordSize();

            return new MEM(new BINOP(BINOP.PLUS, baseAddress, new BINOP(BINOP.MUL, new CONST(offsetFromObjectBaseAddr), new CONST(frameWordSize))));
        }


        return new MEM(new CONST(0));

    }

    public Exp translateExpression(Identifier id) {
        
        return getMemLocation(id.s);

    }

    public Exp translateExpression(utils.syntaxtree.IdentifierExp id) {
        return getMemLocation(id.s);
    }

    public Exp translateExpression(IntegerLiteral i) {
        return new CONST(i.i);
    }

    public Exp translateExpression(Not notExp) {
        Exp notExp1 = translateExpression(notExp.e);

        return new BINOP(BINOP.XOR, notExp1, new CONST(1));
    }

    public Exp translateExpression(ArrayLookup array) {
        int wordSize = this.mipsFrame.wordSize();
        IdentifierExp idExp = (IdentifierExp) array.e1;
        Exp idMemLocation = getMemLocation(idExp.s); // se não for um Identifier, compilador quebra
        Exp exp2 = translateExpression(array.e2);

        return new MEM(
                new BINOP(
                    BINOP.PLUS, idMemLocation,
                    new BINOP(BINOP.MUL, exp2, new CONST(wordSize))
                    )
                );
    }

   private String getObjectClassName(String objectName) {
       if (objectName == "this")
           return this.currClass;

       ClassEntry currentClassEntry = this.symbolTable.searchEntry(this.currClass);
       MethodEntry currentMethodEntry = currentClassEntry.getMethodTable().searchEntry(this.currMethod);
       
       SymbolTable<LocalEntry> methodLocalVariablesTable = currentMethodEntry.getLocalTable();
       if (methodLocalVariablesTable.searchEntry(objectName) != null)
           return methodLocalVariablesTable.searchEntry(objectName).getType().toString();

       SymbolTable<ParamEntry> methodParamVariablesTable = currentMethodEntry.getParamTable();
       if (methodParamVariablesTable.searchEntry(objectName) != null)
           return methodParamVariablesTable.searchEntry(objectName).getType().toString();

       SymbolTable<FieldEntry> classFieldTable = currentClassEntry.getFieldTable();
       if (classFieldTable != null)
           return classFieldTable.searchEntry(objectName).getType().toString();

       return "Error";
   }

   public Exp translateExpression(Call call) {
       String objectName;
       if (call.e instanceof IdentifierExp) {
           IdentifierExp objectNameIdExp = (IdentifierExp) call.e;
           objectName = objectNameIdExp.s;
       } else if (call.e instanceof NewObject) { objectName = ((NewObject) (call.e)).i.s; } else { objectName = "this"; }

       String objectClassName = call.e instanceof NewObject ? objectName : getObjectClassName(objectName); // Dado o nome de um objeto, retorne o nome da classe a que ele pertence. Use a tabela de símbolos para isso. Mais especificamente, olhe nos parâmetros, variáveis locais do método atual ou olhe na classe atual.

       Label methodCallName = new Label(objectClassName + call.i.s);

       List<Exp> listOfExps = new ArrayList<Exp>();
       listOfExps.add(translateExpression(call.e));
       for (int i = 0; i < call.el.size(); i++) {
           listOfExps.add(translateExpression(call.el.elementAt(i)));
       }
       ExpList irTreeExpList = ExpList.createExpList(listOfExps); // Ao transforma utils.syntaxtree.ExpList em uma IRTree.ExpList, colocamos o objeto atual na cabeça da lista.

       return new CALL(new NAME(methodCallName),
                                irTreeExpList);

    }

   public Exp translateExpression(NewArray newArray) {
        
       int wordSize = this.mipsFrame.wordSize();
       Exp arrayNumberElements = translateExpression(newArray.e);
       Exp arraySize = new BINOP(BINOP.PLUS, new BINOP(BINOP.MUL, new CONST(wordSize), arrayNumberElements), new CONST(1)); //Precisaremos mudar runtime.c para incluir o tamnho do array na primeira posição
       List<Exp> listOfExps = new ArrayList<Exp>(Arrays.asList(arraySize, new CONST(0)));

       return frame.externalCall("initArray", listOfExps);

   }

   private int getObjectNumberAttributes(Identifier i) {
       return this.symbolTable.searchEntry(i.s).getFieldTable().getEntries().size();
   }

   public Exp translateExpression(NewObject newObject) {
       
       int wordSize = this.mipsFrame.wordSize();
       int objectNumberAttributes = getObjectNumberAttributes(newObject.i);
       Exp objectSize = new BINOP(BINOP.MUL, new CONST(wordSize), new CONST(objectNumberAttributes + 1));
       List<Exp> listOfExps = new ArrayList<Exp>(Arrays.asList(objectSize, new CONST(0)));
       return frame.externalCall("initRecord", listOfExps);

   }
}
