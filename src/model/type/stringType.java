package model.type;

import model.value.Value;
import model.value.stringValue;

public class stringType implements Type {
    @Override
    public boolean equals(Object that) {
        return that instanceof stringType;
    }

    @Override
    public String toString() {
        return "string";
    }

    @Override
    public Value getDefaultValue() {
        return new stringValue("");
    }
}