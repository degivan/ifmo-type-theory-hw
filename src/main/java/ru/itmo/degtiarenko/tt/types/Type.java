package ru.itmo.degtiarenko.tt.types;

import ru.itmo.degtiarenko.tt.common.NameGenerator;

import java.util.*;

public class Type {
    Type bType;
    private final TypeManager tm;

    Type(TypeManager tm) {
        this.tm = tm;
        bType = this;
    }

    public Type getBackingType() {
        if (this != bType) {
            bType = bType.getBackingType();
        }
        return bType;
    }

    public TypeDescriptor getDescriptor() {
        return tm.td(this);
    }


    @Override
    public String toString() {
        TypeDescriptor desc = getDescriptor();
        return desc != null ? desc.toString() : String.format("type[%s]",
                Integer.toHexString(getBackingType().hashCode()));
    }

    public boolean equals(Type other) {
        return tm.equalize(this, other, false);
    }

    public boolean unifyWith(Type other) {
        return tm.unify(this, other);
    }

    public Type concrete(NameGenerator typeNameGenerator) {
        TypeDescriptor desc = getDescriptor();
        if (desc == null) {
            tm.substitute(this, new Constant(typeNameGenerator.next()));
        } else {
            desc.getParams().forEach(t -> t.concrete(typeNameGenerator));
        }
        return this;
    }

    public boolean contains(Type other) {
        if (this.equals(other)) {
            return true;
        }
        TypeDescriptor desc = getDescriptor();
        if (desc == null) {
            return false;
        }
        return desc.contains(other);
    }

    public void countVariables(Set<Type> variables) {
        TypeDescriptor desc = getDescriptor();
        if(desc == null) {
            variables.add(this.getBackingType());
        } else {
            desc.params.forEach(t -> t.countVariables(variables));
        }
    }

    public Set<Type> getVariables() {
        Set<Type> vars = new HashSet<>();
        countVariables(vars);
        return vars;
    }

    public Type recreateLiterals(Set<Type> literals, Map<Type, Type> createdLiterals) {
        TypeDescriptor desc = getDescriptor();
        if (desc == null) {
            if(literals.contains(getBackingType())) {
                literals.remove(getBackingType());
                Type lit = tm.createType(null);
                createdLiterals.put(getBackingType(), lit);
                return lit;
            }
            return createdLiterals.getOrDefault(getBackingType(), this);
        } else {
            boolean changed = false;
            List<Type> newParams =new ArrayList<>();
            for (Type type: desc.params) {
                Type newParam = type.recreateLiterals(literals, createdLiterals);
                newParams.add(newParam);
                if (type != newParam) {
                    changed = true;
                }
            }
            if (changed) {
                return tm.createType(desc.clone(newParams));
            } else {
                return this;
            }
        }
    }
}
