package model.collection.map;

import exception.exception;
import model.collection.list.list;

import java.util.HashMap;

public interface mapInterface<Key, Value> {
    int size();
    void put(Key key, Value value);
    void remove(Key key) throws exception;
    Value get(Key key) throws exception;
    list<Key> getKeys();
    list<Value> getValues();
    boolean has(Key identifier);
    String toString();
    mapInterface<Key, Value> copy();
    HashMap<Key, Value> toHashMap();
}
