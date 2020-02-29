package ast.expression;

import java.util.Map;

public class OpExp implements IExpression {

    private IExpression leftExpression;
    private IExpression rightExpression;
    private Op operator;

    public enum Op { PLUS, MINUS, TIMES, DIV }

    public OpExp(IExpression left, Op operator, IExpression right) {
        this.leftExpression = left;
        this.operator = operator;
        this.rightExpression = right;
    }

    public IExpression getLeftExpression() {
        return this.leftExpression;
    }

    public IExpression getRightExpression() {
        return this.rightExpression;
    }

    public Op getOperator() {
        return this.operator;
    }

    @Override
    public Double eval(Map<String, Double> memory) {
        Double left = this.leftExpression.eval(memory);
        Double right = this.rightExpression.eval(memory);
        switch (operator) {
            case PLUS: return left + right;
            case MINUS: return left - right;
            case TIMES: return left * right;
            case DIV: return left / right;
            default: return 0.0;
        }
    }

    @Override
    public int maxargs() {
        return Math.max(this.leftExpression.maxargs(), this.rightExpression.maxargs());
    }
}
