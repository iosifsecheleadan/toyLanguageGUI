package model.collection.list;

import exception.exception;
import javafx.collections.ObservableList;

import javax.naming.CompositeName;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

public class list<Type> implements listInterface<Type> {
    private CopyOnWriteArrayList<Type> list;
    //private ArrayList<Type> list;

    public list(CopyOnWriteArrayList<Type> newList){
        this.list = newList;
    }

    public list() {
        this.list = new CopyOnWriteArrayList<Type>();
    }

    @Override
    public boolean isEmpty() {
        return this.list.isEmpty();
    }

    @Override
    public int size() {
        return this.list.size();
    }

    @Override
    public listInterface<Type> sublist(int fromIndex, int toIndex) throws exception{
        if (fromIndex > 0 && toIndex < this.list.size()) {
            return new list<Type>((CopyOnWriteArrayList<Type>) this.list.subList(fromIndex, toIndex));
        } else throw new exception ("index out of bounds");
    }

    @Override
    public synchronized void insert(Type element, int index) {
        this.list.add(index, element);
    }

    @Override
    public synchronized void remove(Type element) throws exception {
        if (this.list.contains(element)) {
            this.list.remove(element);
        } else throw new exception (element + " not in list");
    }

    @Override
    public synchronized void remove(int index) throws exception {
        if (index < this.list.size()) {
            this.remove(index);
        } else throw new exception ("index out of bounds");
    }

    @Override
    public Type get(int index) throws exception {
        if (index < this.list.size()) {
            return this.list.get(index);
        } throw new exception ("index out of bounds");
    }

    @Override
    public String toString() {
        return this.list.toString();
    }

    @Override
    public synchronized void append(Type element) {
        this.list.add(element);
    }

    @Override
    public ArrayList<Type> toArrayList() {
        return new ArrayList<Type>(this.list);
    }

    @Override
    public boolean has(Type userInput) {
        return this.list.contains(userInput);
    }

    @Override
    public Iterator<Type> iterator() {
        return this.list.iterator();
    }
}
