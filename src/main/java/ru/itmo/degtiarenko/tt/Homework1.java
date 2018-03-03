package ru.itmo.degtiarenko.tt;

import ru.itmo.degtiarenko.tt.common.GlobalScope;
import ru.itmo.degtiarenko.tt.common.LambdaStub;
import ru.itmo.degtiarenko.tt.common.StubFactories;
import ru.itmo.degtiarenko.tt.lmbd.Lambda;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Scanner;

public class Homework1 {
    public static void main(String... args) throws FileNotFoundException {
        Scanner in = new Scanner(new FileInputStream("src/main/resources/task1.in"));
        PrintWriter out = new PrintWriter(new PrintStream("src/main/resources/task1.out"));
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
        return lambda.reduceFully().toString();
    }
}
