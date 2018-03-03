package ru.itmo.degtiarenko.tt.common;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import ru.itmo.degtiarenko.tt.autogen.grammar.LmbdLexer;
import ru.itmo.degtiarenko.tt.autogen.grammar.LmbdParser;
import ru.itmo.degtiarenko.tt.lmbd.*;

public class StubFactories {
    public static LambdaStub toLambdaStub(String s) {
        LmbdLexer lexer = new LmbdLexer(new ANTLRInputStream(s));
        return new LmbdParser(new CommonTokenStream(lexer)).let_expression().ret;
    }

    public static LambdaStub let(String alias, LambdaStub def, LambdaStub expr) {
        return scope -> {
            Lambda defLambda = def.resolve(scope);
            Variable var = new Variable(alias);
            Lambda exprLambda = expr.resolve(new AbstractionScope(var, scope));
            return new Let(var, defLambda, exprLambda);
        };
    }

    public static LambdaStub abstraction(String alias, LambdaStub body) {
        return scope -> {
            Variable param = new Variable(alias);
            return new Abstraction(param, body.resolve(new AbstractionScope(param, scope)));
        };
    }

    public static LambdaStub application(LambdaStub func, LambdaStub arg) {
        return scope -> {
            Lambda funcLambda = func.resolve(scope);
            Lambda argLambda = arg.resolve(scope);

            return new Application(funcLambda, argLambda);
        };
    }

    public static LambdaStub variable(String alias) {
        return scope -> new VariableRef(scope.findVariable(alias));
    }

}

