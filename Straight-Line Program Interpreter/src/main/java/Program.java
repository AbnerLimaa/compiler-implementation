import ast.expression.EseqExp;
import ast.expression.IdExp;
import ast.expression.NumExp;
import ast.expression.OpExp;
import ast.expression.list.LastExpList;
import ast.expression.list.PairExpList;
import ast.statement.AssignStm;
import ast.statement.CompoundStm;
import ast.statement.IStatement;
import ast.statement.PrintStm;

import java.util.HashMap;

public class Program {

    public static void main(String[] args) {
        //a := 5 + 3;
        //b := (print(a, a - 1), 10 * a);
        //print(b);
        IStatement program = new CompoundStm(
                new AssignStm("a", new OpExp(new NumExp(5), OpExp.Op.PLUS, new NumExp(3))),
                new CompoundStm(
                        new AssignStm("b", new EseqExp(
                                new PrintStm(new PairExpList(
                                        new IdExp("a"),
                                        new LastExpList(new OpExp(new IdExp("a"), OpExp.Op.MINUS, new NumExp(1)))
                                )),
                                new OpExp(new NumExp(10), OpExp.Op.TIMES, new IdExp("a"))
                        )),
                        new PrintStm(new LastExpList(new IdExp("b")))));

        program.interp(new HashMap<>());
        System.out.println("Max number of args on all PrintStm of this program: " + program.maxargs());
    }
}
