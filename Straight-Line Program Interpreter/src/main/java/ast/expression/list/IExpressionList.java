package ast.expression.list;

import ast.IAbstractSyntaxTree;
import ast.expression.IExpression;

import java.util.List;

public interface IExpressionList extends IAbstractSyntaxTree {

    List<IExpression> getList();
}
