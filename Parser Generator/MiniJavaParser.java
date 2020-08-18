/* Generated By:JavaCC: Do not edit this line. MiniJavaParser.java */
import java.io.Reader;
import syntaxtree.*;

public class MiniJavaParser implements MiniJavaParserConstants {
        public static void parse(Reader code) {
                try {
                        Program p = new MiniJavaParser(code).Program();
                        p.accept(new PrettyPrintVisitor());
                        System.out.println("Syntax is okay");
                } catch (Throwable e) {
                        System.out.println("Syntax is not okay: " + e.getMessage());
                }
        }

  static final public Program Program() throws ParseException {
    MainClass m;
    ClassDeclList cl;
    ClassDecl cd;
    Program p;
     cl = new ClassDeclList();
    m = MainClass();
    label_1:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case CLASS:
        ;
        break;
      default:
        jj_la1[0] = jj_gen;
        break label_1;
      }
      cd = ClassDecl();
        cl.addElement(cd);
    }
    jj_consume_token(0);
        p = new Program(m, cl);
        {if (true) return p;}
    throw new Error("Missing return statement in function");
  }

  static final public MainClass MainClass() throws ParseException {
    Identifier i;
    Identifier i1;
    Statement s;
    jj_consume_token(CLASS);
    i = Identifier();
    jj_consume_token(LBRACE);
    jj_consume_token(PUBLIC);
    jj_consume_token(STATIC);
    jj_consume_token(VOID);
    jj_consume_token(MAIN);
    jj_consume_token(LPAREN);
    jj_consume_token(STRING);
    jj_consume_token(LBRACKET);
    jj_consume_token(RBRACKET);
    i1 = Identifier();
    jj_consume_token(RPAREN);
    jj_consume_token(LBRACE);
    s = Statement();
    jj_consume_token(RBRACE);
    jj_consume_token(RBRACE);
     {if (true) return new MainClass(i, i1, s);}
    throw new Error("Missing return statement in function");
  }

  static final public ClassDecl ClassDecl() throws ParseException {
    ClassDecl cd;
    if (jj_2_1(3)) {
      cd = ClassDeclSimple();
                             {if (true) return cd;}
    } else {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case CLASS:
        cd = ClassDeclExtends();
                              {if (true) return cd;}
        break;
      default:
        jj_la1[1] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
    }
    throw new Error("Missing return statement in function");
  }

  static final public ClassDecl ClassDeclExtends() throws ParseException {
    Identifier i;
    Identifier j;
    VarDeclList vl;
    MethodDeclList ml;
    MethodDecl md;
    VarDecl vd;
     vl = new VarDeclList(); ml = new MethodDeclList();
    jj_consume_token(CLASS);
    i = Identifier();
    jj_consume_token(EXTENDS);
    j = Identifier();
    jj_consume_token(LBRACE);
    label_2:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case BOOLEAN:
      case INT:
      case ID:
        ;
        break;
      default:
        jj_la1[2] = jj_gen;
        break label_2;
      }
      vd = VarDecl();
           vl.addElement(vd);
    }
    label_3:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case PUBLIC:
        ;
        break;
      default:
        jj_la1[3] = jj_gen;
        break label_3;
      }
      md = MethodDecl();
          ml.addElement(md);
    }
    jj_consume_token(RBRACE);
        {if (true) return new ClassDeclExtends(i, j, vl, ml);}
    throw new Error("Missing return statement in function");
  }

  static final public ClassDecl ClassDeclSimple() throws ParseException {
    Identifier i;
    VarDeclList vl;
    MethodDeclList ml;
    MethodDecl md;
    VarDecl vd;
     vl = new VarDeclList(); ml = new MethodDeclList();
    jj_consume_token(CLASS);
    i = Identifier();
    jj_consume_token(LBRACE);
    label_4:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case BOOLEAN:
      case INT:
      case ID:
        ;
        break;
      default:
        jj_la1[4] = jj_gen;
        break label_4;
      }
      vd = VarDecl();
           vl.addElement(vd);
    }
    label_5:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case PUBLIC:
        ;
        break;
      default:
        jj_la1[5] = jj_gen;
        break label_5;
      }
      md = MethodDecl();
          ml.addElement(md);
    }
    jj_consume_token(RBRACE);
     {if (true) return new ClassDeclSimple(i, vl, ml);}
    throw new Error("Missing return statement in function");
  }

  static final public VarDecl VarDecl() throws ParseException {
    Type t;
    Identifier i;
    t = Type();
    i = Identifier();
    jj_consume_token(SEMICOLON);
                                             {if (true) return new VarDecl(t, i);}
    throw new Error("Missing return statement in function");
  }

  static final public MethodDecl MethodDecl() throws ParseException {
    Type t;
    Identifier i;
    FormalList fl;
    VarDeclList vl;
    StatementList sl;
    Exp e;

    Type tf1;
    Identifier fi;
    Type tf2;
    Identifier fi2;

    VarDecl vd;
    Statement s;
     fl = new FormalList(); vl = new VarDeclList(); sl = new StatementList();
    jj_consume_token(PUBLIC);
    t = Type();
    i = Identifier();
    jj_consume_token(LPAREN);
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case BOOLEAN:
    case INT:
    case ID:
      tf1 = Type();
      fi = Identifier();
         fl.addElement(new Formal(tf1, fi));
      label_6:
      while (true) {
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case COMMA:
          ;
          break;
        default:
          jj_la1[6] = jj_gen;
          break label_6;
        }
        jj_consume_token(COMMA);
        tf2 = Type();
        fi2 = Identifier();
          fl.addElement(new Formal(tf2, fi2));
      }
      break;
    default:
      jj_la1[7] = jj_gen;
      ;
    }
    jj_consume_token(RPAREN);
    jj_consume_token(LBRACE);
    label_7:
    while (true) {
      if (jj_2_2(2)) {
        ;
      } else {
        break label_7;
      }
      vd = VarDecl();
          vl.addElement(vd);
    }
    label_8:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case IF:
      case WHILE:
      case PRINT:
      case LBRACE:
      case ID:
        ;
        break;
      default:
        jj_la1[8] = jj_gen;
        break label_8;
      }
      s = Statement();
          sl.addElement(s);
    }
    jj_consume_token(RETURN);
    e = Expression();
    jj_consume_token(SEMICOLON);
    jj_consume_token(RBRACE);
     {if (true) return new MethodDecl(t, i, fl, vl, sl, e);}
    throw new Error("Missing return statement in function");
  }

  static final public Type Type() throws ParseException {
 Identifier id;
    if (jj_2_3(2)) {
      jj_consume_token(INT);
      jj_consume_token(LBRACKET);
      jj_consume_token(RBRACKET);
                                 {if (true) return new IntArrayType();}
    } else {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case INT:
        jj_consume_token(INT);
             {if (true) return new IntegerType();}
        break;
      case BOOLEAN:
        jj_consume_token(BOOLEAN);
                 {if (true) return new BooleanType();}
        break;
      case ID:
        id = Identifier();
                         {if (true) return new IdentifierType(id.s);}
        break;
      default:
        jj_la1[9] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
    }
    throw new Error("Missing return statement in function");
  }

  static final public Statement Statement() throws ParseException {
    Statement s;
    StatementList sl;

    Exp e;
    Statement s1;
    Statement s2;
    Exp e1;
    Identifier i;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case LBRACE:
     sl = new StatementList();
      jj_consume_token(LBRACE);
      label_9:
      while (true) {
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case IF:
        case WHILE:
        case PRINT:
        case LBRACE:
        case ID:
          ;
          break;
        default:
          jj_la1[10] = jj_gen;
          break label_9;
        }
        s = Statement();
                                   sl.addElement(s);
      }
      jj_consume_token(RBRACE);
                                                                    {if (true) return new Block(sl);}
      break;
    case IF:
      jj_consume_token(IF);
      jj_consume_token(LPAREN);
      e = Expression();
      jj_consume_token(RPAREN);
      s1 = Statement();
      jj_consume_token(ELSE);
      s2 = Statement();
                                                                                          {if (true) return new If(e, s1, s2);}
      break;
    case WHILE:
      jj_consume_token(WHILE);
      jj_consume_token(LPAREN);
      e = Expression();
      jj_consume_token(RPAREN);
      s = Statement();
                                                                    {if (true) return new While(e, s);}
      break;
    case PRINT:
      jj_consume_token(PRINT);
      jj_consume_token(LPAREN);
      e = Expression();
      jj_consume_token(RPAREN);
      jj_consume_token(SEMICOLON);
                                                                {if (true) return new Print(e);}
      break;
    default:
      jj_la1[11] = jj_gen;
      if (jj_2_4(2)) {
        i = Identifier();
        jj_consume_token(LBRACKET);
        e = Expression();
        jj_consume_token(RBRACKET);
        jj_consume_token(EQUAL);
        e1 = Expression();
        jj_consume_token(SEMICOLON);
                                                                                                                   {if (true) return new ArrayAssign(i, e, e1);}
      } else {
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case ID:
          i = Identifier();
          jj_consume_token(EQUAL);
          e = Expression();
          jj_consume_token(SEMICOLON);
                                                               {if (true) return new Assign(i, e);}
          break;
        default:
          jj_la1[12] = jj_gen;
          jj_consume_token(-1);
          throw new ParseException();
        }
      }
    }
    throw new Error("Missing return statement in function");
  }

  static final public Exp Expression() throws ParseException {
    Exp e1;
    Identifier i;
    Exp e2;
    ExpList el;
    Token t;
    Exp e;
     el = new ExpList();
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case NEW:
      jj_consume_token(NEW);
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case INT:
        jj_consume_token(INT);
        jj_consume_token(LBRACKET);
        e = Expression();
        jj_consume_token(RBRACKET);
                                                              e1 = new NewArray(e);
        break;
      case ID:
        i = Identifier();
        jj_consume_token(LPAREN);
        jj_consume_token(RPAREN);
                                                                                                                             e1 = new NewObject(i);
        break;
      default:
        jj_la1[13] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
      break;
    case INTEGER_LITERAL:
      t = jj_consume_token(INTEGER_LITERAL);
                                 e1 = new IntegerLiteral(Integer.parseInt(t.image));
      break;
    case BOOLEAN_LITERAL:
      t = jj_consume_token(BOOLEAN_LITERAL);
                                 if (Boolean.parseBoolean(t.image)) e1 = new True(); else e1 = new False();
      break;
    case ID:
      i = Identifier();
                            e1 = new IdentifierExp(i.s);
      break;
    case THIS:
      jj_consume_token(THIS);
                   e1 = new This();
      break;
    case BANG:
      jj_consume_token(BANG);
      e = Expression();
                                     e1 = new Not(e);
      break;
    case LPAREN:
      jj_consume_token(LPAREN);
      e = Expression();
      jj_consume_token(RPAREN);
                                                e1 = e;
      break;
    default:
      jj_la1[14] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
    label_10:
    while (true) {
      if (jj_2_5(2)) {
        ;
      } else {
        break label_10;
      }
      if (jj_2_6(2)) {
        jj_consume_token(PERIOD);
        jj_consume_token(LENGTH);
                                            e1 = new ArrayLength(e1);
      } else {
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case PERIOD:
          jj_consume_token(PERIOD);
          i = Identifier();
          jj_consume_token(LPAREN);
          switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
          case THIS:
          case NEW:
          case LPAREN:
          case BANG:
          case ID:
          case INTEGER_LITERAL:
          case BOOLEAN_LITERAL:
            e2 = Expression();
                                                                     el.addElement(e2);
            label_11:
            while (true) {
              switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
              case COMMA:
                ;
                break;
              default:
                jj_la1[15] = jj_gen;
                break label_11;
              }
              jj_consume_token(COMMA);
              e2 = Expression();
                                                                                                                     el.addElement(e2);
            }
            break;
          default:
            jj_la1[16] = jj_gen;
            ;
          }
          jj_consume_token(RPAREN);
                                                                                                                                                      e1 = new Call(e1, i, el);
          break;
        case LBRACKET:
          jj_consume_token(LBRACKET);
          e2 = Expression();
          jj_consume_token(RBRACKET);
                                                         e1 = new ArrayLookup(e1, e2);
          break;
        case AND:
          jj_consume_token(AND);
          e2 = Expression();
                                         e1 =  new And(e1, e2);
          break;
        case LESSTHAN:
          jj_consume_token(LESSTHAN);
          e2 = Expression();
                                              e1 =  new LessThan(e1, e2);
          break;
        case PLUS:
          jj_consume_token(PLUS);
          e2 = Expression();
                                          e1 =  new Plus(e1, e2);
          break;
        case MINUS:
          jj_consume_token(MINUS);
          e2 = Expression();
                                           e1 =  new Minus(e1, e2);
          break;
        case TIMES:
          jj_consume_token(TIMES);
          e2 = Expression();
                                           e1 =  new Times(e1, e2);
          break;
        default:
          jj_la1[17] = jj_gen;
          jj_consume_token(-1);
          throw new ParseException();
        }
      }
    }
     {if (true) return e1;}
    throw new Error("Missing return statement in function");
  }

  static final public Identifier Identifier() throws ParseException {
    Token t;
    Identifier i;
    t = jj_consume_token(ID);
     {if (true) return new Identifier(t.image);}
    throw new Error("Missing return statement in function");
  }

  static private boolean jj_2_1(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_1(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(0, xla); }
  }

  static private boolean jj_2_2(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_2(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(1, xla); }
  }

  static private boolean jj_2_3(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_3(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(2, xla); }
  }

  static private boolean jj_2_4(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_4(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(3, xla); }
  }

  static private boolean jj_2_5(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_5(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(4, xla); }
  }

  static private boolean jj_2_6(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_6(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(5, xla); }
  }

  static private boolean jj_3_2() {
    if (jj_3R_13()) return true;
    return false;
  }

  static private boolean jj_3R_14() {
    if (jj_scan_token(ID)) return true;
    return false;
  }

  static private boolean jj_3_4() {
    if (jj_3R_14()) return true;
    if (jj_scan_token(LBRACKET)) return true;
    return false;
  }

  static private boolean jj_3R_21() {
    if (jj_scan_token(TIMES)) return true;
    if (jj_3R_23()) return true;
    return false;
  }

  static private boolean jj_3R_20() {
    if (jj_scan_token(MINUS)) return true;
    if (jj_3R_23()) return true;
    return false;
  }

  static private boolean jj_3_1() {
    if (jj_3R_12()) return true;
    return false;
  }

  static private boolean jj_3R_19() {
    if (jj_scan_token(PLUS)) return true;
    if (jj_3R_23()) return true;
    return false;
  }

  static private boolean jj_3R_18() {
    if (jj_scan_token(LESSTHAN)) return true;
    if (jj_3R_23()) return true;
    return false;
  }

  static private boolean jj_3R_12() {
    if (jj_scan_token(CLASS)) return true;
    if (jj_3R_14()) return true;
    if (jj_scan_token(LBRACE)) return true;
    return false;
  }

  static private boolean jj_3R_17() {
    if (jj_scan_token(AND)) return true;
    if (jj_3R_23()) return true;
    return false;
  }

  static private boolean jj_3R_16() {
    if (jj_scan_token(LBRACKET)) return true;
    if (jj_3R_23()) return true;
    return false;
  }

  static private boolean jj_3R_15() {
    if (jj_scan_token(PERIOD)) return true;
    if (jj_3R_14()) return true;
    return false;
  }

  static private boolean jj_3_6() {
    if (jj_scan_token(PERIOD)) return true;
    if (jj_scan_token(LENGTH)) return true;
    return false;
  }

  static private boolean jj_3_5() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3_6()) {
    jj_scanpos = xsp;
    if (jj_3R_15()) {
    jj_scanpos = xsp;
    if (jj_3R_16()) {
    jj_scanpos = xsp;
    if (jj_3R_17()) {
    jj_scanpos = xsp;
    if (jj_3R_18()) {
    jj_scanpos = xsp;
    if (jj_3R_19()) {
    jj_scanpos = xsp;
    if (jj_3R_20()) {
    jj_scanpos = xsp;
    if (jj_3R_21()) return true;
    }
    }
    }
    }
    }
    }
    }
    return false;
  }

  static private boolean jj_3R_33() {
    if (jj_scan_token(LPAREN)) return true;
    return false;
  }

  static private boolean jj_3R_32() {
    if (jj_scan_token(BANG)) return true;
    return false;
  }

  static private boolean jj_3R_31() {
    if (jj_scan_token(THIS)) return true;
    return false;
  }

  static private boolean jj_3R_26() {
    if (jj_3R_14()) return true;
    return false;
  }

  static private boolean jj_3R_30() {
    if (jj_3R_14()) return true;
    return false;
  }

  static private boolean jj_3R_25() {
    if (jj_scan_token(BOOLEAN)) return true;
    return false;
  }

  static private boolean jj_3R_29() {
    if (jj_scan_token(BOOLEAN_LITERAL)) return true;
    return false;
  }

  static private boolean jj_3R_24() {
    if (jj_scan_token(INT)) return true;
    return false;
  }

  static private boolean jj_3R_28() {
    if (jj_scan_token(INTEGER_LITERAL)) return true;
    return false;
  }

  static private boolean jj_3R_27() {
    if (jj_scan_token(NEW)) return true;
    return false;
  }

  static private boolean jj_3_3() {
    if (jj_scan_token(INT)) return true;
    if (jj_scan_token(LBRACKET)) return true;
    return false;
  }

  static private boolean jj_3R_22() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3_3()) {
    jj_scanpos = xsp;
    if (jj_3R_24()) {
    jj_scanpos = xsp;
    if (jj_3R_25()) {
    jj_scanpos = xsp;
    if (jj_3R_26()) return true;
    }
    }
    }
    return false;
  }

  static private boolean jj_3R_13() {
    if (jj_3R_22()) return true;
    if (jj_3R_14()) return true;
    return false;
  }

  static private boolean jj_3R_23() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_27()) {
    jj_scanpos = xsp;
    if (jj_3R_28()) {
    jj_scanpos = xsp;
    if (jj_3R_29()) {
    jj_scanpos = xsp;
    if (jj_3R_30()) {
    jj_scanpos = xsp;
    if (jj_3R_31()) {
    jj_scanpos = xsp;
    if (jj_3R_32()) {
    jj_scanpos = xsp;
    if (jj_3R_33()) return true;
    }
    }
    }
    }
    }
    }
    return false;
  }

  static private boolean jj_initialized_once = false;
  /** Generated Token Manager. */
  static public MiniJavaParserTokenManager token_source;
  static SimpleCharStream jj_input_stream;
  /** Current token. */
  static public Token token;
  /** Next token. */
  static public Token jj_nt;
  static private int jj_ntk;
  static private Token jj_scanpos, jj_lastpos;
  static private int jj_la;
  /** Whether we are looking ahead. */
  static private boolean jj_lookingAhead = false;
  static private boolean jj_semLA;
  static private int jj_gen;
  static final private int[] jj_la1 = new int[18];
  static private int[] jj_la1_0;
  static private int[] jj_la1_1;
  static {
      jj_la1_init_0();
      jj_la1_init_1();
   }
   private static void jj_la1_init_0() {
      jj_la1_0 = new int[] {0x4,0x4,0x80000c00,0x8,0x80000c00,0x8,0x800000,0x80000c00,0x80046002,0x80000c00,0x80046002,0x46002,0x80000000,0x80000800,0x88130000,0x800000,0x88130000,0x12000000,};
   }
   private static void jj_la1_init_1() {
      jj_la1_1 = new int[] {0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x3,0x0,0x3,0x7c,};
   }
  static final private JJCalls[] jj_2_rtns = new JJCalls[6];
  static private boolean jj_rescan = false;
  static private int jj_gc = 0;

  /** Constructor with InputStream. */
  public MiniJavaParser(java.io.InputStream stream) {
     this(stream, null);
  }
  /** Constructor with InputStream and supplied encoding */
  public MiniJavaParser(java.io.InputStream stream, String encoding) {
    if (jj_initialized_once) {
      System.out.println("ERROR: Second call to constructor of static parser.  ");
      System.out.println("       You must either use ReInit() or set the JavaCC option STATIC to false");
      System.out.println("       during parser generation.");
      throw new Error();
    }
    jj_initialized_once = true;
    try { jj_input_stream = new SimpleCharStream(stream, encoding, 1, 1); } catch(java.io.UnsupportedEncodingException e) { throw new RuntimeException(e); }
    token_source = new MiniJavaParserTokenManager(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 18; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  /** Reinitialise. */
  static public void ReInit(java.io.InputStream stream) {
     ReInit(stream, null);
  }
  /** Reinitialise. */
  static public void ReInit(java.io.InputStream stream, String encoding) {
    try { jj_input_stream.ReInit(stream, encoding, 1, 1); } catch(java.io.UnsupportedEncodingException e) { throw new RuntimeException(e); }
    token_source.ReInit(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 18; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  /** Constructor. */
  public MiniJavaParser(java.io.Reader stream) {
    if (jj_initialized_once) {
      System.out.println("ERROR: Second call to constructor of static parser. ");
      System.out.println("       You must either use ReInit() or set the JavaCC option STATIC to false");
      System.out.println("       during parser generation.");
      throw new Error();
    }
    jj_initialized_once = true;
    jj_input_stream = new SimpleCharStream(stream, 1, 1);
    token_source = new MiniJavaParserTokenManager(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 18; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  /** Reinitialise. */
  static public void ReInit(java.io.Reader stream) {
    jj_input_stream.ReInit(stream, 1, 1);
    token_source.ReInit(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 18; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  /** Constructor with generated Token Manager. */
  public MiniJavaParser(MiniJavaParserTokenManager tm) {
    if (jj_initialized_once) {
      System.out.println("ERROR: Second call to constructor of static parser. ");
      System.out.println("       You must either use ReInit() or set the JavaCC option STATIC to false");
      System.out.println("       during parser generation.");
      throw new Error();
    }
    jj_initialized_once = true;
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 18; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  /** Reinitialise. */
  public void ReInit(MiniJavaParserTokenManager tm) {
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 18; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  static private Token jj_consume_token(int kind) throws ParseException {
    Token oldToken;
    if ((oldToken = token).next != null) token = token.next;
    else token = token.next = token_source.getNextToken();
    jj_ntk = -1;
    if (token.kind == kind) {
      jj_gen++;
      if (++jj_gc > 100) {
        jj_gc = 0;
        for (int i = 0; i < jj_2_rtns.length; i++) {
          JJCalls c = jj_2_rtns[i];
          while (c != null) {
            if (c.gen < jj_gen) c.first = null;
            c = c.next;
          }
        }
      }
      return token;
    }
    token = oldToken;
    jj_kind = kind;
    throw generateParseException();
  }

  static private final class LookaheadSuccess extends java.lang.Error { }
  static final private LookaheadSuccess jj_ls = new LookaheadSuccess();
  static private boolean jj_scan_token(int kind) {
    if (jj_scanpos == jj_lastpos) {
      jj_la--;
      if (jj_scanpos.next == null) {
        jj_lastpos = jj_scanpos = jj_scanpos.next = token_source.getNextToken();
      } else {
        jj_lastpos = jj_scanpos = jj_scanpos.next;
      }
    } else {
      jj_scanpos = jj_scanpos.next;
    }
    if (jj_rescan) {
      int i = 0; Token tok = token;
      while (tok != null && tok != jj_scanpos) { i++; tok = tok.next; }
      if (tok != null) jj_add_error_token(kind, i);
    }
    if (jj_scanpos.kind != kind) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) throw jj_ls;
    return false;
  }


/** Get the next Token. */
  static final public Token getNextToken() {
    if (token.next != null) token = token.next;
    else token = token.next = token_source.getNextToken();
    jj_ntk = -1;
    jj_gen++;
    return token;
  }

/** Get the specific Token. */
  static final public Token getToken(int index) {
    Token t = jj_lookingAhead ? jj_scanpos : token;
    for (int i = 0; i < index; i++) {
      if (t.next != null) t = t.next;
      else t = t.next = token_source.getNextToken();
    }
    return t;
  }

  static private int jj_ntk() {
    if ((jj_nt=token.next) == null)
      return (jj_ntk = (token.next=token_source.getNextToken()).kind);
    else
      return (jj_ntk = jj_nt.kind);
  }

  static private java.util.List jj_expentries = new java.util.ArrayList();
  static private int[] jj_expentry;
  static private int jj_kind = -1;
  static private int[] jj_lasttokens = new int[100];
  static private int jj_endpos;

  static private void jj_add_error_token(int kind, int pos) {
    if (pos >= 100) return;
    if (pos == jj_endpos + 1) {
      jj_lasttokens[jj_endpos++] = kind;
    } else if (jj_endpos != 0) {
      jj_expentry = new int[jj_endpos];
      for (int i = 0; i < jj_endpos; i++) {
        jj_expentry[i] = jj_lasttokens[i];
      }
      boolean exists = false;
      for (java.util.Iterator it = jj_expentries.iterator(); it.hasNext();) {
        int[] oldentry = (int[])(it.next());
        if (oldentry.length == jj_expentry.length) {
          exists = true;
          for (int i = 0; i < jj_expentry.length; i++) {
            if (oldentry[i] != jj_expentry[i]) {
              exists = false;
              break;
            }
          }
          if (exists) break;
        }
      }
      if (!exists) jj_expentries.add(jj_expentry);
      if (pos != 0) jj_lasttokens[(jj_endpos = pos) - 1] = kind;
    }
  }

  /** Generate ParseException. */
  static public ParseException generateParseException() {
    jj_expentries.clear();
    boolean[] la1tokens = new boolean[44];
    if (jj_kind >= 0) {
      la1tokens[jj_kind] = true;
      jj_kind = -1;
    }
    for (int i = 0; i < 18; i++) {
      if (jj_la1[i] == jj_gen) {
        for (int j = 0; j < 32; j++) {
          if ((jj_la1_0[i] & (1<<j)) != 0) {
            la1tokens[j] = true;
          }
          if ((jj_la1_1[i] & (1<<j)) != 0) {
            la1tokens[32+j] = true;
          }
        }
      }
    }
    for (int i = 0; i < 44; i++) {
      if (la1tokens[i]) {
        jj_expentry = new int[1];
        jj_expentry[0] = i;
        jj_expentries.add(jj_expentry);
      }
    }
    jj_endpos = 0;
    jj_rescan_token();
    jj_add_error_token(0, 0);
    int[][] exptokseq = new int[jj_expentries.size()][];
    for (int i = 0; i < jj_expentries.size(); i++) {
      exptokseq[i] = (int[])jj_expentries.get(i);
    }
    return new ParseException(token, exptokseq, tokenImage);
  }

  /** Enable tracing. */
  static final public void enable_tracing() {
  }

  /** Disable tracing. */
  static final public void disable_tracing() {
  }

  static private void jj_rescan_token() {
    jj_rescan = true;
    for (int i = 0; i < 6; i++) {
    try {
      JJCalls p = jj_2_rtns[i];
      do {
        if (p.gen > jj_gen) {
          jj_la = p.arg; jj_lastpos = jj_scanpos = p.first;
          switch (i) {
            case 0: jj_3_1(); break;
            case 1: jj_3_2(); break;
            case 2: jj_3_3(); break;
            case 3: jj_3_4(); break;
            case 4: jj_3_5(); break;
            case 5: jj_3_6(); break;
          }
        }
        p = p.next;
      } while (p != null);
      } catch(LookaheadSuccess ls) { }
    }
    jj_rescan = false;
  }

  static private void jj_save(int index, int xla) {
    JJCalls p = jj_2_rtns[index];
    while (p.gen > jj_gen) {
      if (p.next == null) { p = p.next = new JJCalls(); break; }
      p = p.next;
    }
    p.gen = jj_gen + xla - jj_la; p.first = token; p.arg = xla;
  }

  static final class JJCalls {
    int gen;
    Token first;
    int arg;
    JJCalls next;
  }

}
