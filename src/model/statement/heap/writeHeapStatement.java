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


public class writeHeapStatement implements Statement {
    private Expression expression;
    private String identifier;

    public writeHeapStatement(String identifier, Expression expression) {
        this.expression = expression;
        this.identifier = identifier;
    }

    @Override
    public String toString() {
        return "writeHeap(" + this.identifier + ", " + this.expression.toString() + ")";
    }


    @Override
    public programState execute(programState state) throws exception {
        mapInterface<String, Value> symbolTable = state.getSymbolTable();
        mapInterface<Integer, Value> heap = state.getHeap();

        //if (symbolTable.has(this.identifier)) {
            Value value = symbolTable.get(this.identifier);
            //if (value.getType() instanceof refType) {
                int valueAdress = ((refValue) value).getAdress();
                if (heap.has(valueAdress)) {
                    Value expressionValue = this.expression.evaluate(symbolTable, heap);
                    //if (expressionValue.getType().equals(((refType) value.getType()).getInner())) {
                        heap.put(valueAdress, expressionValue);
                    //} else throw new exception("Type of variable " + this.identifier + " does not match type of expression" + this.expression.toString());
                } else throw new exception("Heap index out of range");
            //} else throw new exception("Variable " + this.identifier + " is not a reference");
        //} else throw new exception("Variable " + this.identifier + " is not defined");
        return null;
    }

    @Override
    public mapInterface<String, Type> typeCheck(mapInterface<String, Type> typeEnvironment) throws exception {
        if (typeEnvironment.get(this.identifier).equals(new refType(this.expression.typeCheck(typeEnvironment)))) {
        //if (this.expression.typeCheck(typeEnvironment).equals(typeEnvironment.get(this.identifier))) {
            return typeEnvironment;
        } throw new exception("Variable Type " + this.identifier + " does not match Expressionn Type " + this.expression.toString());
    }

    @Override
    public Statement copy() {
        return new writeHeapStatement(this.identifier, this.expression);
    }
}
