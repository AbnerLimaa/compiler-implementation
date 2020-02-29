package ast.statement;

import ast.expression.IExpression;
import ast.expression.list.IExpressionList;

import java.util.List;
import java.util.Map;

public class PrintStm implements IStatement {

    private IExpressionList expressionList;

    public PrintStm(IExpressionList expressionList) {
        this.expressionList = expressionList;
    }

    public IExpressionList getExpressionList() {
        return this.expressionList;
    }

    @Override
    public int maxargs() {
        List<IExpression> list = this.expressionList.getList();
        int maxargs = list.size();
        for (IExpression expression : list) {
            maxargs = Math.max(maxargs, expression.maxargs());
        }
        return maxargs;
    }

    @Override
    public void interp(Map<String, Double> memory) {
        List<IExpression> list = this.expressionList.getList();
        for (IExpression expression : list) {
            System.out.print(expression.eval(memory) + " ");
        }
        System.out.println();
    }
}
