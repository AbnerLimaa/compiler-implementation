package ast.expression;

import ast.IAbstractSyntaxTree;

import java.util.Map;

public interface IExpression extends IAbstractSyntaxTree {

    Double eval(Map<String, Double> memory);
}
