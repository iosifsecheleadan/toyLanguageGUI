package model.statement.executionStack;

import exception.exception;
import model.collection.map.mapInterface;
import model.collection.programState.programState;
import model.collection.stack.stackInterface;
import model.expression.Expression;
import model.statement.Statement;
import model.statement.symbolTable.assignStatement;
import model.type.Type;
import model.type.boolType;

public class conditionalAssignStatement implements Statement {
    private String identifier;
    private Expression condition;
    private Expression thenExpression;
    private Expression elseExpression;

    public conditionalAssignStatement(String identifier, Expression condition, Expression thenExpression, Expression elseExpression) {
        this.identifier = identifier;
        this.condition = condition;
        this.thenExpression = thenExpression;
        this.elseExpression = elseExpression;
    }

    @Override
    public programState execute(programState state) throws exception {
        stackInterface<Statement> stack = state.getExecutionStack();
        stack.push(
            new ifStatement(this.condition,
                new assignStatement(this.identifier, this.thenExpression),
                new assignStatement(this.identifier, this.elseExpression)
            )
        );
        return null;
    }

    @Override
    public mapInterface<String, Type> typeCheck(mapInterface<String, Type> typeEnvironment) throws exception {
        if (this.condition.typeCheck(typeEnvironment).equals(new boolType())) {
            Type identifierType = typeEnvironment.get(this.identifier);
            if (this.thenExpression.typeCheck(typeEnvironment).equals(identifierType)) {
                if (this.elseExpression.typeCheck(typeEnvironment).equals(identifierType)) {
                    return typeEnvironment;
                } else throw new exception("Variable Type " + this.identifier + " doesn't match Expression Type " + this.elseExpression.toString());
            } else throw new exception("Variable Type " + this.identifier + " doesn't match Expression Type " + this.thenExpression.toString());
        } else throw new exception("Expression " + this.condition.toString() + " is not a boolean.");
    }

    @Override
    public String toString() {
        return identifier + " = "
                + this.condition.toString() + " ? "
                + this.thenExpression.toString() + " : "
                + this.elseExpression.toString();
    }

    @Override
    public Statement copy() {
        return new conditionalAssignStatement(this.identifier, this.condition, this.thenExpression, this.elseExpression);
    }
}
