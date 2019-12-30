package model.collection.stack;

import java.util.Stack;

public class stack<Type> implements stackInterface<Type> {
    private Stack<Type> stack;

    public stack() {
        this.stack = new Stack<Type>();
    }

    @Override
    public int size() {
        return this.stack.size();
    }

    @Override
    public Type pop() {
        return this.stack.pop();
    }

    @Override
    public void push(Type element) {
        this.stack.push(element);
    }

    @Override
    public String toString() {
        return this.stack.toString();
    }
}
