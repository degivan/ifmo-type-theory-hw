package ru.itmo.degtiarenko.tt.lmbd;


public class MemoLambda implements LambdaWrapper {
    private Lambda lambdaInt;
    private boolean fullyReduced = false;

    public MemoLambda(Lambda lambdaInt) {
        this.lambdaInt = lambdaInt;
    }

    @Override
    public Lambda getLambda() {
        return lambdaInt;
    }

    @Override
    public LambdaWrapper reduce() {
        if (fullyReduced) {
            return null;
        }
        Lambda reduced = lambdaInt.reduce();
        if (reduced == null) {
            fullyReduced = true;
            return null;
        }
        lambdaInt = reduced;
        return this;
    }

    @Override
    public LambdaWrapper substitute(Variable varSub, LambdaWrapper sub) {
        return memo(getLambda().substituteShared(varSub, sub, null, null));
    }

    @Override
    public LambdaWrapper substituteShared(Variable varSub, LambdaWrapper sub, VariableStack prevVarStack, VariableStack newVarStack) {
        return memo(getLambda().substituteShared(varSub, sub, prevVarStack, newVarStack));
    }

    public static MemoLambda memo(LambdaWrapper arg) {
        if (arg instanceof MemoLambda) {
            return (MemoLambda) arg;
        } else {
            return new MemoLambda(arg.getLambda());
        }
    }
}
