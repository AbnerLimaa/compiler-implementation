package ast.statement;

import java.util.Map;

public class CompoundStm implements IStatement {

    private IStatement statement1;
    private IStatement statement2;

    public CompoundStm(IStatement statement1, IStatement statement2) {
        this.statement1 = statement1;
        this.statement2 = statement2;
    }

    public IStatement getStatement1() {
        return this.statement1;
    }

    public IStatement getStatement2() {
        return this.statement2;
    }

    @Override
    public int maxargs() {
        return Math.max(this.statement1.maxargs(), this.statement2.maxargs());
    }

    @Override
    public void interp(Map<String, Double> memory) {
        this.statement1.interp(memory);
        this.statement2.interp(memory);
    }
}
