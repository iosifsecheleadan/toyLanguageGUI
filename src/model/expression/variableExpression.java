package model.expression;

import exception.exception;
import model.collection.map.mapInterface;
import model.type.Type;
import model.value.Value;

public class variableExpression implements Expression {
    private String identifier;

    public variableExpression(String identifier) {
        this.identifier = identifier;
    }

    @Override
    public Value evaluate(mapInterface<String, Value> table, mapInterface<Integer, Value> heap) throws exception {
        return table.get(this.identifier);
    }

    @Override
    public Type typeCheck(mapInterface<String, Type> typeEnvironment) throws exception {
        return typeEnvironment.get(this.identifier);
    }

    @Override
    public String toString() {
        return this.identifier.toString();
    }
}
