package model.value;

import exception.exception;
import model.type.Type;
import model.type.stringType;

public class stringValue implements Value {
    private String value;

    public stringValue(String value) {
        this.value = value;
    }

    public stringValue() {
        this.value = "";
    }

    public String getValue() {
        return this.value;
    }

    @Override
    public String toString() {
        return this.value;
    }

    @Override
    public Type getType() {
        return new stringType();
    }

    @Override
    public boolean equals(Value that) throws exception {
        if (that instanceof stringValue) {
            return this.getValue().equals(((stringValue) that).getValue());
        } else {
            throw new exception("Comparison between incompatible types.");
        }
    }
}
