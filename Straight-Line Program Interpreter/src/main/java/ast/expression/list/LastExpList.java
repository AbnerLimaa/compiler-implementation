package ast.expression.list;

import ast.expression.IExpression;

import java.util.LinkedList;
import java.util.List;

public class LastExpList implements IExpressionList {

    private IExpression head;

    public LastExpList(IExpression head) {
        this.head = head;
    }

    public IExpression getHead() {
        return this.head;
    }

    @Override
    public List<IExpression> getList() {
        List<IExpression> list = new LinkedList<>();
        list.add(this.head);
        return list;
    }

    @Override
    public int maxargs() {
        return this.head.maxargs();
    }
}
