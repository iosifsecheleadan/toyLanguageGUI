package model.type;

import model.value.Value;

public interface Type {
    String toString();
    boolean equals(Object that);
    Value getDefaultValue();
}
