package model.collection.list;
import exception.exception;

import java.util.Iterator;

public interface listInterface<Type> {
    boolean isEmpty();
    int size();
    listInterface sublist(int fromIndex, int toIndex) throws exception;
    void insert(Type element, int index);
    void remove(Type element) throws exception;
    void remove(int index) throws exception;
    Type get(int index) throws exception;
    Iterator<Type> iterator();
    boolean has(Type userInput);

    String toString();

    void append(Type element);
}
