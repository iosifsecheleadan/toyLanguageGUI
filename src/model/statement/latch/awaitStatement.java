package model.statement.latch;

import exception.exception;
import model.collection.map.mapInterface;
import model.collection.programState.programState;
import model.statement.Statement;
import model.type.Type;
import model.type.intType;
import model.value.Value;
import model.value.intValue;

public class awaitStatement implements Statement {
    String identifier;

    public awaitStatement(String identifier) {
        this.identifier = identifier;
    }

    @Override
    public programState execute(programState state) throws exception {
        mapInterface<String, Value> symbolTable = state.getSymbolTable();
        mapInterface<Integer, intValue> latchTable = state.getLatch();

        Integer foundIndex = ((intValue) symbolTable.get(this.identifier)).getValue();
        if(latchTable.has(foundIndex)) {
            if (! latchTable.get(foundIndex).equals(new intValue(0))) {
                state.getExecutionStack().push(this.copy());
            }
        } else throw new exception("Latch does not have value for " + this.identifier);
        return null;
    }

    @Override
    public mapInterface<String, Type> typeCheck(mapInterface<String, Type> typeEnvironment) throws exception {
        if (typeEnvironment.get(this.identifier).equals(new intType())) {
            return typeEnvironment;
        } throw new exception("Type of Variable " + this.identifier + " is not int.");
    }

    @Override
    public String toString() {
        return "await " + this.identifier;
    }

    @Override
    public Statement copy() {
        return new awaitStatement(this.identifier);
    }
}
