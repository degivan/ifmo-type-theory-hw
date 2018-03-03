package ru.itmo.degtiarenko.tt.lmbd;

import ru.itmo.degtiarenko.tt.types.Type;
import ru.itmo.degtiarenko.tt.types.TypeManager;

public class VariableRef extends Lambda {
    private final Variable variable;

    public VariableRef(Variable variable) {
        this.variable = variable;
        variables.add(variable);
    }

    @Override
    public LambdaWrapper substitute(Variable varSub, LambdaWrapper sub) {
        return varSub.equals(variable) ? sub : this;
    }

    @Override
    public Type deduceType(TypeManager typeManager) {
        return typeManager.typeOf(this.variable).mono();
    }

    @Override
    public LambdaWrapper substituteShared(Variable varSub, LambdaWrapper sub, VariableStack prevVarStack, VariableStack newVarStack) {
        if (varSub.equals(variable)) {
            return sub;
        }
        VariableStack oldStack = prevVarStack;
        VariableStack newStack = newVarStack;
        while (oldStack != null) {
            if (newStack == null) {
                throw new IllegalStateException("Different size of stack!");
            }
            if (oldStack.getVariable().equals(variable)) {
                return makeRef(newStack.getVariable());
            }
            oldStack = oldStack.getPrev();
            newStack = newStack.getPrev();
        }
        return this;
    }

    @Override
    public String toString() {
        return variable.getName();
    }

    public static VariableRef makeRef(Variable variable) {
        return new VariableRef(variable);
    }
}
