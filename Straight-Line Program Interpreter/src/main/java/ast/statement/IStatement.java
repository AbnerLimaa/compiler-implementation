package ast.statement;

import ast.IAbstractSyntaxTree;

import java.util.Map;

public interface IStatement extends IAbstractSyntaxTree {

    void interp(Map<String, Double> memory);
}
