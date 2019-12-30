package repository;

import exception.exception;
import model.collection.list.list;
import model.collection.programState.programState;
import model.statement.Statement;


public interface repositoryInterface {
    programState getCurrentProgram() throws exception;
    list<programState> getProgramList();
    void setProgramList(list<programState> threads);
    void addStatement(Statement statement);
    void setCurrentProgramState(programState state);
    void clearAll();
    void logProgramState(programState state) throws exception;

    void close();

    void logNewProgram() throws exception;
}
