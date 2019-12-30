package model.type;

import model.value.Value;
import model.value.refValue;

public class refType implements Type {
    Type inner;


    public refType(Type inner) {
        this.inner = inner;
    }

    public Type getInner() {
        return this.inner;
    }

    @Override
    public boolean equals(Object that) {
        if (that instanceof refType) {
            return this.inner.equals(((refType) that).getInner());
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return "ref(" + this.inner.toString() + ")";
    }

    @Override
    public Value getDefaultValue() {
        return new refValue(0, inner);
    }

}
