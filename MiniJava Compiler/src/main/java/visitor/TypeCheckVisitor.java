package visitor;

import symboltable.SymbolTable;
import symboltable.entries.*;
import syntaxtree.*;

import java.util.ArrayList;
import java.util.List;

public class TypeCheckVisitor extends TypeDepthFirstVisitor {

    private ClassDecl currentClass;
    private MethodDecl currentMethod;
    private Boolean searchOnCurrentClass = true;
    private String lastClassIdentifierName;

    private SymbolTable<ClassEntry> symbolTable;
    private List<String> errors = new ArrayList<>();

    public TypeCheckVisitor(SymbolTable<ClassEntry> symbolTable) {
        this.symbolTable = symbolTable;
    }

    public void printErrors() {
        System.out.println("TYPE CHECKING ERRORS:");
        if (errors.isEmpty())
            System.out.println("No error found");
        else {
            for(String message : errors) {
                System.out.println(message);
            }
        }
    }

    private Type getMethodType(String s) {
        String searchClass = lastClassIdentifierName;
        if (searchOnCurrentClass && this.currentClass != null)
            searchClass = this.currentClass.i.s;
        if (searchClass != null) {
            ClassEntry classEntry = symbolTable.searchEntry(searchClass);
            if (classEntry != null) {
                MethodEntry methodEntry = classEntry.getMethodTable().searchEntry(s);
                if (methodEntry != null) {
                    //System.out.println("Method id match");
                    return methodEntry.getReturnType();
                }
            }
        }
        return null;
    }

    private Type getVarType(String s) {
        if (this.currentClass != null) {
            ClassEntry classEntry = symbolTable.searchEntry(this.currentClass.i.s);
            if (classEntry != null) {
                FieldEntry fieldEntry = classEntry.getFieldTable().searchEntry(s);
                if (this.currentMethod != null) {
                    MethodEntry methodEntry = classEntry.getMethodTable().searchEntry(this.currentMethod.i.s);
                    if (methodEntry != null) {
                        LocalEntry localEntry = methodEntry.getLocalTable().searchEntry(s);
                        ParamEntry paramEntry = methodEntry.getParamTable().searchEntry(s);
                        if (localEntry != null) {
                            //System.out.println("Local id match");
                            return localEntry.getType();
                        }
                        else if (paramEntry != null) {
                            //System.out.println("Param id match");
                            return paramEntry.getType();
                        }
                        else if (fieldEntry != null) {
                            //System.out.println("Field id match");
                            return fieldEntry.getType();
                        }
                    }
                }
                else if (fieldEntry != null) {
                    //System.out.println("Field id match");
                    return fieldEntry.getType();
                }
            }
        }
        return null;
    }

    private Boolean expResultsBoolean(Exp e) {
        if (e instanceof True || e instanceof False) {
            //System.out.println("True or False");
            return true;
        }
        if (e instanceof IdentifierExp) {
            //System.out.println("Identifier");
            IdentifierExp i = (IdentifierExp)e;
            Type iType = getVarType(i.s);
            Boolean result = iType instanceof BooleanType;
            //System.out.println("Result: " + result);
            return result;
        }
        if (e instanceof Call) {
            Identifier i = ((Call)e).i;
            if (i != null) {
                Type iType = getMethodType(i.s);
                return iType instanceof BooleanType;
            }
        }
        if (e instanceof And) {
            //System.out.println("And");
            Exp e1 = ((And)e).e1;
            Exp e2 = ((And)e).e2;
            return expResultsBoolean(e1) && expResultsBoolean(e2);
        }
        if (e instanceof LessThan) {
            //System.out.println("Less Than");
            Exp e1 = ((LessThan)e).e1;
            Exp e2 = ((LessThan)e).e2;
            return expResultsInteger(e1) && expResultsInteger(e2);
        }
        if (e instanceof Not) {
            //System.out.println("Not");
            Exp e1 = ((Not)e).e;
            return expResultsBoolean(e1);
        }
        return false;
    }

    private Boolean expResultsInteger(Exp e) {
        if (e instanceof IntegerLiteral)
            return true;
        if (e instanceof IdentifierExp) {
            IdentifierExp iExp = (IdentifierExp)e;
            Type iType = getVarType(iExp.s);
            return iType instanceof IntegerType;
        }
        if (e instanceof Call) {
            Identifier i = ((Call)e).i;
            if (i != null) {
                Type iType = getMethodType(i.s);
                return iType instanceof IntegerType;
            }
        }
        if (e instanceof Plus) {
            Exp e1 = ((Plus)e).e1;
            Exp e2 = ((Plus)e).e2;
            return expResultsInteger(e1) && expResultsInteger(e2);
        }
        if (e instanceof Minus) {
            Exp e1 = ((Minus)e).e1;
            Exp e2 = ((Minus)e).e2;
            return expResultsInteger(e1) && expResultsInteger(e2);
        }
        if (e instanceof Times) {
            Exp e1 = ((Times)e).e1;
            Exp e2 = ((Times)e).e2;
            return expResultsInteger(e1) && expResultsInteger(e2);
        }
        return false;
    }

    private Boolean expResultsIntArray(Exp e) {
        if (e instanceof NewArray)
            return true;
        if (e instanceof IdentifierExp) {
            IdentifierExp iExp = (IdentifierExp)e;
            Type iType = getVarType(iExp.s);
            return iType instanceof IntArrayType;
        }
        if (e instanceof Call) {
            Identifier i = ((Call)e).i;
            if (i != null) {
                Type iType = getMethodType(i.s);
                return iType instanceof IntArrayType;
            }
        }
        return false;
    }

    private Boolean expResultsIdentifier(Exp e, String s) {
        if (e instanceof IdentifierExp) {
            IdentifierExp iExp = (IdentifierExp)e;
            Type iType = getVarType(iExp.s);
            if (iType instanceof IdentifierType) {
                IdentifierType i = (IdentifierType)iType;
                if (i.s != null)
                    return i.s.equals(s);
            }
        }
        if (e instanceof Call) {
            Identifier i = ((Call)e).i;
            if (i != null) {
                Type type = getMethodType(i.s);
                if (type instanceof IdentifierType) {
                    IdentifierType iType = (IdentifierType)type;
                    if (iType.s != null)
                        return iType.s.equals(s);
                }
            }
        }
        return false;
    }

    private Boolean isSameType(Exp e, Type t) {
        if (t instanceof BooleanType && expResultsBoolean(e))
            return true;
        if (t instanceof IntegerType && expResultsInteger(e))
            return true;
        if (t instanceof IntArrayType && expResultsIntArray(e))
            return true;
        if (t instanceof IdentifierType) {
            IdentifierType type = (IdentifierType)t;
            if (type.s != null) {
                return expResultsIdentifier(e, type.s);
            }
        }
        return false;
    }

    @Override
    public Type visit(ClassDeclSimple n) {
        this.currentClass = n;
        Type result = super.visit(n);
        this.currentClass = null;
        return result;
    }

    @Override
    public Type visit(ClassDeclExtends n) {
        this.currentClass = n;
        Type result = super.visit(n);
        this.currentClass = null;
        return result;
    }

    @Override
    public Type visit(MethodDecl n) {
        this.currentMethod = n;
        Type result = super.visit(n);
        this.currentMethod = null;
        return result;
    }

    @Override
    public Type visit(If n) {
        if (n != null && n.e != null)
            if (!expResultsBoolean(n.e))
                errors.add("Trying to assign an expression that is not boolean to an if statement");
        return super.visit(n);
    }

    @Override
    public Type visit(While n) {
        if (n != null && n.e != null)
            if (!expResultsBoolean(n.e))
                errors.add("Trying to assign an expression that is not boolean to an while statement");
        return super.visit(n);
    }

    @Override
    public Type visit(Assign n) {
        if (n != null && n.e != null && n.i != null && n.i.s != null) {
            Type iType = getVarType(n.i.s);
            if (!isSameType(n.e, iType))
                errors.add("Trying assign value with type different than identifier type");
        }
        return super.visit(n);
    }

    @Override
    public Type visit(ArrayAssign n) {
        //System.out.println("ArrayAssign: " + n.i.s);
        return super.visit(n);
    }

    @Override
    public Type visit(And n) {
        if (n != null && n.e1 != null && n.e2 != null) {
            Exp e1 = n.e1;
            Exp e2 = n.e2;
            if (!(expResultsBoolean(e1) && expResultsBoolean(e2)))
                errors.add("Trying to conjugate expressions that are not booleans");
        }
        return super.visit(n);
    }

    @Override
    public Type visit(LessThan n) {
        if (n != null && n.e1 != null && n.e2 != null) {
            Exp e1 = n.e1;
            Exp e2 = n.e2;
            if (!(expResultsInteger(e1) && expResultsInteger(e2)))
                errors.add("Trying to compare expressions that are not integers");
        }
        return super.visit(n);
    }

    @Override
    public Type visit(Plus n) {
        if (n != null && n.e1 != null && n.e2 != null) {
            Exp e1 = n.e1;
            Exp e2 = n.e2;
            if (!(expResultsInteger(e1) && expResultsInteger(e2)))
                errors.add("Trying to sum expressions that are not integers");
        }
        return super.visit(n);
    }

    @Override
    public Type visit(Minus n) {
        if (n != null && n.e1 != null && n.e2 != null) {
            Exp e1 = n.e1;
            Exp e2 = n.e2;
            if (!(expResultsInteger(e1) && expResultsInteger(e2)))
                errors.add("Trying to minus expressions that are not integers");
        }
        return super.visit(n);
    }

    @Override
    public Type visit(Times n) {
        if (n != null && n.e1 != null && n.e2 != null) {
            Exp e1 = n.e1;
            Exp e2 = n.e2;
            if (!(expResultsInteger(e1) && expResultsInteger(e2)))
                errors.add("Trying to times expressions that are not integers");
        }
        return super.visit(n);
    }

    @Override
    public Type visit(Call n) {
        if (n != null && n.e != null && n.i != null && n.i.s != null) {
            Type iType = getMethodType(n.i.s);
            if (!isSameType(n.e, iType))
                errors.add("Trying to return value that has not the same type as the function declaration");
        }
        return super.visit(n);
    }

    @Override
    public Type visit(This n) {
        searchOnCurrentClass = true;
        return super.visit(n);
    }

    @Override
    public Type visit(IdentifierExp n) {
        //System.out.println("IdentifierExp: " + n.s);
        return super.visit(n);
    }

    @Override
    public Type visit(Not n) {
        if (n != null && n.e != null) {
            if (!expResultsBoolean(n.e))
                errors.add("Trying to negate an expression that is not boolean");
        }
        return super.visit(n);
    }

    @Override
    public Type visit(Identifier n) {
        if (n != null && n.s != null) {
            Type iType = getVarType(n.s);
            if (iType instanceof IdentifierType) {
                IdentifierType i = (IdentifierType)iType;
                searchOnCurrentClass = false;
                lastClassIdentifierName = i.s;
            }
        }
        //System.out.println("Identifier: " + n.s);
        return super.visit(n);
    }
}
