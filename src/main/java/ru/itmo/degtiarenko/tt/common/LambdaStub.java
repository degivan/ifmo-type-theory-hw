package ru.itmo.degtiarenko.tt.common;

import ru.itmo.degtiarenko.tt.lmbd.Lambda;

public interface LambdaStub {
    Lambda resolve(Scope scope);
}
