package ru.itmo.degtiarenko.tt.lmbd;

import ru.itmo.degtiarenko.tt.types.PolyType;
import ru.itmo.degtiarenko.tt.types.Type;
import ru.itmo.degtiarenko.tt.types.TypeManager;

import java.util.Set;
import java.util.stream.Collectors;

import static ru.itmo.degtiarenko.tt.lmbd.MemoLambda.memo;

public class Let extends Lambda {
    private final Variable variable;
    private final LambdaWrapper defContainer;
    private final LambdaWrapper exprContainer;

    public Let(Variable variable, LambdaWrapper defContainer, LambdaWrapper exprContainer) {
        super();
        this.variable = variable;
        this.defContainer = defContainer;
        this.exprContainer = exprContainer;

        variables.addAll(getDefinition().variables);
        variables.addAll(getExpr().variables);
        variables.remove(variable);
    }

    @Override
    public LambdaWrapper substitute(Variable varSub, LambdaWrapper sub) {
        if (checkVariables(varSub, sub)) {
            return null;
        }
        LambdaWrapper definitionSub = defContainer.substitute(varSub, sub);
        LambdaWrapper exprSub = exprContainer.substitute(varSub, sub);
        if (definitionSub == null || exprSub == null) {
            return null;
        }
        return new Let(variable, definitionSub, exprSub);
    }

    @Override
    public LambdaWrapper substituteShared(Variable varSub, LambdaWrapper sub, VariableStack prevVarStack, VariableStack newVarStack) {
        if (checkVariables(varSub, sub)) {
            return null;
        }
        Variable newVariable = new Variable(variable.getName());
        LambdaWrapper definitionSub = defContainer.substituteShared(varSub, sub, prevVarStack, newVarStack);
        LambdaWrapper exprSub = exprContainer.substituteShared(varSub, sub,
                new VariableStack(variable, prevVarStack),
                new VariableStack(newVariable, newVarStack));
        if (definitionSub == null || exprSub == null) {
            return null;
        }
        return new Let(newVariable, definitionSub, exprSub);
    }

    @Override
    public Lambda reduce() {
        LambdaWrapper substituted = exprContainer.substitute(variable, memo(getDefinition()));
        if (substituted != null) {
            return substituted.getLambda();
        }
        Lambda defReduced = getDefinition().reduce();
        if (defReduced == null) {
            return null;
        }
        if (defReduced == defContainer) {
            return this;
        }
        return new Let(variable, defReduced, getExpr());
    }

    private boolean checkVariables(Variable varSub, LambdaWrapper sub) {
        return getExpr().variables.contains(varSub) &&
                sub.getLambda().variables.stream()
                        .anyMatch(v -> v.getName().equals(varSub.getName()));
    }

    @Override
    public Type deduceType(TypeManager typeManager) {
        Type defType = getDefinition().deduceType(typeManager);
        Set<Type> polymorphicTypes = defType.getVariables()
                .stream()
                .filter(type -> {
                    boolean filter = true;
                    for (Variable defVar: getDefinition().getVariables()) {
                        if (typeManager.typeOf(defVar).getType().contains(type)) {
                            filter = false;
                            break;
                        }
                    }
                    return filter;
                }).collect(Collectors.toSet());
        typeManager.assignType(variable, new PolyType(defType, polymorphicTypes));
        return getExpr().deduceType(typeManager);
    }

    public Lambda getDefinition() {
        return defContainer.getLambda();
    }

    public Lambda getExpr() {
        return exprContainer.getLambda();
    }
}
