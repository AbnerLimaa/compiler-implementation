package ast.expression;

import ast.statement.IStatement;

import java.util.Map;

public class EseqExp implements IExpression {

    private IStatement statement;
    private IExpression expression;

    public EseqExp(IStatement statement, IExpression expression) {
        this.statement = statement;
        this.expression = expression;
    }

    public IStatement getStatement() {
        return this.statement;
    }

    public IExpression getExpression() {
        return this.expression;
    }

    @Override
    public Double eval(Map<String, Double> memory) {
        this.statement.interp(memory);
        return this.expression.eval(memory);
    }

    @Override
    public int maxargs() {
        return Math.max(this.statement.maxargs(), this.expression.maxargs());
    }
}
