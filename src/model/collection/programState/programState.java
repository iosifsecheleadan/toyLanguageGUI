package model.collection.programState;

import exception.exception;
import model.collection.list.list;
import model.collection.list.listInterface;
import model.collection.map.map;
import model.collection.map.mapInterface;
import model.collection.stack.stack;
import model.collection.stack.stackInterface;
import model.statement.Statement;
import model.value.Value;

import java.io.BufferedReader;
import java.util.concurrent.atomic.AtomicInteger;


public class programState {
    private int heapIndex;
    private int thisID;
    private static AtomicInteger id = new AtomicInteger(0);

    private stackInterface<Statement> executionStack;
    private mapInterface<String, Value> symbolTable;
    private listInterface<Value> output;
    private mapInterface<String, BufferedReader> fileTable;
    private mapInterface<Integer, Value> heap;

    private Statement originalProgram;

    public programState(Statement originalProgram) {
        this.heapIndex = 1;

        this.executionStack = new stack<Statement>();
        this.symbolTable = new map<String, Value>();
        this.output = new list<Value>();
        this.fileTable = new map<String, BufferedReader>();
        this.heap = new map<Integer, Value>();

        this.originalProgram = originalProgram.copy();
        executionStack.push(this.originalProgram);
        this.thisID = this.id.getAndIncrement();
    }

    public programState(stackInterface<Statement> executionStack,
                        mapInterface<String, Value> symbolTable,
                        listInterface<Value> output,
                        mapInterface<String, BufferedReader> fileTable,
                        mapInterface<Integer, Value> heap,
                        Statement originalProgram) {
        this.heapIndex = 1;

        this.executionStack = executionStack;
        this.symbolTable = symbolTable;
        this.output = output;
        this.fileTable = fileTable;
        this.heap = heap;

        this.originalProgram = originalProgram.copy();
        executionStack.push(this.originalProgram);
        this.thisID = this.id.getAndIncrement();
    }

    public String toString() {
        return  "Program ID : " + this.thisID + "\n" +
                "Execution Stack :\n" + this.executionStack.toString() + "\n" +
                "Symbol Table :\n" + this.symbolTable.toString() + "\n" +
                "Output :\n" + this.output.toString() + "\n" +
                "File Table :\n" + this.fileTable.toString() + "\n" +
                "Heap : \n" + this.heap.toString() + "\n\n";
    }

    public stackInterface<Statement> getExecutionStack() {
        return this.executionStack;
    }

    public mapInterface<String, Value> getSymbolTable() {
        return this.symbolTable;
    }

    public listInterface<Value> getOutput() {
        return this.output;
    }

    public mapInterface<String, BufferedReader> getFileTable() {
        return this.fileTable;
    }

    public mapInterface<Integer, Value> getHeap ()
    {
        return this.heap;
    }

    public int insertToHeap(Value value) {
        this.heap.put(this.heapIndex, value);
        this.heapIndex += 1;
        return this.heapIndex;
    }

    public void setHeap(mapInterface<Integer, Value> heap) {
        this.heap = heap;
    }

    public Boolean complete() {
        return this.executionStack.size() == 0;
    }

    public programState oneStep() throws exception {
        if (this.executionStack.size() == 0) {
            throw new exception("program state execution stack is empty");
        }
        Statement current = this.executionStack.pop();
        return current.execute(this);
    }

    public int getID() {
        return this.thisID;
    }

    //set any field
}
