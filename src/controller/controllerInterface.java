package controller;

import exception.exception;
import model.collection.list.list;
import model.collection.programState.programState;
import model.statement.Statement;

public interface controllerInterface {
    void allSteps();
    void oneStep(list<programState> stateList) throws exception;
    void setDisplayFlag(boolean displayFlag);

    void addStatement(Statement statement);

    void clearAll();
    list<programState> removeCompletedProgramStates(list<programState> programStatelist);

    void close();
}
