package ast.expression;

import java.util.Map;

public class IdExp implements IExpression {

    private String id;

    public IdExp(String id) {
        this.id = id;
    }

    public String getId() {
        return this.id;
    }

    @Override
    public Double eval(Map<String, Double> memory) {
        return memory.getOrDefault(this.id, 0.0);
    }

    @Override
    public int maxargs() {
        return 0;
    }
}
