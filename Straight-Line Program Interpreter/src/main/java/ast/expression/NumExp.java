package ast.expression;

import java.util.Map;

public class NumExp implements IExpression {

    private double number;

    public NumExp(double number) {
        this.number = number;
    }

    public double getNumber() {
        return this.number;
    }

    @Override
    public Double eval(Map<String, Double> memory) {
        return this.number;
    }

    @Override
    public int maxargs() {
        return 0;
    }
}
