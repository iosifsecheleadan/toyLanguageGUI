package model.statement.symbolTable;

import exception.exception;
import model.collection.map.mapInterface;
import model.collection.programState.programState;
import model.statement.Statement;
import model.type.Type;
import model.value.Value;

public class declarationStatement implements Statement {
    private String identifier;
    private Type type;

    public declarationStatement(String identifier, Type type) {
        this.identifier = identifier;
        this.type = type;
    }

    @Override
    public String toString() {
        return this.type + " " + this.identifier;
    }

    @Override
    public programState execute(programState state) throws exception{
        mapInterface<String, Value> symbolTable = state.getSymbolTable();
        //if (!symbolTable.has(this.identifier)) {
            symbolTable.put(this.identifier, this.type.getDefaultValue());
        //} else throw new exception("Variable " + this.identifier + " is already defined");
        return null;
    }

    @Override
    public mapInterface<String, Type> typeCheck(mapInterface<String, Type> typeEnvironment) throws exception {
        if(! typeEnvironment.has(this.identifier)) {
            typeEnvironment.put(this.identifier, this.type);
            return typeEnvironment;
        } else throw new exception("Variable " + this.identifier + " is already declared");
    }

    @Override
    public Statement copy() {
        return new declarationStatement(this.identifier, this.type);
    }
}
