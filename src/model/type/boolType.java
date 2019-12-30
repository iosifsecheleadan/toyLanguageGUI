package model.type;

import model.value.Value;
import model.value.boolValue;

public class boolType implements Type{
    @Override
    public boolean equals(Object that) {
        return that instanceof boolType;
    }

    @Override
    public String toString() {
        return "bool";
    }

    @Override
    public Value getDefaultValue() {
        return new boolValue(false);
    }
}
