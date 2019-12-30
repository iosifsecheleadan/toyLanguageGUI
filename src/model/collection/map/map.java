package model.collection.map;

import exception.exception;
import model.collection.list.list;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class map<Key, Value> implements mapInterface<Key, Value> {
    private ConcurrentHashMap<Key, Value> map;

    public map() {
        this.map = new ConcurrentHashMap<Key, Value>();
    }

    public map(ConcurrentHashMap<Key, Value>  newMap) {
        this.map = newMap;
    }

    @Override
    public int size() {
        return this.map.size();
    }

    @Override
    public synchronized void put(Key key, Value value) {
        this.map.put(key, value);
    }

    @Override
    public synchronized void remove(Key key) throws exception {
        try {
            this.map.remove(key);
        } catch (Exception ex) {
            throw new exception("Inexisting key.");
        }
    }

    @Override
    public Value get(Key key) throws exception {
        try {
            return this.map.get(key);
        } catch (Exception ex) {
            throw new exception("Inexisting key.");
        }
    }

    @Override
    public list<Key> getKeys() {
        return new list<Key>(new CopyOnWriteArrayList<Key>(this.map.keySet()));
    }

    @Override
    public list<Value> getValues() {
        return new list<Value>(new CopyOnWriteArrayList<Value>(this.map.values()));
    }

    @Override
    public boolean has(Key identifier) {
        return this.map.containsKey(identifier);
    }

    @Override
    public String toString() {
        return this.map.toString();
    }

    @Override
    public model.collection.map.map<Key, Value> copy() {
        ConcurrentHashMap<Key, Value> newMap = new ConcurrentHashMap<Key, Value>();
        for (Key key : this.map.keySet()) {
            newMap.put(key, this.map.get(key));
        }
        return new map<Key, Value>(newMap);
    }
}
