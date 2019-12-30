package model.expression;

import exception.exception;
import model.collection.map.mapInterface;
import model.type.Type;
import model.value.Value;

public class valueExpression implements Expression {
    private Value value;

    public valueExpression(Value value) {
        this.value = value;
    }

    @Override
    public Value evaluate(mapInterface<String, Value> table, mapInterface<Integer, Value> heap) throws exception {
        return this.value;
    }

    @Override
    public Type typeCheck(mapInterface<String, Type> typeEnvironment) throws exception {
        return value.getType();
    }

    @Override
    public String toString() {
        return this.value.toString();
    }
}
