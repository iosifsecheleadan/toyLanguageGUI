package model.statement.executionStack;
import exception.exception;
import model.collection.map.mapInterface;
import model.collection.programState.programState;
import model.statement.Statement;
import model.type.Type;

public class noStatement implements Statement {
    @Override
    public String toString() {
        return "";
    }

    @Override
    public programState execute(programState state) throws exception {
        return null;
    }

    @Override
    public mapInterface<String, Type> typeCheck(mapInterface<String, Type> typeEnvironment) throws exception {
        return typeEnvironment;
    }

    @Override
    public Statement copy() {
        return new noStatement();
    }
}
