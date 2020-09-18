package visitor;

import symboltable.SymbolTable;
import symboltable.entries.*;
import syntaxtree.*;

import java.util.ArrayList;
import java.util.List;

public class TypeCheckVisitor extends TypeDepthFirstVisitor {

    private ClassDecl currentClass;
    private MethodDecl currentMethod;

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

    private Boolean classInherits(String s1, String s2) {
        if (s1 != null && s2 != null) {
            ClassEntry classEntry2 = symbolTable.searchEntry(s2);
            if (classEntry2 != null && classEntry2.getSuper() != null)
                return classEntry2.getSuper().equals(s1);
        }
        return false;
    }

    private Type getMethodType(String className, String methodName) {
        if (className != null && methodName != null) {
            ClassEntry classEntry = symbolTable.searchEntry(className);
            if (classEntry != null) {
                MethodEntry methodEntry = classEntry.getMethodTable().searchEntry(methodName);
                if (methodEntry != null)
                    return methodEntry.getReturnType();
            }
        }
        return null;
    }

    private List<ParamEntry> getMethodParams(String className, String methodName) {
        if (className != null && methodName != null) {
            ClassEntry classEntry = symbolTable.searchEntry(className);
            if (classEntry != null) {
                MethodEntry methodEntry = classEntry.getMethodTable().searchEntry(methodName);
                if (methodEntry != null)
                    return methodEntry.getParamTable().getEntries();
            }
        }
        return new ArrayList<>();
    }

    private Type getVarType(String varName) {
        if (this.currentClass != null) {
            ClassEntry classEntry = symbolTable.searchEntry(this.currentClass.i.s);
            if (classEntry != null) {
                FieldEntry fieldEntry = classEntry.getFieldTable().searchEntry(varName);
                if (this.currentMethod != null) {
                    MethodEntry methodEntry = classEntry.getMethodTable().searchEntry(this.currentMethod.i.s);
                    if (methodEntry != null) {
                        LocalEntry localEntry = methodEntry.getLocalTable().searchEntry(varName);
                        ParamEntry paramEntry = methodEntry.getParamTable().searchEntry(varName);
                        if (localEntry != null)
                            return localEntry.getType();
                        else if (paramEntry != null)
                            return paramEntry.getType();
                        else if (fieldEntry != null)
                            return fieldEntry.getType();
                    }
                }
                else if (fieldEntry != null)
                    return fieldEntry.getType();
            }
        }
        return null;
    }

    private Type getVarType(String className, String methodName, String varName) {
        if (className != null) {
            ClassEntry classEntry = symbolTable.searchEntry(className);
            if (classEntry != null) {
                FieldEntry fieldEntry = classEntry.getFieldTable().searchEntry(varName);
                if (methodName != null) {
                    MethodEntry methodEntry = classEntry.getMethodTable().searchEntry(methodName);
                    if (methodEntry != null) {
                        LocalEntry localEntry = methodEntry.getLocalTable().searchEntry(varName);
                        ParamEntry paramEntry = methodEntry.getParamTable().searchEntry(varName);
                        if (localEntry != null)
                            return localEntry.getType();
                        else if (paramEntry != null)
                            return paramEntry.getType();
                        else if (fieldEntry != null)
                            return fieldEntry.getType();
                    }
                }
                else if (fieldEntry != null)
                    return fieldEntry.getType();
            }
        }
        return null;
    }

    private Boolean isSameType(Type t1, Type t2) {
        if (t1 instanceof BooleanType && t2 instanceof BooleanType)
            return true;
        if (t1 instanceof IntegerType && t2 instanceof IntegerType)
            return true;
        if (t1 instanceof IntArrayType && t2 instanceof IntArrayType)
            return true;
        if (t1 instanceof IdentifierType && t2 instanceof IdentifierType) {
            IdentifierType type1 = (IdentifierType)t1;
            IdentifierType type2 = (IdentifierType)t2;
            if (type1.s != null && type2.s != null) {
                boolean condition1 = type1.s.equals(type2.s);
                boolean condition2 = classInherits(type1.s, type2.s);
                return condition1 || condition2;
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
    public Type visit(Print n) {
        if (n != null && n.e != null) {
            Type type = n.e.accept(this);
            if (!(type instanceof IntegerType))
                errors.add("Trying to print value that is not integer");
        }
        return super.visit(n);
    }

    @Override
    public Type visit(If n) {
        if (n != null && n.e != null) {
            Type type = n.e.accept(this);
            if (!(type instanceof BooleanType))
                errors.add("Trying to assign an expression that is not boolean to an if statement");
        }
        return super.visit(n);
    }

    @Override
    public Type visit(While n) {
        if (n != null && n.e != null) {
            Type type = n.e.accept(this);
            if (!(type instanceof BooleanType))
                errors.add("Trying to assign an expression that is not boolean to an while statement");
        }
        return super.visit(n);
    }

    @Override
    public Type visit(Assign n) {
        if (n != null && n.e != null && n.i != null && n.i.s != null) {
            Type type1 = getVarType(n.i.s);
            Type type2 = n.e.accept(this);
            if (!isSameType(type1, type2))
                errors.add("Trying to assign value with type different than identifier type");
        }
        return super.visit(n);
    }

    @Override
    public Type visit(ArrayAssign n) {
        if (n != null && n.e1 != null && n.e2 != null && n.i != null && n.i.s != null) {
            Type type = getVarType(n.i.s);
            Type type1 = n.e1.accept(this);
            Type type2 = n.e2.accept(this);
            if (!(type instanceof IntArrayType))
                errors.add("Identifier is not an integer array");
            if (!(type1 instanceof IntegerType))
                errors.add("Array index must be of type integer");
            if (!(type2 instanceof IntegerType))
                errors.add("Trying to assign array value with type different than integer");
        }
        return super.visit(n);
    }

    @Override
    public Type visit(And n) {
        if (n != null && n.e1 != null && n.e2 != null) {
            Type type1 = n.e1.accept(this);
            Type type2 = n.e2.accept(this);
            if (!(type1 instanceof BooleanType))
                errors.add("Left side of And expression must have boolean value");
            if (!(type2 instanceof BooleanType))
                errors.add("Right side of And expression must have boolean value");
        }
        return new BooleanType();
    }

    @Override
    public Type visit(Not n) {
        if (n != null && n.e != null) {
            Type type = n.e.accept(this);
            if (!(type instanceof BooleanType))
                errors.add("Trying to negate an expression that is not boolean");
        }
        return new BooleanType();
    }

    @Override
    public Type visit(True n) {
        return new BooleanType();
    }

    @Override
    public Type visit(False n) {
        return new BooleanType();
    }

    @Override
    public Type visit(LessThan n) {
        if (n != null && n.e1 != null && n.e2 != null) {
            Type type1 = n.e1.accept(this);
            Type type2 = n.e2.accept(this);
            if (!(type1 instanceof IntegerType))
                errors.add("Left side of Less Than expression must have integer value");
            if (!(type2 instanceof IntegerType))
                errors.add("Right side of Less Than expression must have integer value");
        }
        return new BooleanType();
    }

    @Override
    public Type visit(Plus n) {
        if (n != null && n.e1 != null && n.e2 != null) {
            Type type1 = n.e1.accept(this);
            Type type2 = n.e2.accept(this);
            if (!(type1 instanceof IntegerType))
                errors.add("Left side of Plus expression must have integer value");
            if (!(type2 instanceof IntegerType))
                errors.add("Right side of Plus expression must have integer value");
        }
        return new IntegerType();
    }

    @Override
    public Type visit(Minus n) {
        if (n != null && n.e1 != null && n.e2 != null) {
            Type type1 = n.e1.accept(this);
            Type type2 = n.e2.accept(this);
            if (!(type1 instanceof IntegerType))
                errors.add("Left side of Minus expression must have integer value");
            if (!(type2 instanceof IntegerType))
                errors.add("Right side of Minus expression must have integer value");
        }
        return new IntegerType();
    }

    @Override
    public Type visit(Times n) {
        if (n != null && n.e1 != null && n.e2 != null) {
            Type type1 = n.e1.accept(this);
            Type type2 = n.e2.accept(this);
            if (!(type1 instanceof IntegerType))
                errors.add("Left side of Times expression must have integer value");
            if (!(type2 instanceof IntegerType))
                errors.add("Right side of Times expression must have integer value");
        }
        return new IntegerType();
    }

    @Override
    public Type visit(IntegerLiteral n) {
        return new IntegerType();
    }

    @Override
    public Type visit(This n) {
        if (this.currentClass != null)
            return new IdentifierType(this.currentClass.i.s);
        return super.visit(n);
    }

    @Override
    public Type visit(NewObject n) {
        if (n != null && n.i != null && n.i.s != null)
            return new IdentifierType(n.i.s);
        return super.visit(n);
    }

    @Override
    public Type visit(Call n) {
        if (n != null && n.e != null && n.i != null && n.i.s != null && n.el != null) {
            Type eType = n.e.accept(this);
            if (eType instanceof IdentifierType) {
                IdentifierType iType = (IdentifierType)eType;
                if (iType.s != null) {
                    List<ParamEntry> params = getMethodParams(iType.s, n.i.s);
                    if (params.size() != n.el.size())
                        errors.add("Arguments size does not match method params size");
                    else {
                        for(int i = 0; i < params.size(); i++) {
                            String paramSymbol = params.get(i).getSymbol();
                            Type t1 = getVarType(iType.s, n.i.s, paramSymbol);
                            Type t2 = n.el.elementAt(i).accept(this);
                            if (!isSameType(t1, t2))
                                errors.add("Trying to pass argument to method with different value");
                        }
                    }
                    return getMethodType(iType.s, n.i.s);
                }
            }
            else
                errors.add("Trying to call a function from an expression that does not returns class instance");
        }
        return super.visit(n);
    }

    @Override
    public Type visit(NewArray n) {
        if (n != null && n.e != null) {
            Type eType = n.e.accept(this);
            if (!(eType instanceof IntegerType))
                errors.add("Trying to create array with an expression that is not integer");
        }
        return new IntArrayType();
    }

    @Override
    public Type visit(ArrayLength n) {
        if (n != null && n.e != null) {
            Type eType = n.e.accept(this);
            if (!(eType instanceof IntArrayType))
                errors.add("Trying to get array length from an expression that is not an instance of array");
        }
        return new IntegerType();
    }

    @Override
    public Type visit(ArrayLookup n) {
        if (n != null && n.e1 != null && n.e2 != null) {
            Type e1Type = n.e1.accept(this);
            Type e2Type = n.e2.accept(this);
            if (!(e1Type instanceof IntArrayType))
                errors.add("Trying to get array length from an expression that is not an instance of array");
            if (!(e2Type instanceof IntegerType))
                errors.add("Trying get a value on array that is not integer");
        }
        return new IntegerType();
    }

    @Override
    public Type visit(Identifier n) {
        if (n != null && n.s != null)
            return getVarType(n.s);
        return super.visit(n);
    }

    @Override
    public Type visit(IdentifierExp n) {
        if (n != null && n.s != null)
            return getVarType(n.s);
        return super.visit(n);
    }
}
