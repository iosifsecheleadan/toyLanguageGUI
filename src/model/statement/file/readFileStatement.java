package model.statement.file;

import exception.exception;
import model.collection.map.mapInterface;
import model.collection.programState.programState;
import model.expression.Expression;
import model.statement.Statement;
import model.type.Type;
import model.type.intType;
import model.type.stringType;
import model.value.Value;
import model.value.intValue;
import model.value.stringValue;

import java.io.BufferedReader;
import java.io.IOException;

public class readFileStatement implements Statement {
    private String identifier;
    private Expression expression;

    public readFileStatement(String identifier, Expression expression) {
        this.identifier = identifier;
        this.expression = expression;
    }

    @Override
    public String toString() {
        return "readFile(" + this.expression.toString() + ", " + this.identifier + ")";
    }

    @Override
    public programState execute(programState state) throws exception {
        mapInterface<String, Value> symbolTable = state.getSymbolTable();
        mapInterface<String, BufferedReader> fileTable = state.getFileTable();
        mapInterface<Integer, Value> heap = state.getHeap();

        //if (symbolTable.has(this.identifier)) {
            Type identifierType = symbolTable.get(this.identifier).getType();
            //if (identifierType.equals(new intType())) {
                Value expressionValue = null;
                try {
                    expressionValue = this.expression.evaluate(symbolTable, heap);
                } catch (exception exception) {
                    exception.printStackTrace();
                }
                //if(expressionValue.getType().equals(new stringType())) {
                    String expressionString = ((stringValue) expressionValue).getValue();
                    //if (fileTable.has(expressionString)) {
                        BufferedReader reader = fileTable.get(expressionString);
                        try {
                            String line = reader.readLine();
                            Value lineInt = new intValue(Integer.parseInt(line));
                            symbolTable.put(this.identifier, lineInt);
                        } catch (IOException e) {
                            throw new exception("Could not read from file " + expressionString);
                        } catch (NumberFormatException ex) {
                                symbolTable.put(expressionString, new intValue());
                        }
                    //} else throw new exception("File " + expressionString + " is not open.");
                //} else throw new exception("Expression " + this.expression.toString() + " must be of type string.");
            //} else throw new exception("Type of variable " + this.identifier + " must be int.");
        //} else throw new exception("The used variable " +  this.identifier + " was not declared before.");
        return null;
    }

    @Override
    public mapInterface<String, Type> typeCheck(mapInterface<String, Type> typeEnvironment) throws exception {
        if(typeEnvironment.get(this.identifier).equals(new intType())) {
            if(this.expression.typeCheck(typeEnvironment).equals(new stringType())) {
                return typeEnvironment;
            } throw new exception("Expression " + this.expression.toString() + " is not a string");
        } throw new exception("Variable " + this.identifier + " is not an integer");
    }

    @Override
    public Statement copy() {
        return new readFileStatement(this.identifier, this.expression);
    }
}
