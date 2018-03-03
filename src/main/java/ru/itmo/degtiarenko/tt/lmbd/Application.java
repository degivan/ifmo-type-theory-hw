package ru.itmo.degtiarenko.tt.lmbd;

import ru.itmo.degtiarenko.tt.types.Type;
import ru.itmo.degtiarenko.tt.types.TypeManager;

import static ru.itmo.degtiarenko.tt.lmbd.MemoLambda.memo;

public class Application extends Lambda {
    private final LambdaWrapper funcContainer;
    private final LambdaWrapper argContainer;

    public Application(LambdaWrapper funcContainer, LambdaWrapper argContainer) {
        this.funcContainer = funcContainer;
        this.argContainer = argContainer;
        variables.addAll(getFunc().variables);
        variables.addAll(getArg().variables);
    }

    public Lambda getFunc() {
        return funcContainer.getLambda();
    }

    public Lambda getArg() {
        return argContainer.getLambda();
    }

    @Override
    public Lambda reduce() {
        if (funcContainer instanceof Abstraction) {
            return ((Abstraction) funcContainer).getBodyContainer()
                    .substitute(((Abstraction) funcContainer).getParam(),
                            memo(getArg())).getLambda();
        }
        if (funcContainer instanceof MemoLambda) {
            Lambda funcInt = funcContainer.getLambda();
            if (funcInt instanceof Abstraction) {
                return ((Abstraction) funcInt).getBodyContainer().substituteShared(((Abstraction) funcInt).getParam(),
                        memo(getArg()), null, null).getLambda();
            }
        }
        LambdaWrapper funcReduced = funcContainer.reduce();
        if (funcReduced != null) {
            if (funcReduced == funcContainer) {
                return this;
            }
            return new Application(funcReduced, argContainer);
        }

        LambdaWrapper argReduced = argContainer.reduce();
        if (argReduced != null) {
            if (argReduced == argContainer) {
                return this;
            }
            return new Application(funcContainer, argReduced);
        }
        return null;
    }

    @Override
    public Lambda substitute(Variable varSub, LambdaWrapper sub) {
        LambdaWrapper funcSubst = funcContainer.substitute(varSub, sub);
        LambdaWrapper argSubst = argContainer.substitute(varSub, sub);
        if (funcSubst != null && argSubst != null) {
            return new Application(funcSubst, argSubst);
        }
        return null;
    }

    @Override
    public Type deduceType(TypeManager typeManager) {
        Type funcType = getFunc().deduceType(typeManager);
        Type argType = getArg().deduceType(typeManager);
        if (funcType == null || argType == null) {
            return null;
        }
        Type resType = typeManager.createType(null);
        boolean unifyRes = funcType.unifyWith(typeManager.createTypeApplication(argType, resType));
        return unifyRes ? resType : null;
    }

    @Override
    public LambdaWrapper substituteShared(Variable varSub, LambdaWrapper sub, VariableStack prevVarStack, VariableStack newVarStack) {
        LambdaWrapper funcSubst = funcContainer.substituteShared(varSub, sub, prevVarStack, newVarStack);
        LambdaWrapper argSubst = argContainer.substituteShared(varSub, sub, prevVarStack, newVarStack);
        if (funcSubst != null && argSubst != null) {
            return new Application(funcSubst, argSubst);
        }
        return null;
    }

    @Override
    public String toString() {
        String strFunc = getFunc() instanceof Abstraction ?
                String.format("(%s)", getFunc().toString()) :
                getFunc().toString();
        String strArg = getArg() instanceof VariableRef ?
                getArg().toString() :
                String.format("(%s)", getArg().toString());
        return strFunc + ' ' + strArg;
    }
}
