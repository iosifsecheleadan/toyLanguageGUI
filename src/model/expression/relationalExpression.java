package model.expression;

import exception.exception;
import model.collection.map.mapInterface;
import model.type.Type;
import model.type.boolType;
import model.type.intType;
import model.value.Value;
import model.value.boolValue;
import model.value.intValue;

public class relationalExpression implements Expression {
    private Expression first;
    private Expression second;
    private String operator;

    public relationalExpression(Expression first, Expression second, String operator) {
        this.first = first;
        this.second = second;
        this.operator = operator;
    }


    @Override
    public Value evaluate(mapInterface<String, Value> table, mapInterface<Integer, Value> heap) throws exception {
        Value firstValue = this.first.evaluate(table, heap);
        //if(firstValue.getType().equals(new intType())) {
            Value secondValue = this.second.evaluate(table, heap);
            //if (secondValue.getType().equals(new intType())) {
                int firstInteger = ((intValue) firstValue).getValue();
                int secondInteger = ((intValue) secondValue).getValue();
                switch (this.operator) {
                    case "<" :
                        return new boolValue(firstInteger < secondInteger);
                    case "<=" :
                        return new boolValue(firstInteger <= secondInteger);
                    case "==" :
                        return new boolValue(firstInteger == secondInteger);
                    case "!=" :
                        return new boolValue(firstInteger != secondInteger);
                    case ">" :
                        return new boolValue(firstInteger > secondInteger);
                    case ">=":
                        return new boolValue(firstInteger >= secondInteger);
                    default: return new boolValue(false); // never called
                }
            //} else throw new exception("second operand is not an integer");
        //} else throw new exception("first operand is not an integer");
    }

    @Override
    public Type typeCheck(mapInterface<String, Type> typeEnvironment) throws exception {
        if (this.first.typeCheck(typeEnvironment).equals(new intType())) {
            if (this.second.typeCheck(typeEnvironment).equals(new intType())) {
                return new boolType();
            } else throw new exception("Expression " + this.first.toString() + " is not an integer");
        } else throw new exception("Expression " + this.second.toString() + " is not an integer");
    }
}
