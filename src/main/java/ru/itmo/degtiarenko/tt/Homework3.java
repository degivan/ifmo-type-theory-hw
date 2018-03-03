package ru.itmo.degtiarenko.tt;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Scanner;

import static ru.itmo.degtiarenko.tt.Homework2.getResult;

public class Homework3 {
    public static void main(String[] args) throws FileNotFoundException {
        Scanner in = new Scanner(new FileInputStream("src/main/resources/task3.in"));
        PrintWriter out = new PrintWriter(new PrintStream("src/main/resources/task3.out"));
        String s = in.nextLine();

        long time = System.currentTimeMillis();

        String res = getResult(s);
        out.println(res);
        time = System.currentTimeMillis() - time;

        System.out.println("Execution time: " + time + " ms");

        in.close();
        out.close();
    }
}
