package model.statement.executionStack;

import exception.exception;
import model.collection.map.mapInterface;
import model.collection.programState.programState;
import model.collection.stack.stackInterface;
import model.statement.Statement;
import model.type.Type;

public class compoundStatement implements Statement {
    private Statement first;
    private Statement second;

    public compoundStatement(Statement first, Statement second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public String toString() {
        return  first.toString() + "; " + second.toString();
    }

    @Override
    public programState execute(programState state) throws exception {
        stackInterface<Statement> stack = state.getExecutionStack();
        stack.push(this.second);
        stack.push(this.first);
        return null;
    }

    @Override
    public mapInterface<String, Type> typeCheck(mapInterface<String, Type> typeEnvironment) throws exception {
        this.first.typeCheck(typeEnvironment);
        this.second.typeCheck(typeEnvironment);
        return typeEnvironment;
    }

    @Override
    public Statement copy() {
        return new compoundStatement(this.first.copy(), this.second.copy());
    }
}
