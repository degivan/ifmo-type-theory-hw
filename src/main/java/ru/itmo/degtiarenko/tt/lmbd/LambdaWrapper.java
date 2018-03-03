package ru.itmo.degtiarenko.tt.lmbd;

public interface LambdaWrapper {
    Lambda getLambda();

    LambdaWrapper reduce();

    LambdaWrapper substitute(Variable varSub, LambdaWrapper sub);

    LambdaWrapper substituteShared(Variable varSub, LambdaWrapper sub, VariableStack prevVarStack,
                                   VariableStack newVarStack);
}
