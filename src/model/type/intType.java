package model.type;

import model.value.Value;
import model.value.intValue;

public class intType implements Type{
    @Override
    public boolean equals(Object that) {
        return that instanceof intType;
    }

    @Override
    public String toString() {
        return "int";
    }

    @Override
    public Value getDefaultValue() {
        return new intValue(0);
    }
}
