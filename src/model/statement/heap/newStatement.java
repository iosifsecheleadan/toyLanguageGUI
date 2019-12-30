package model.statement.heap;

import exception.exception;
import model.collection.map.mapInterface;
import model.collection.programState.programState;
import model.expression.Expression;
import model.statement.Statement;
import model.type.Type;
import model.type.refType;
import model.value.Value;
import model.value.refValue;

public class newStatement implements Statement {
    private String identifier;
    private Expression expression;

    public newStatement(String identifier, Expression expression) {
        this.identifier = identifier;
        this.expression = expression;
    }

    @Override
    public String toString() {
        return "new " + this.identifier + " = " + this.expression.toString();
    }

    @Override
    public programState execute(programState state) throws exception {
        mapInterface<String, Value> symbolTable = state.getSymbolTable();
        mapInterface<Integer, Value> heap = state.getHeap();

        //if(symbolTable.has(this.identifier)) {
            //Value idValue = symbolTable.get(this.identifier);
            //if(idValue.getType() instanceof refType) {
                Value expressionValue = this.expression.evaluate(symbolTable, heap);
                //if ( expressionValue.getType().equals( ((refType) idValue.getType()).getInner() ) ) {
                    int adress = state.insertToHeap(expressionValue);
                    symbolTable.put(this.identifier, new refValue(adress - 1, expressionValue.getType()));
                //} else throw new exception("Type of variable " + this.identifier + " does not match type of expression " + this.expression.toString());
            //} else throw new exception("Variable " + this.identifier + " is not a reference.");
        //} else throw new exception("Variable " + this.identifier + " is not defined.");
        return null;
    }

    @Override
    public mapInterface<String, Type> typeCheck(mapInterface<String, Type> typeEnvironment) throws exception {
        if (typeEnvironment.get(this.identifier).equals(new refType(this.expression.typeCheck(typeEnvironment)))) {
            return typeEnvironment;
        } else throw new exception("Variable Type " + this.identifier + " does not match Expression Type " + this.expression.toString());
    }

    @Override
    public Statement copy() {
        return new newStatement(this.identifier, this.expression);
    }
}
