package model.collection.stack;

public interface stackInterface<Type> {
    int size();
    Type pop();
    void push(Type element);

    String toString();
}
