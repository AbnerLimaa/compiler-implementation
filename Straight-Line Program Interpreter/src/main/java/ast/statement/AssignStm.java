package ast.statement;

import ast.expression.IExpression;

import java.util.Map;

public class AssignStm implements IStatement {

    private String id;
    private IExpression expression;

    public AssignStm(String id, IExpression expression) {
        this.id = id;
        this.expression = expression;
    }

    public String getId() {
        return this.id;
    }

    public IExpression getExpression() {
        return this.expression;
    }

    @Override
    public int maxargs() {
        return this.expression.maxargs();
    }

    @Override
    public void interp(Map<String, Double> memory) {
        memory.put(this.id, this.expression.eval(memory));
    }
}
