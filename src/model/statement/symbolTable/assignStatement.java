package model.statement.symbolTable;

import exception.exception;
import model.collection.map.mapInterface;
import model.collection.programState.programState;
import model.expression.Expression;
import model.statement.Statement;
import model.type.Type;
import model.value.Value;

public class assignStatement implements Statement {
    private String identifier;
    private Expression expression;

    public assignStatement(String identifier, Expression expression) {
        this.identifier = identifier;
        this.expression = expression;
    }

    @Override
    public String toString() {
        return this.identifier + " = " + this.expression.toString();
    }

    @Override
    public programState execute (programState state) throws exception {
        mapInterface<String, Value> symbolTable = state.getSymbolTable();
        mapInterface<Integer, Value> heap = state.getHeap();

        Value value = null;
        try {
            value = this.expression.evaluate(symbolTable, heap);
        } catch (exception exception) {
            exception.printStackTrace();
        }
        //if (symbolTable.has(this.identifier)) {
            //Type identifierType = symbolTable.get(this.identifier).getType();
            //if (value.getType().equals(identifierType)) {
                symbolTable.put(this.identifier, value);
            //} else throw new exception("Declared type of variable " + this.identifier + " and type of the assigned expression do not match.");
        //} else throw new exception("The used variable " + this.identifier + " was not declared before.");
        return null;
    }

    @Override
    public mapInterface<String, Type> typeCheck(mapInterface<String, Type> typeEnvironment) throws exception {
        if (this.expression.typeCheck(typeEnvironment).equals(typeEnvironment.get(this.identifier))) {
            return typeEnvironment;
        } else throw new exception("Variable Type " + this.identifier + " doesn't match Expression Type " + this.expression.toString());
    }

    @Override
    public Statement copy() {
        return new assignStatement(this.identifier, this.expression);
    }
}
