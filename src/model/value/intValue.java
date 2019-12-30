package model.value;

import exception.exception;
import model.type.Type;
import model.type.intType;

public class intValue implements Value{
    private int value;

    public intValue(int value) {
        this.value = value;
    }

    public intValue() {
        this.value = 0;
    }

    public int getValue() {
        return this.value;
    }

    @Override
    public String toString() {
        return String.valueOf(this.value);
    }

    @Override
    public Type getType() {
        return new intType();
    }

    @Override
    public boolean equals(Value that) throws exception {
        if (that instanceof intValue) {
            return this.getValue() == ((intValue) that).getValue();
        } else {
            throw new  exception("Comparison between incompatible types.");
        }
    }
}
