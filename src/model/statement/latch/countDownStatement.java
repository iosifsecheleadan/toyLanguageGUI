package model.statement.latch;

import exception.exception;
import model.collection.list.listInterface;
import model.collection.map.mapInterface;
import model.collection.programState.programState;
import model.collection.stack.stackInterface;
import model.statement.Statement;
import model.type.Type;
import model.type.intType;
import model.value.Value;
import model.value.intValue;

public class countDownStatement implements Statement {
    private String identifier;

    public countDownStatement(String identifier) {
        this.identifier = identifier;
    }

    @Override
    public programState execute(programState state) throws exception {
        mapInterface<String, Value> symbolTable = state.getSymbolTable();
        mapInterface<Integer, intValue> latch = state.getLatch();
        listInterface<Value> output = state.getOutput();

        Integer foundIndex = ((intValue) symbolTable.get(this.identifier)).getValue();
        intValue latchValue = latch.get(foundIndex);
        if (latchValue.getValue() > 0) {
            latch.put(foundIndex, latchValue);
        }
        output.append(new intValue(state.getID()));
        return null;
    }

    @Override
    public mapInterface<String, Type> typeCheck(mapInterface<String, Type> typeEnvironment) throws exception {
        if (! typeEnvironment.get(this.identifier).equals(new intType())) {
            return typeEnvironment;
        } else throw new exception("Variable " + this.identifier + " is not of Type int.");
    }

    @Override
    public String toString() {
        return "countDown (" + this.identifier + ")";
    }

    @Override
    public Statement copy() {
        return new countDownStatement(this.identifier);
    }
}
