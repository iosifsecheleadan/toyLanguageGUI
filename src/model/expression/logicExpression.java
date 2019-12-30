package model.expression;

import exception.exception;
import model.collection.map.mapInterface;
import model.type.Type;
import model.type.boolType;
import model.value.Value;
import model.value.boolValue;

public class logicExpression implements Expression {
    private Expression first;
    private Expression second;
    private String operator;

    public logicExpression(Expression first, Expression second, String operator) {
        this.first = first;
        this.second = second;
        this.operator = operator;
    }

    @Override
    public Value evaluate(mapInterface<String, Value> table, mapInterface<Integer, Value> heap) throws exception {
        Value firstValue = this.first.evaluate(table, heap);
        //if (firstValue.getType().equals(new boolType())) {
            Value secondValue = this.second.evaluate(table, heap);
            //if (secondValue.getType().equals(new boolType())) {
                switch (this.operator) {
                    case "and":
                        return new boolValue( ((boolValue) firstValue).getValue()
                                && ((boolValue) secondValue).getValue() );
                    case "or" :
                        return new boolValue( ((boolValue) firstValue).getValue()
                                || ((boolValue) secondValue).getValue() );
                    default : throw new exception("Invalid operator.");
                }
            //} else throw new exception("Second operand is not a boolean");
        //} else throw new exception("Fist operand is not a boolean");
    }

    @Override
    public Type typeCheck(mapInterface<String, Type> typeEnvironment) throws exception {
        if(this.first.typeCheck(typeEnvironment).equals(new boolType())) {
            if (this.second.typeCheck(typeEnvironment).equals(new boolType())){
                return new boolType();
            } else throw new exception("Expression " + this.first.toString() + " is not a boolean");
        } else throw new exception("Expression " + this.second.toString() + " is not a boolean");
    }

    @Override
    public String toString() {
        return '(' + this.first.toString() +
                ' ' + this.operator +
                ' ' + this.second.toString() + ')';
    }
}
