package model.statement.executionStack;

import exception.exception;
import model.collection.map.mapInterface;
import model.collection.programState.programState;
import model.collection.stack.stackInterface;
import model.expression.Expression;
import model.statement.Statement;
import model.type.Type;
import model.type.boolType;
import model.value.Value;
import model.value.boolValue;

public class whileStatement implements Statement {
    private Expression expression;
    private Statement statement;

    public whileStatement(Expression expression, Statement statement) {
        this.expression = expression;
        this.statement = statement;
    }

    @Override
    public String toString() {
        return "while(" + this.expression.toString() + ") "
                + this.statement.toString();
    }

    @Override
    public programState execute(programState state) throws exception {
        mapInterface<String, Value> symbolTable = state.getSymbolTable();
        mapInterface<Integer, Value> heap = state.getHeap();
        stackInterface<Statement> stack = state.getExecutionStack();
        try {
            boolValue value = (boolValue) this.expression.evaluate(symbolTable, heap);
            if (value.getValue()) {
                stack.push(this);
                stack.push(this.statement);
            }
        } catch (Exception ex) { throw new exception("Conditional expression not a boolean"); }
        return null;
    }

    @Override
    public mapInterface<String, Type> typeCheck(mapInterface<String, Type> typeEnvironment) throws exception {
        if (this.expression.typeCheck(typeEnvironment) instanceof boolType) {
            this.statement.typeCheck(typeEnvironment.copy());
            return typeEnvironment;
        } else throw new exception("Expression " + this.expression.toString() + " is not a boolean");
    }

    @Override
    public Statement copy() {
        return new whileStatement(this.expression, this.statement);
    }
}
