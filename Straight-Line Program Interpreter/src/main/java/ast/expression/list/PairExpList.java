package ast.expression.list;

import ast.expression.IExpression;

import java.util.LinkedList;
import java.util.List;

public class PairExpList implements IExpressionList {

    private IExpression head;
    private IExpressionList tail;

    public PairExpList(IExpression head, IExpressionList tail) {
        this.head = head;
        this.tail = tail;
    }

    public IExpression getHead() {
        return this.head;
    }

    public IExpressionList getTail() {
        return this.tail;
    }

    @Override
    public List<IExpression> getList() {
        List<IExpression> list = new LinkedList<>();
        list.add(this.head);
        list.addAll(this.tail.getList());
        return list;
    }

    @Override
    public int maxargs() {
        int maxargs = this.head.maxargs();
        for (IExpression expression : this.tail.getList()) {
            maxargs = Math.max(maxargs, expression.maxargs());
        }
        return maxargs;
    }
}
