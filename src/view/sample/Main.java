package view.sample;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import exception.exception;
import model.collection.list.list;
import model.collection.map.map;
import model.collection.map.mapInterface;
import model.collection.pair.pair;
import model.collection.programState.programState;
import model.expression.*;
import model.statement.Statement;
import model.statement.executionStack.*;
import model.statement.file.closeFileStatement;
import model.statement.file.openFileStatement;
import model.statement.file.readFileStatement;
import model.statement.heap.newStatement;
import model.statement.heap.writeHeapStatement;
import model.statement.symbolTable.assignStatement;
import model.statement.symbolTable.declarationStatement;
import model.statement.symbolTable.printStatement;
import model.type.*;
import model.value.Value;
import model.value.boolValue;
import model.value.intValue;
import model.value.stringValue;
import repository.repository;
import repository.repositoryInterface;
import controller.controllerInterface;
import controller.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class Main extends Application {
    private repositoryInterface repo;
    private controllerInterface ctrl;
    private List programs;

    public Main() {
        this.repo = new repository(new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + ".txt");
        this.ctrl = new controller(this.repo);
        this.programs = new ArrayList<String>();
    }

    public static void main(String[] args) {
        launch(args);
    }

    TabPane mainTabPane;
    Tab programTab;
    ListView<String> programList;
    //Integer selectedProgramIndex;
    Label noProgramStatesTitle;
    TextField noProgramStatesText;
    HBox noProgramsBox;
    VBox programBox;

    Tab heapTab;
    TableView<pair> heapTable;
    TableColumn heapAdressColumn;
    TableColumn heapValueColumn;

    Tab outputTab;
    ListView<String> outputList;

    Tab fileTab;
    ListView<String> fileList;

    Tab programStateTab;
    ListView<Integer> programStateList;
    Integer selectedProgramStateID;

    Tab symbolTab;
    TableView<pair> symbolTable;
    TableColumn symbolNameColumn;
    TableColumn symbolValueColumn;

    Tab executionTab;
    ListView<String> executionList;

    //Button addProgramButton;
    Button oneStepButton;
    Button allStepButton;
    HBox buttonBox;

    VBox mainBox;
    Scene window;


    @Override
    public void start(Stage primaryStage) throws Exception{
        this.addPrograms();
        this.instantiateButtons();
        this.instantiateMainTabPane();
        this.instantiateTabChanges();

        this.mainBox = new VBox(this.buttonBox, this.mainTabPane);
        this.window = new Scene(this.mainBox, 1000, 500);

        primaryStage.setTitle("Toy Language GUI");
        primaryStage.setScene(window);
        primaryStage.show();
    }


    private void addPrograms() {
        this.programs.add("int v; v = 2; print(v)");
        this.programs.add("int a; int b; a = 2 + 3*5; b = a + 1; print(b)");
        this.programs.add("bool a; int v; a = true; (if a then v = 2 else v = 3); print(v)");
        this.programs.add("string varf; varf=\"test.in\"; openRFile(varf); int varc; readFile(varf,varc); print(varc); readFile(varf,varc); print(varc); closeRFile(varf)");
        this.programs.add("ref int v; new(v,20); ref ref int a; new(a,v); print(v); print(a)");
        this.programs.add("ref int v; new(v,20); ref ref int a; new(a,v); print(readHeap(v)); print(readHeap(readHeap(a)) + 5)");
        this.programs.add("ref int v; new(v,20); print(readHeap(v)); writeHeap(v,30); print(readHeap(v) + 5);");
        this.programs.add("ref int v; new(v,20); ref ref int a; new(a,v); new(v,30); print(readHeap(readHeap(a)))");
        this.programs.add("int v; v = 4; (while (v > 0) print(v); v = v - 1); print(v)");
        this.programs.add("int v; ref int a; v = 10; new(a,22); fork( writeHeap(a,30); v = 32; print(v); print(readHeap(a)) ); print(v); print(readHeap(a))");
    }


    private void instantiateButtons() {
        this.oneStepButton = new Button("DEBUG (run one step)");
        this.allStepButton = new Button("RUN (run all steps)");
        //this.addProgramButton = new Button("ADD (selected program)");
        this.oneStepButton.setOnMouseClicked(mouseEvent -> {
            this.oneStep(); });
        this.allStepButton.setOnMouseClicked(mouseEvent -> {
            this.ctrl.allSteps(); });

        this.buttonBox = new HBox(this.allStepButton, this.oneStepButton);
    }

    private void oneStep() {
        list<programState> stateList = this.ctrl.removeCompletedProgramStates(this.repo.getProgramList());
        try {
            this.ctrl.oneStep(stateList);
        } catch (exception exception) {
            new Alert(AlertType.ERROR, "An error has occured :\n\t" + exception.getMessage()).showAndWait();
            return;
        }
        this.ctrl.garbageCollector(stateList);
        this.updateProgramTab();
    }

    private void allSteps() {
        this.ctrl.allSteps();
    }

    private void instantiateMainTabPane() {
        this.mainTabPane = new TabPane();
        this.instantiateProgramTab();
        this.instantiateHeapTab();
        this.instantiateOutputTab();
        this.instantiateFileTab();
        this.instantiateProgramStateTab();
        this.instantiateSymbolTab();
        this.instantiateExecutionTab();

        this.mainTabPane.getTabs().addAll(this.programTab,
                this.programStateTab,
                this.executionTab,
                this.heapTab,
                this.symbolTab,
                this.outputTab,
                this.fileTab);
    }

    private void instantiateExecutionTab() {
        this.executionList = new ListView<String>();
        this.executionTab = new Tab("execution");
    }

    private void instantiateSymbolTab() {
        this.symbolTable = new TableView<pair>();
        this.symbolNameColumn = new TableColumn("Name");
        this.symbolNameColumn.setCellValueFactory(new PropertyValueFactory<>("first"));
        this.symbolValueColumn = new TableColumn("Value");
        this.symbolValueColumn.setCellValueFactory(new PropertyValueFactory<>("second"));
        this.symbolTable.getColumns().addAll(this.symbolNameColumn, this.symbolValueColumn);
        this.symbolTab = new Tab("symbols");
    }

    private void instantiateProgramStateTab() {
        this.selectedProgramStateID = 0;
        this.programStateList = new ListView<Integer>();
        this.programStateTab = new Tab("program states");
    }

    private void instantiateFileTab() {
        this.fileList = new ListView<String>();
        this.fileTab = new Tab("files");
    }

    private void instantiateOutputTab() {
        this.outputList = new ListView<String>();
        this.outputTab = new Tab("output");
    }

    private void instantiateHeapTab() {
        this.heapTable = new TableView<pair>();
        this.heapAdressColumn = new TableColumn("Adress");
        this.heapAdressColumn.setCellValueFactory(new PropertyValueFactory<>("first"));
        this.heapValueColumn = new TableColumn("Value");
        this.heapValueColumn.setCellValueFactory(new PropertyValueFactory<>("second"));
        this.heapTable.getColumns().addAll(this.heapAdressColumn, this.heapValueColumn);
        this.heapTab = new Tab("heap");
    }

    private void instantiateProgramTab() {
        this.noProgramStatesTitle = new Label("Number of program states : ");
        this.noProgramStatesText = new TextField("0");
        this.noProgramsBox = new HBox(this.noProgramStatesTitle, this.noProgramStatesText);

        //this.selectedProgramIndex = 0;
        this.programList = new ListView<String>(FXCollections.observableArrayList(this.programs));
        this.programList.getSelectionModel().selectedIndexProperty().addListener(
            (observableValue, oldIndex, newIndex) -> {
                //selectedProgramIndex = (Integer) newIndex;
                this.getProgram((Integer) newIndex);
            });
        this.programBox = new VBox(this.noProgramsBox,
                this.programList);

        this.programTab = new Tab("programs");
        this.programTab.setContent(this.programBox);
    }

    private void getProgram(Integer selectedProgramIndex) {
        this.ctrl.clearAll();
        Statement originalProgram = this.input(selectedProgramIndex);
        this.updateProgramTab();
        try {
            originalProgram.typeCheck(new map<String, Type>());
            this.ctrl.addStatement(originalProgram);
        } catch (exception exception) {
            new Alert( AlertType.ERROR, "An error has occured :\n\t" + exception.getMessage() ).showAndWait();
            return;
        }
    }

    private Statement input(Integer programIndex) {
        switch (programIndex) {
            case 0:
                return  new compoundStatement(
                        new declarationStatement("v", new intType()), new compoundStatement(
                        new assignStatement("v", new valueExpression(new intValue(2))),
                        new printStatement(new variableExpression("v"))
                )   );
            case 1:
                return  new compoundStatement(
                        new declarationStatement("a", new intType()), new compoundStatement(
                        new declarationStatement("b", new intType()), new compoundStatement(
                        new assignStatement("a", new arithmeticExpression(new valueExpression(new intValue(2)),
                                new arithmeticExpression(new valueExpression(new intValue(3)),
                                        new valueExpression(new intValue(5)), "*"), "+")), new compoundStatement(
                        new assignStatement("b", new arithmeticExpression(new variableExpression("a"),
                                new valueExpression(new intValue(1)), "+")),
                        new printStatement(new variableExpression("a"))
                )   )   )   );
            case 2:
                return  new compoundStatement(
                        new declarationStatement("a", new boolType()), new compoundStatement(
                        new declarationStatement("v", new intType()), new compoundStatement(
                        new assignStatement("a", new valueExpression(new boolValue(true))), new compoundStatement(
                        new ifStatement(new variableExpression("a"),
                                new assignStatement("v", new valueExpression(new intValue(2))),
                                new assignStatement("v", new valueExpression(new intValue(3)))),
                        new printStatement(new variableExpression("v"))
                )   )   )   );
            case 3:
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
            case 4:
                return  new compoundStatement(
                        new declarationStatement("v", new refType(new intType())), new compoundStatement(
                        new newStatement("v", new valueExpression(new intValue(20))),  new compoundStatement(
                        new declarationStatement("a", new refType(new refType(new intType()))),  new compoundStatement(
                        new newStatement("a", new variableExpression("v")),  new compoundStatement(
                        new printStatement(new variableExpression("v")),
                        new printStatement(new variableExpression("a"))
                )   )   )   )   );
            case 5:
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
            case 6:
                return  new compoundStatement(
                        new declarationStatement("v", new refType(new intType())), new compoundStatement(
                        new newStatement("v", new valueExpression(new intValue(20))), new compoundStatement(
                        new printStatement(new readHeapExpression(new variableExpression("v"))), new compoundStatement(
                        new writeHeapStatement("v", new valueExpression(new intValue(30))),
                        new printStatement(new arithmeticExpression(new readHeapExpression(new variableExpression("v")), new valueExpression(new intValue(5)), "+"))
                )   )   )   );
            case 7:
                return  new compoundStatement(
                        new declarationStatement("v", new refType(new intType())), new compoundStatement(
                        new newStatement("v", new valueExpression(new intValue(20))), new compoundStatement(
                        new declarationStatement("a", new refType(new refType(new intType()))), new compoundStatement(
                        new newStatement("a", new variableExpression("v")), new compoundStatement(
                        new newStatement("v", new valueExpression(new intValue(30))),
                        new printStatement(new readHeapExpression(new readHeapExpression(new variableExpression("a"))))
                )   )   )   )   );
            case 8:
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
            case 9 :
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
            default:
                return new noStatement();
        }
    }


    private void instantiateTabChanges() {
        this.executionTab.setOnSelectionChanged(event -> {
            this.updateExecutionTab(); });

        this.symbolTab.setOnSelectionChanged(event -> {
            this.updateSymbolTab(); });

        this.programStateTab.setOnSelectionChanged(event -> {
            this.updateProgramStateTab(); });

        this.fileTab.setOnSelectionChanged(event -> {
            this.updateFileTab(); });

        this.outputTab.setOnSelectionChanged(event -> {
            this.updateOutputTab(); });

        this.heapTab.setOnSelectionChanged(event -> {
            this.updateHeapTab(); });
    }

    private void updateExecutionTab() {
        List<String> executionStackStrings = null;
        try {
            executionStackStrings = this.repo.getProgram(this.selectedProgramStateID)
                .getExecutionStack().toArrayList().stream()
                .map(Statement::toString)
                .collect(Collectors.toList());
        } catch (exception exception) {
            new Alert(AlertType.ERROR, "No Program State Selected :\n\t" + exception.toString()).showAndWait();
            return;
        }


        this.executionList = new ListView<String>(FXCollections.observableArrayList(executionStackStrings));
        this.executionTab.setContent(this.executionList);
    }

    private void updateSymbolTab() {
        List<pair> symbolValues = null;
        try {
            symbolValues =
                this.repo.getProgram(this.selectedProgramStateID)
                    .getSymbolTable().toHashMap().entrySet().stream()
                    .map(element -> { return new pair(element.getKey(), element.getValue().toString()); })
                    .collect(Collectors.toList());
        } catch (exception exception) {
            new Alert(AlertType.ERROR, "No Program State Selected :\n\t" + exception.toString()).showAndWait();
            return;
        }


        this.symbolTable.getItems().clear();
        this.symbolTable.getItems().addAll(FXCollections.observableArrayList(symbolValues));
        this.symbolTab.setContent(this.symbolTable);
    }

    private void updateProgramStateTab() {
        List<Integer> programStateIDs = null;
        programStateIDs =
            this.repo.getProgramList()
                .toArrayList().stream()
                .map(programState::getID)
                .collect(Collectors.toList());

        this.programStateList = new ListView<Integer>(FXCollections.observableArrayList(programStateIDs));
        this.programStateList.getSelectionModel().selectedItemProperty().addListener(
                (observableValue, oldID, newID) -> selectedProgramStateID = newID );
        this.programStateTab.setContent(this.programStateList);
    }

    private void updateFileTab() {
        this.fileList = null;
        try {
            this.fileList = new ListView<String>(FXCollections.observableArrayList(
                    this.repo.getProgram(this.selectedProgramStateID).getFileTable().getKeys().toArrayList() ));
        } catch (exception exception) {
            new Alert(AlertType.ERROR, "No Program State Selected :\n\t" + exception.toString()).showAndWait();
            return;
        }

        this.fileTab.setContent(this.fileList);
    }

    private void updateOutputTab() {
        List<String> outputStrings = null;
        try {
            outputStrings =
                this.repo.getProgram(this.selectedProgramStateID)
                    .getOutput().toArrayList().stream()
                    .map(Value::toString)
                    .collect(Collectors.toList());
        } catch (exception exception) {
            new Alert(AlertType.ERROR, "No Program State Selected :\n\t" + exception.toString()).showAndWait();
            return;
        }

        this.outputList = new ListView<String>(FXCollections.observableArrayList(outputStrings));
        this.outputTab.setContent(this.outputList);
    }

    private void updateHeapTab() {
        List<pair> heapValues = null;
        try {
            heapValues =
                this.repo.getProgram(this.selectedProgramStateID)
                    .getHeap().toHashMap().entrySet().stream()
                    .map(element -> { return new pair(element.getKey().toString(), element.getValue().toString()); })
                    .collect(Collectors.toList());
        } catch (exception exception) {
            new Alert(AlertType.ERROR, "No Program State Selected :\n\t" + exception.toString()).showAndWait();
            return;
        }

        this.heapTable.getItems().clear();
        this.heapTable.getItems().addAll(FXCollections.observableArrayList(heapValues));
        this.heapTab.setContent(this.heapTable);
    }

    private void updateProgramTab() {
        this.noProgramStatesText.setText( String.valueOf(
                this.repo.getProgramList().size() ));
    }
}
