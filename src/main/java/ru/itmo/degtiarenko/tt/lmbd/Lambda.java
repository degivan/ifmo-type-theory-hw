package ru.itmo.degtiarenko.tt.lmbd;


import ru.itmo.degtiarenko.tt.types.PolyType;
import ru.itmo.degtiarenko.tt.types.Type;
import ru.itmo.degtiarenko.tt.types.TypeManager;

import java.util.HashSet;
import java.util.Set;

public abstract class Lambda implements LambdaWrapper {
    protected Set<Variable> variables = new HashSet<>();

    @Override
    public Lambda getLambda() {
        return this;
    }

    @Override
    public Lambda reduce() {
        return null;
    }

    @Override
    public LambdaWrapper substitute(Variable varSub, LambdaWrapper sub) {
        return this;
    }

    public Lambda reduceFully() {
        Lambda lambda = this;
        Lambda reduced = lambda.reduce();
        while (reduced != null) {
//            System.out.println(reduced);
            lambda = reduced;
            reduced = lambda.reduce();
        }
        return lambda;
    }

    public abstract Type deduceType(TypeManager typeManager);

    public Set<Variable> getVariables() {
        return variables;
    }
}
