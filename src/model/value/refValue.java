package model.value;

import exception.exception;
import model.type.Type;
import model.type.refType;

public class refValue implements Value {
    private int adress;
    private Type valueType;

    public refValue(int adress, Type valueType) {
        this.adress = adress;
        this.valueType = valueType;
    }

    @Override
    public boolean equals(Value that) throws exception {
        if (that instanceof refValue) {
            return this.adress == ((refValue) that).getAdress();
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return "(" + this.adress + ", " + this.valueType.toString() + ")";

    }

    @Override
    public Type getType() {
        return new refType(this.valueType);
    }

    public int getAdress() {
        return this.adress;
    }

    public Type getValueType() {
        return this.valueType;
    }
}
