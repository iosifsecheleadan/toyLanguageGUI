package model.statement.symbolTable;

import exception.exception;
import model.collection.list.listInterface;
import model.collection.map.mapInterface;
import model.collection.programState.programState;
import model.expression.Expression;
import model.statement.Statement;
import model.type.Type;
import model.value.Value;

public class printStatement implements Statement {
    private Expression expression;

    public printStatement(Expression expression) {
        this.expression = expression;
    }

    @Override
    public String toString() {
        return "print(" + expression.toString() + ")" ;
    }

    @Override
    public programState execute(programState state) throws exception {
        //stackInterface<Statement> stack = state.getExecutionStack();
        listInterface<Value> output = state.getOutput();
        mapInterface<String, Value> symbolTable = state.getSymbolTable();
        mapInterface<Integer, Value> heap = state.getHeap();

        try {
            Value value = this.expression.evaluate(symbolTable, heap);
            output.append(value);
        } catch (exception exception) {
            throw new exception("Could not print " + this.expression.toString());
        }
        return null;
    }

    @Override
    public mapInterface<String, Type> typeCheck(mapInterface<String, Type> typeEnvironment) throws exception {
        expression.typeCheck(typeEnvironment);
        return typeEnvironment;
    }

    @Override
    public Statement copy() {
        return new printStatement(this.expression);
    }
}
