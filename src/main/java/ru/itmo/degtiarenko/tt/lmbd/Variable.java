package ru.itmo.degtiarenko.tt.lmbd;

public class Variable {
    private final String name;

    public Variable(String name) {
        this.name = name;
    }

    public static Variable makeVar(String name) {
        return new Variable(name);
    }

    public String getName() {
        return name;
    }
}
