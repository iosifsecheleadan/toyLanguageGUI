package model.statement.latch;

import exception.exception;
import model.collection.map.mapInterface;
import model.collection.programState.programState;
import model.expression.Expression;
import model.statement.Statement;
import model.type.Type;
import model.type.intType;
import model.value.Value;
import model.value.intValue;
import model.value.refValue;

public class newLatchStatement implements Statement {
    String identifier;
    Expression expression;

    public newLatchStatement(String identifier, Expression expression) {
        this.identifier = identifier;
        this.expression = expression;
    }

    @Override
    public programState execute(programState state) throws exception {
        mapInterface<String, Value> symbolTable = state.getSymbolTable();
        mapInterface<Integer, Value> heap = state.getHeap();

        intValue expressionValue = (intValue) this.expression.evaluate(symbolTable, heap);
        int address = state.insertToLatch(expressionValue);
        symbolTable.put(this.identifier, new refValue(address - 1, expressionValue.getType()));

        return null;
    }

    @Override
    public mapInterface<String, Type> typeCheck(mapInterface<String, Type> typeEnvironment) throws exception {
        if (typeEnvironment.get(this.identifier).equals(new intType())) {
            if (typeEnvironment.get(this.identifier).equals(this.expression.typeCheck(typeEnvironment))) {
                return typeEnvironment;
            } else throw new exception("Variable Type " + this.identifier + " does not match Expression Type " + this.expression.toString());
        } else throw new exception("Variable Type " + this.identifier + " is not int.");
    }

    @Override
    public String toString() {
        return "newLatch " + this.identifier + " = " + this.expression.toString();
    }

    @Override
    public Statement copy() {
        return new newLatchStatement(this.identifier, this.expression);
    }
}
