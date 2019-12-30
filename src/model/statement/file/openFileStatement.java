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

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class openFileStatement implements Statement {

    private final Expression expression;

    public openFileStatement(Expression expression) {
        this.expression = expression;
    }

    @Override
    public String toString() {
        return "openFile(" + this.expression.toString() + ")";
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
            if (fileTable.has(expressionString)) {
                throw new exception("File " + expressionValue + " is already open");
            } else {
                try {
                    if(! new File(expressionString).exists()) {
                        Files.createFile(Paths.get(expressionString));
                    }
                    fileTable.put(expressionString, new BufferedReader(new FileReader(expressionString)));
                } catch (FileNotFoundException ex) {
                    throw new exception("Could not open file :\n\t" + expressionString);
                } catch (IOException e) {
                    throw new exception("Could not create file :\n\t" + expressionString);
                }
            }
        // } else throw new exception("Expression is not a string.");
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
        return new openFileStatement(this.expression);
    }
}
