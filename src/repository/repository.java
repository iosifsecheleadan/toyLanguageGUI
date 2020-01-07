package repository;

import exception.exception;
import model.collection.list.list;
import model.collection.programState.programState;
import model.collection.stack.stackInterface;
import model.statement.Statement;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Scanner;

public class repository implements repositoryInterface {
    private Writer log;
    private list<programState> threads;
    private String logFile;

    public repository() {
        this.threads = new list<programState>();
        //this.logFile = "/home/sechelea/Documents/programming/projects/java/advanced programming methods/toyLanguage/";
        this.logFile = "";
        this.readLogFile();
        try {
            this.log = new FileWriter(this.logFile, true);
            this.newLog();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public repository(String logFile) {
        this.threads = new list<programState>();
        this.logFile = logFile;
        try {
            this.log = new FileWriter(this.logFile, true);
            this.newLog();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void newLog() throws IOException {
        String newLog = "\n\n-----------------------------------------------------------------\n" +
                "-------------------- " +
                new SimpleDateFormat("E yyyy-MM-dd HH:mm:ss").format(new Date()) +
                    " --------------------\n" +
                "-----------------------------------------------------------------\n\n";
        this.log.append(newLog);
    }

    @Override
    public programState getCurrentProgram() throws exception{
        return this.threads.get(0);
    }

    @Override
    public programState getProgram(Integer ID) throws exception {
        for(Iterator<programState> it = this.threads.iterator(); it.hasNext(); ) {
            programState current = it.next();
            if (ID.equals(current.getID())) {
                return current;
            }
        }
        throw new exception("No program with ID " + ID.toString());
    }

    @Override
    public list<programState> getProgramList() {
        return this.threads;
    }

    @Override
    public void setProgramList(list<programState> threads) {
        this.threads = threads;
    }

    @Override
    public void addStatement(Statement statement) {
        try {
            stackInterface<Statement> stack = this.getCurrentProgram().getExecutionStack();
            stack.push(statement);
        } catch (exception ex) {
            this.threads.append(new programState(statement));
        }
    }

    @Override
    public void clearAll() {
        this.threads = new list<programState>();
    }

    @Override
    public void logProgramState(programState state) throws exception {
        try {
            this.log.append(state.toString());
        } catch (IOException e) {
            throw new exception("could not write to file at :\n\t" + this.logFile);
        }
    }

    @Override
    public void close() {
        try {
            this.log.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void logNewProgram() throws exception {
        try {
            this.log.append("\n-----------------------------------------------------------------\n\n\n");
        } catch (IOException e) {
            throw new exception("could not write to file at :\n\t" + this.logFile);
        }
    }

    @Override
    public void setCurrentProgramState(programState state){
        try {
            this.threads.remove(0);
        } catch (exception ignored){}
        this.threads.insert(state, 0);
    }

    private void readLogFile() {
        System.out.println("Please give the log file name.\nIt will be at location :\n\t\" " + this.logFile + " \"");
        Scanner console = new Scanner(System.in);
        this.logFile += console.nextLine();
    }
}
