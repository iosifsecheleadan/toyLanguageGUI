package model.value;

import exception.exception;
import model.type.Type;

public interface Value {
    String toString();
    boolean equals (Value that) throws exception;
    Type getType();
}
