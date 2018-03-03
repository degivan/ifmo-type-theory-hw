package ru.itmo.degtiarenko.tt.common;

import ru.itmo.degtiarenko.tt.lmbd.Variable;

public interface Scope {
    Variable findVariable(String name);
    boolean contains(Variable variable);
}
