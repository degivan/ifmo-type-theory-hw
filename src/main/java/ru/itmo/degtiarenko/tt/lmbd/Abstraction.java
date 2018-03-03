package ru.itmo.degtiarenko.tt.lmbd;

import ru.itmo.degtiarenko.tt.types.PolyType;
import ru.itmo.degtiarenko.tt.types.Type;
import ru.itmo.degtiarenko.tt.types.TypeManager;

public class Abstraction extends Lambda {
    private Variable param;

    private LambdaWrapper bodyContainer;

    public Abstraction(Variable param, LambdaWrapper bodyContainer) {
        this.param = param;
        this.bodyContainer = bodyContainer;
        variables.addAll(getBody().variables);
        variables.remove(param);
    }

    public Variable getParam() {
        return param;
    }

    public LambdaWrapper getBodyContainer() {
        return bodyContainer;
    }

    public Lambda getBody() {
        return bodyContainer.getLambda();
    }

    @Override
    public Lambda reduce() {
        LambdaWrapper reduced = bodyContainer.reduce();
        if (reduced == null) {
            return null;
        }
        if (reduced == bodyContainer) {
            return this;
        }
        return new Abstraction(param, reduced);
    }

    @Override
    public Lambda substitute(Variable varSub, LambdaWrapper sub) {
        if (checkVariables(varSub, sub)) {
            return null;
        }
        LambdaWrapper bodySubst = bodyContainer.substitute(varSub, sub);
        if (bodySubst == null) {
            return null;
        }
        return new Abstraction(param, bodySubst);
    }

    @Override
    public Type deduceType(TypeManager typeManager) {
        Type paramType = typeManager.typeOf(param).getType();
        Type bodyType = getBody().deduceType(typeManager);
        if (bodyType == null) {
            return null;
        }
        return typeManager.createTypeApplication(paramType, bodyType);
    }

    @Override
    public LambdaWrapper substituteShared(Variable varSub, LambdaWrapper sub, VariableStack prevVarStack, VariableStack newVarStack) {
        if (checkVariables(varSub, sub)) {
            return null;
        }
        Variable newParam = new Variable(param.getName());
        LambdaWrapper bodySubst = bodyContainer.substituteShared(varSub, sub,
                new VariableStack(param, prevVarStack),
                new VariableStack(newParam, newVarStack));
        if (bodySubst == null) {
            return null;
        }
        return new Abstraction(newParam, bodySubst);
    }

    @Override
    public String toString() {
        return '\\' + param.getName() + '.' + getBody();
    }

    private boolean checkVariables(Variable varSub, LambdaWrapper subst) {
        return getBody().variables.contains(varSub)
                && subst.getLambda().variables
                .stream()
                .anyMatch(v -> v.getName().equals(param.getName()));
    }
}
