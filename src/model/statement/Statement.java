package model.statement;

import exception.exception;
import model.collection.map.mapInterface;
import model.collection.programState.programState;
import model.type.Type;

public interface Statement {
    programState execute(programState state) throws exception;
    mapInterface<String, Type> typeCheck (mapInterface<String, Type> typeEnvironment) throws exception;
    String toString();
    Statement copy();
}
