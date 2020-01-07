package controller;

import exception.exception;
import model.collection.list.list;
import model.collection.list.listInterface;
import model.collection.map.map;
import model.collection.map.mapInterface;
import model.collection.programState.programState;
import model.statement.Statement;
import model.value.Value;
import model.value.refValue;
import repository.repositoryInterface;

import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class controller implements controllerInterface {
    private boolean displayFlag;
    private repositoryInterface repo;
    ExecutorService executor;

    public controller(repositoryInterface repo) {
        this.repo = repo;
    }

    /*
    @Override
    public void allSteps() throws exception{
        // TO BE CHANGED
        programState state = this.repo.getCurrentProgram();
        this.repo.logNewProgram();
        this.repo.logProgramState(state);
        while (! state.complete()) {
            try {
                state.oneStep();
                this.garbageCollector(state);
                this.repo.logProgramState(state);
                //System.out.println(this.repo.getCurrentProgram().toString());
            } catch (exception ex) {
                System.out.println(ex.getMessage());
            }
        }
        this.repo.logProgramState(state);
    }
    */

    @Override
    public void allSteps() {
        try { this.repo.logNewProgram();
        } catch (exception ignored) {}
        this.executor = Executors.newFixedThreadPool(2);
        list<programState> stateList = this.removeCompletedProgramStates(this.repo.getProgramList());
        while (! stateList.isEmpty()) {
            try {
                this.oneStep(stateList);
            } catch (exception exception) {
                System.out.println(exception.getMessage());
            }
            this.garbageCollector(stateList);
            stateList = removeCompletedProgramStates(this.repo.getProgramList());
        }
        executor.shutdownNow();
        repo.setProgramList(stateList);
    }

    @Override
    public void oneStep(list<programState> stateList) throws exception {
        listInterface<programState> newStates = new list<programState>();
        for(Iterator<programState> it = stateList.iterator(); it.hasNext(); ){
            programState state = it.next();
            if (! state.complete()) {
                this.repo.logProgramState(state);
                programState newState = state.oneStep();
                if (newState != null) {
                    newStates.append(newState);
                }
            }
        }
        for(Iterator<programState> it = newStates.iterator(); it.hasNext(); ) {
            stateList.append(it.next());
        }
        this.repo.setProgramList(stateList);
    }

    @Override
    public void clearAll() {
        this.repo.clearAll();
        this.displayFlag = false;
    }

    @Override
    public list<programState> removeCompletedProgramStates(list<programState> stateList) {
        listInterface<programState> toRemove = new list<programState>();
        for (Iterator<programState> it = stateList.iterator(); it.hasNext(); ) {
            programState state = it.next();
            if (state.complete()) {
                toRemove.append(state);
            }
        }
        for(Iterator<programState> it = toRemove.iterator(); it.hasNext(); ) {
            try {
                stateList.remove(it.next());
            } catch (exception ignored) {}
        }
        return stateList;
    }

    @Override
    public void setDisplayFlag(boolean displayFlag) {
        this.displayFlag = displayFlag;
    }

    @Override
    public void addStatement(Statement statement) {
        this.repo.addStatement(statement);
    }

    @Override
    public void close() {
        this.repo.close();
    }


    @Override
    public void garbageCollector(list<programState> stateList) {
        list<Integer> adresses = this.getAdresses(stateList);
        programState state;
        for(Iterator<programState> it = stateList.iterator(); it.hasNext(); ) {
            state = it.next();
            state.setHeap(this.cleanHeap(adresses, state.getHeap()));
        }
    }

    private mapInterface<Integer, Value> cleanHeap(listInterface<Integer> adresses, mapInterface<Integer, Value> heap) {
        mapInterface<Integer, Value> newHeap = new map<Integer, Value>();
        for (Iterator<Integer> iterator = heap.getKeys().iterator(); iterator.hasNext(); ) {
            Integer adress = iterator.next();
            try {
                newHeap.put(adress, heap.get(adress));
            } catch (exception ignored) {}
        }
        return newHeap;
    }

    private list<Integer> getAdresses(listInterface<programState> stateList){
        list<Integer> adresses = new list<Integer>();
        for(Iterator<programState> it = stateList.iterator(); it.hasNext(); ) {
            programState state = it.next();
            for (Iterator<Value> iterator = state.getSymbolTable().getValues().iterator(); iterator.hasNext(); ) {
                try { adresses.append(((refValue) iterator.next()).getAdress());
                } catch (Exception ignored) {}
            }
            for (Iterator<Value> iterator = state.getHeap().getValues().iterator(); iterator.hasNext(); ) {
                try { adresses.append(((refValue) iterator.next()).getAdress());
                } catch (Exception ignored) {}
            }
        }
        return adresses;
    }
}
