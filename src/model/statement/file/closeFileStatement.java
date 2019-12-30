package model.statement.file;

import exception.exception;
import model.collection.map.mapInterface;
import model.collection.programState.programState;
import model.expression.Expression;
import model.statement.Statement;
import model.type.Type;
import model.type.stringType;
import model.value.Value;
import model.value.stringValue;

import java.io.BufferedReader;
import java.io.IOException;

public class closeFileStatement implements Statement {
    Expression expression;

    public closeFileStatement(Expression expression) {
        this.expression = expression;
    }

    @Override
    public String toString() {
        return "closeFile(" + this.expression.toString() + ")";
    }

    @Override
    public programState execute(programState state) throws exception {
        mapInterface<String, Value> symbolTable = state.getSymbolTable();
        mapInterface<String, BufferedReader> fileTable = state.getFileTable();
        mapInterface<Integer, Value> heap = state.getHeap();

        Value expressionValue = null;
        try {
            expressionValue = this.expression.evaluate(symbolTable, heap);
        } catch (exception exception) {
            exception.printStackTrace();
        }
        //if (expressionValue.getType().equals(new stringType())) {
            String expressionString = ((stringValue) expressionValue).getValue();
            try {
                fileTable.get(expressionString).close();
                fileTable.remove(expressionString);
            } catch (IOException e) {
                throw new exception("File " + expressionString + " is not open.");
            }
        //} else throw new exception("Expression " + this.expression.toString() + " must be string.");
        return null;
    }

    @Override
    public mapInterface<String, Type> typeCheck(mapInterface<String, Type> typeEnvironment) throws exception {
        if(this.expression.typeCheck(typeEnvironment).equals(new stringType())) {
            return typeEnvironment;
        } else throw new exception("Expression " + this.expression.toString() + " is not a string.");
    }

    @Override
    public Statement copy() {
        return new closeFileStatement(this.expression);
    }
}
