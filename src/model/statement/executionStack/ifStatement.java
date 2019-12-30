package model.statement.executionStack;
import exception.exception;
import model.collection.map.mapInterface;
import model.collection.programState.programState;
import model.collection.stack.stackInterface;
import model.expression.Expression;
import model.statement.Statement;
import model.type.Type;
import model.type.boolType;
import model.value.Value;
import model.value.boolValue;

public class ifStatement implements Statement {
    private Expression expression;
    private Statement thenStatement;
    private Statement elseStatement;

    public ifStatement(Expression expression, Statement thenStatement, Statement elseStatement) {
        this.expression = expression;
        this.thenStatement = thenStatement;
        this.elseStatement = elseStatement;
    }

    @Override
    public String toString() {
        return "if (" + expression.toString() + ") then (" + thenStatement.toString() +
                ") else (" + elseStatement.toString() + ")";
    }

    @Override
    public programState execute (programState state) throws exception {
        mapInterface<String, Value> symbolTable = state.getSymbolTable();
        stackInterface<Statement> stack = state.getExecutionStack();
        mapInterface<Integer, Value> heap = state.getHeap();
        Value value = null;
        try {
            value = this.expression.evaluate(symbolTable, heap);
        } catch (exception exception) {
            exception.printStackTrace();
            return state;
        }
        //if(value.getType() instanceof boolType) {
            if (value.equals(new boolValue(true))) {
                stack.push(this.thenStatement);
                //stack.push(this.elseStatement);
            } else {
                //stack.push(this.thenStatement);
                stack.push(this.elseStatement);
            }
        //} else throw new exception("Conditional expression not a boolean");
        return null;
    }

    @Override
    public mapInterface<String, Type> typeCheck(mapInterface<String, Type> typeEnvironment) throws exception {
        if (this.expression.typeCheck(typeEnvironment).equals(new boolType())) {
            this.thenStatement.typeCheck(typeEnvironment.copy());
            this.elseStatement.typeCheck(typeEnvironment.copy());
            return typeEnvironment;
        } else throw new exception("Expression " + this.expression.toString() + " is not a boolean");
    }

    @Override
    public Statement copy() {
        return new ifStatement(this.expression, this.thenStatement, this.elseStatement);
    }
}
