package ru.itmo.degtiarenko.tt;

import ru.itmo.degtiarenko.tt.common.GlobalScope;
import ru.itmo.degtiarenko.tt.common.LambdaStub;
import ru.itmo.degtiarenko.tt.common.NameGenerator;
import ru.itmo.degtiarenko.tt.common.StubFactories;
import ru.itmo.degtiarenko.tt.lmbd.Lambda;
import ru.itmo.degtiarenko.tt.lmbd.Variable;
import ru.itmo.degtiarenko.tt.types.Type;
import ru.itmo.degtiarenko.tt.types.TypeManager;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.Set;

public class Homework2 {
    public static void main(String[] args) throws FileNotFoundException {
        Scanner in = new Scanner(new FileInputStream("src/main/resources/task2.in"));
        PrintWriter out = new PrintWriter(new PrintStream("src/main/resources/task2.out"));
        String s = in.nextLine();

        long time = System.currentTimeMillis();

        String res = getResult(s);
        out.println(res);
        time = System.currentTimeMillis() - time;

        System.out.println("Execution time: " + time + " ms");

        in.close();
        out.close();
    }

    public static String getResult(String s) {
        LambdaStub lambdaStub = StubFactories.toLambdaStub(s);
        Lambda lambda = lambdaStub.resolve(GlobalScope.getGlobalScope());

        final TypeManager typeManager = new TypeManager();
        NameGenerator nm = new NameGenerator("t");
        Type type = lambda.deduceType(typeManager);
        if (type != null) {
            StringBuilder sb = new StringBuilder();
            sb.append("Type: ").append(type.concrete(nm)).append("\n");
            Set<Variable> variables = lambda.getVariables();
            sb.append("Context: ").append(variables.isEmpty() ? "empty\n" : "\n");
            for (Variable var: variables) {
                sb.append('\t');
                sb.append(var.getName());
                sb.append(" : ");
                sb.append(typeManager.typeOf(var).mono().concrete(nm));
            }
            return sb.toString();
        } else {
            return "No type deduced";
        }
    }
}
