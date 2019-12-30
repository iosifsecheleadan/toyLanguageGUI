package model.expression;

import exception.exception;
import model.collection.map.mapInterface;
import model.type.Type;
import model.type.intType;
import model.value.Value;
import model.value.intValue;

public class arithmeticExpression implements Expression {
    Expression first;
    Expression second;
    String operator; // '+', '-', '*', '/', '%'

    public arithmeticExpression(Expression first, Expression second, String operator) {
        this.first = first;
        this.second = second;
        this.operator = operator;
    }

    @Override
    public Value evaluate(mapInterface<String, Value> table, mapInterface<Integer, Value> heap) throws exception {
        Value firstValue = this.first.evaluate(table, heap);
        //if (firstValue instanceof intValue) {
            Value secondValue = this.second.evaluate(table, heap);
            //if(secondValue instanceof intValue) {
                int firstInt = ((intValue) firstValue).getValue(),
                        secondInt = ((intValue) secondValue).getValue();
                switch (this.operator) {
                    case "+" :
                        return new intValue(firstInt + secondInt);
                    case "-" :
                        return new intValue(firstInt - secondInt);
                    case "*" :
                        return new intValue(firstInt * secondInt);
                    case "/" :
                        if (secondInt == 0 ) throw new exception("division by 0");
                        return new intValue(firstInt / secondInt);
                    case "%" :
                        if (secondInt == 0 ) throw new exception("division by 0");
                        return new intValue(firstInt % secondInt);
                    default : throw new exception("Invalid operator");
                    }
            //} else throw new exception("second operand is not an integer");
        //} else throw new exception("first operand is not an integer");
    }

    @Override
    public Type typeCheck(mapInterface<String, Type> typeEnvironment) throws exception {
        if(this.first.typeCheck(typeEnvironment).equals(new intType())) {
            if (this.second.typeCheck(typeEnvironment).equals(new intType())){
                return new intType();
            } else throw new exception("Expression " + this.first.toString() + " is not an integer");
        } else throw new exception("Expression " + this.second.toString() + " is not an integer");
    }

    @Override
    public String toString() {
        return '(' + this.first.toString() +
                ' ' + this.operator +
                ' ' + this.second.toString() + ')';
    }
}
