package model.collection.stack;

import java.util.ArrayList;

public interface stackInterface<Type> {
    int size();
    Type pop();
    void push(Type element);

    String toString();

    ArrayList<Type> toArrayList();
}
