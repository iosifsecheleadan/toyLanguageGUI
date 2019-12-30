package view.command;

import controller.controllerInterface;
import exception.exception;
import model.collection.map.map;
import model.expression.*;
import model.statement.Statement;
import model.statement.executionStack.compoundStatement;
import model.statement.executionStack.forkStatement;
import model.statement.executionStack.ifStatement;
import model.statement.executionStack.whileStatement;
import model.statement.file.closeFileStatement;
import model.statement.file.openFileStatement;
import model.statement.file.readFileStatement;
import model.statement.heap.newStatement;
import model.statement.heap.writeHeapStatement;
import model.statement.symbolTable.assignStatement;
import model.statement.symbolTable.declarationStatement;
import model.statement.symbolTable.printStatement;
import model.type.*;
import model.value.boolValue;
import model.value.intValue;
import model.value.stringValue;

import java.util.Scanner;

public class inputCommand extends Command {
    Scanner console;

    public inputCommand(String key, String description, controllerInterface ctrl) {
        super(key, description, ctrl);
        this.console = new Scanner(System.in);
    }

    @Override
    public void execute() throws exception{
        this.ctrl.clearAll();
        try {
            Statement originalProgram = this.input();
            try { originalProgram.typeCheck(new map<String, Type>());
            } catch (exception ex) {
                System.out.println(ex.getMessage());
                return;
            }
            this.ctrl.addStatement(originalProgram);
        } catch (exception exception) {
            if (exception.getMessage().equals("exit")) {
                return;
            } else {
                throw exception;
            }
        }
    }

    private Statement input() throws exception {
        while (true) {
            System.out.print(">>");
            String userInput = this.console.nextLine();
            switch (userInput) {
                case "1":
                    return this.firstProgram();
                case "2":
                    return this.secondProgram();
                case "3":
                    return this.thirdProgram();
                case "4":
                    return this.fourthProgram();
                case "5":
                    return this.fifthProgram();
                case "6":
                    return this.sixthProgram();
                case "7":
                    return this.seventhProgram();
                case "8":
                    return this.eigthProgram();
                case "9":
                    return this.ninthProgram();
                case "10" :
                    return this.tenthProgram();
                case "help":
                    this.help();
                    break;
                case "exit":
                    throw new exception("exit");
                default:
                    throw new exception("wrong input");
            }
        }
    }


    private void help() {
        System.out.println("This is the input help menu.\n" +
                "Please choose one of the following programs:\n" +
                "1. \"int v; v = 2; print(v)\"\n" +
                "2. \"int a; int b; a = 2 + 3*5; b = a + 1; print(b)\"\n" +
                "3. \"bool a; int v; a = true; (if a then v = 2 else v = 3); print(v)\"\n" +
                "4. \"string varf; varf=\"test.in\"; openRFile(varf); int varc; readFile(varf,varc); print(varc); readFile(varf,varc); print(varc); closeRFile(varf)\"\n" +
                "5. \"ref int v; new(v,20); ref ref int a; new(a,v); print(v); print(a)\"\n" +
                "6. \"ref int v; new(v,20); ref ref int a; new(a,v); print(readHeap(v)); print(readHeap(readHeap(a)) + 5)\"\n" +
                "7. \"ref int v; new(v,20); print(readHeap(v)); writeHeap(v,30); print(readHeap(v) + 5);\"\n" +
                "8. \"ref int v; new(v,20); ref ref int a; new(a,v); new(v,30); print(readHeap(readHeap(a)))\"\n" +
                "9. \"int v; v = 4; (while (v > 0) print(v); v = v - 1); print(v)\"\n" +
                "10. \"int v; ref int a; v = 10; new(a,22); fork( writeHeap(a,30); v = 32; print(v); print(readHeap(a)) ); print(v); print(readHeap(a))\"\n");
    }


    private Statement firstProgram() {
        // int v; v = 2; print(v)
        return  new compoundStatement(
                new declarationStatement("v", new intType()), new compoundStatement(
                new assignStatement("v", new valueExpression(new intValue(2))),
                new printStatement(new variableExpression("v"))
        )   );
    }

    private Statement secondProgram() {
        //int a; int b; a = 2 + 3*5; b = a + 1; print(b)
        Statement declareA = new declarationStatement("a", new intType());
        Statement declareB = new declarationStatement("b", new intType());
        Expression multiplication = new arithmeticExpression(new valueExpression(new intValue(3)),
                new valueExpression(new intValue(5)), "*");
        Expression sum = new arithmeticExpression(new valueExpression(new intValue(2)),
                multiplication, "+");
        Statement assignA = new assignStatement("a", sum);
        Expression sum2 = new arithmeticExpression(new variableExpression("a"),
                new valueExpression(new intValue(1)), "+");
        Statement assignB = new assignStatement("b", sum2);
        Statement printB = new printStatement(new variableExpression("a"));

        return  new compoundStatement(
                declareA, new compoundStatement(
                declareB, new compoundStatement(
                assignA, new compoundStatement(
                assignB,
                printB
        )   )   )   );
    }

    private Statement thirdProgram() {
        //bool a; int v; a = true; (if a then v = 2 else v = 3); print(v)
        Expression a = new variableExpression("a");
        Expression v = new variableExpression("v");
        Expression True = new valueExpression(new boolValue(true));
        Expression two = new valueExpression(new intValue(2));
        Expression three = new valueExpression(new intValue(3));
        //Expression aEqualsTrue = new logicExpression(a, True, "and");

        Statement boolA = new declarationStatement("a", new boolType());
        Statement intV = new declarationStatement("v", new intType());
        Statement aGetsTrue = new assignStatement("a", True);
        Statement then = new assignStatement("v", two);
        Statement elsse = new assignStatement("v", three);
        Statement vGets = new ifStatement(a, then, elsse);
        Statement printV = new printStatement(v);

        return  new compoundStatement(
                boolA, new compoundStatement(
                intV, new compoundStatement(
                aGetsTrue, new compoundStatement(
                vGets,
                printV
        )   )   )   );
    }

    private Statement fourthProgram() {
        // string varf; varf="test.in"; openRFile(varf); int varc; readFile(varf,varc); print(varc); readFile(varf,varc); print(varc); closeRFile(varf)
        return  new compoundStatement(
                new declarationStatement("varf", new stringType()), new compoundStatement(
                new assignStatement("varf", new valueExpression(new stringValue("test.in"))), new compoundStatement(
                new openFileStatement(new variableExpression("varf")), new compoundStatement(
                new declarationStatement("varc", new intType()), new compoundStatement(
                new readFileStatement("varc", new variableExpression("varf")),  new compoundStatement(
                new printStatement(new variableExpression("varc")), new compoundStatement(
                new readFileStatement("varc", new variableExpression("varf")), new compoundStatement(
                new printStatement(new variableExpression("varc")),
                new closeFileStatement(new variableExpression("varf"))
        )   )   )   )   )   )   )   );
    }


    private Statement fifthProgram() {
        // ref int v; new(v,20); ref ref int a; new(a,v); print(v); print(a)
        return  new compoundStatement(
                new declarationStatement("v", new refType(new intType())), new compoundStatement(
                new newStatement("v", new valueExpression(new intValue(20))),  new compoundStatement(
                new declarationStatement("a", new refType(new refType(new intType()))),  new compoundStatement(
                new newStatement("a", new variableExpression("v")),  new compoundStatement(
                new printStatement(new variableExpression("v")),
                new printStatement(new variableExpression("a"))
        )   )   )   )   );
    }

    private Statement sixthProgram() {
        // ref int v; new(v,20); ref ref int a; new(a,v); print(readHeap(v)); print(readHeap(readHeap(a)) + 5)
        return  new compoundStatement(
                new declarationStatement("v", new refType(new intType())), new compoundStatement(
                new newStatement("v", new valueExpression(new intValue(20))), new compoundStatement(
                new declarationStatement("a", new refType(new refType(new intType()))),  new compoundStatement(
                new newStatement("a", new variableExpression("v")),  new compoundStatement(
                new printStatement(new readHeapExpression(new variableExpression("v"))),
                new printStatement(new arithmeticExpression(
                        new readHeapExpression(new readHeapExpression(new variableExpression("a"))),
                        new valueExpression(new intValue(5)), "+"))
        )   )   )   )   );
    }

    private Statement seventhProgram() {
        // ref int v; new(v,20); print(readHeap(v)); writeHeap(v,30); print(readHeap(v) + 5);
        return  new compoundStatement(
                new declarationStatement("v", new refType(new intType())), new compoundStatement(
                new newStatement("v", new valueExpression(new intValue(20))), new compoundStatement(
                new printStatement(new readHeapExpression(new variableExpression("v"))), new compoundStatement(
                new writeHeapStatement("v", new valueExpression(new intValue(30))),
                new printStatement(new arithmeticExpression(new readHeapExpression(new variableExpression("v")), new valueExpression(new intValue(5)), "+"))
        )   )   )   );
    }

    private Statement eigthProgram() {
        // ref int v; new(v,20); ref ref int a; new(a,v); new(v,30); print(readHeap(readHeap(a)))
        return  new compoundStatement(
                new declarationStatement("v", new refType(new intType())), new compoundStatement(
                new newStatement("v", new valueExpression(new intValue(20))), new compoundStatement(
                new declarationStatement("a", new refType(new refType(new intType()))), new compoundStatement(
                new newStatement("a", new variableExpression("v")), new compoundStatement(
                new newStatement("v", new valueExpression(new intValue(30))),
                new printStatement(new readHeapExpression(new readHeapExpression(new variableExpression("a"))))
        )   )   )   )   );
    }

    private Statement ninthProgram() {
        // int v; v = 4; (while (v > 0) print(v); v = v - 1); print(v)
        return  new compoundStatement(
                new declarationStatement("v", new intType()), new compoundStatement(
                new assignStatement("v", new valueExpression(new intValue(4))), new compoundStatement(
                new whileStatement(new relationalExpression(new variableExpression("v"), new valueExpression(new intValue(0)), ">"),
                        new compoundStatement(
                        new printStatement(new variableExpression("v")),
                        new assignStatement("v", new arithmeticExpression(new variableExpression("v"), new valueExpression(new intValue(1)), "-"))
                )   ),
                new printStatement(new variableExpression("v"))
        )   )   );
    }


    private Statement tenthProgram() {
        /*  int v; Ref int a; v=10;new(a,22);
            fork(wH(a,30);v=32;print(v);print(rH(a)));
            print(v);print(rH(a))
         */
        return  new compoundStatement(
                new declarationStatement("v", new intType()), new compoundStatement(
                new declarationStatement("a", new refType(new intType())), new compoundStatement(
                new assignStatement("v", new valueExpression(new intValue(10))), new compoundStatement(
                new newStatement("a", new valueExpression(new intValue(22))), new compoundStatement(
                new forkStatement(new compoundStatement(
                        new writeHeapStatement("a", new valueExpression(new intValue(30))), new compoundStatement(
                        new assignStatement("v", new valueExpression(new intValue(32))), new compoundStatement(
                        new printStatement(new variableExpression("v")),
                        new printStatement(new readHeapExpression(new variableExpression("a")))
                )   )   )   ), new compoundStatement(
                new printStatement(new variableExpression("v")),
                new printStatement(new readHeapExpression(new variableExpression("a")))
        )   )   )   )   )   );
    }
}
