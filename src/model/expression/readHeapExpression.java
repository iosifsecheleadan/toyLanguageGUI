package model.expression;

import exception.exception;
import model.collection.map.mapInterface;
import model.type.Type;
import model.type.refType;
import model.value.Value;
import model.value.refValue;


public class readHeapExpression implements Expression {

    private Expression expression;

    public readHeapExpression(Expression expression) {
        this.expression = expression;
    }

    @Override
    public String toString() {
        return "readHeap(" + this.expression.toString() + ")";
    }

    @Override
    public Value evaluate(mapInterface<String, Value> symbolTable, mapInterface<Integer, Value> heap) throws exception {
        Value value = this.expression.evaluate(symbolTable, heap);
        //if (value instanceof refValue) {
            try {
                return heap.get( ((refValue) value).getAdress() );
            } catch (Exception exception) {
                throw new exception("Heap index out of range");
            }
        //} else throw new exception("Expression " + this.expression.toString() + " is not a reference");
    }

    @Override
    public Type typeCheck(mapInterface<String, Type> typeEnvironment) throws exception {
        if(this.expression.typeCheck(typeEnvironment) instanceof refType) {
            return ((refType) this.expression.typeCheck(typeEnvironment)).getInner();
        } else throw new exception("Expression " + this.expression.toString() + " is not a reference");

    }
}
