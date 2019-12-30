package model.statement.executionStack;

import exception.exception;
import model.collection.map.mapInterface;
import model.collection.programState.programState;
import model.collection.stack.stack;
import model.statement.Statement;
import model.type.Type;

public class forkStatement implements Statement {
    private Statement statement;

    public forkStatement(Statement statement) {
        this.statement = statement;
    }

    @Override
    public programState execute(programState state) throws exception {
        return new programState(new stack<Statement>(),
                state.getSymbolTable().copy(),
                state.getOutput(),
                state.getFileTable(),
                state.getHeap(),
                this.statement);
    }

    @Override
    public Statement copy() {
        return new forkStatement(this.statement);
    }

    @Override
    public mapInterface<String, Type> typeCheck(mapInterface<String, Type> typeEnvironment) throws exception {
        this.statement.typeCheck(typeEnvironment.copy());
        return typeEnvironment;
    }

    @Override
    public String toString() {
        return "fork(" + this.statement.toString() + ")";
    }
}
