package view.sample;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Pair;

import model.collection.list.list;
import repository.repository;
import repository.repositoryInterface;
import controller.controllerInterface;
import controller.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Main extends Application {
    private repositoryInterface repo;
    private controllerInterface ctrl;
    private List programs;

    public Main() {
        this.repo = new repository(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        this.ctrl = new controller(repo);
        this.programs = new ArrayList<String>();
    }

    TabPane mainTabPane;

    Tab programTab;
    ListView<String> programList;
    Label noProgramStatesTitle;
    TextField noProgramStatesText;
    HBox noProgramsBox;
    VBox programBox;

    Tab heapTab;
    TableView< Pair<Integer, String> > heapTable;

    Tab outputTab;
    ListView<String> outputList;

    Tab fileTab;
    ListView<String> fileList;

    Tab programStateTab;
    ListView<Integer> programStateList;

    Tab symbolTab;
    TableView< Pair<String, String> > symbolTable;

    Tab executionTab;
    ListView<String> executionList;

    Button oneStepButton;

    VBox mainBox;
    Scene window;


    @Override
    public void start(Stage primaryStage) throws Exception{
        this.addPrograms();
        this.oneStepButton = new Button("Run one Step");
        this.instantiateMainTabPane();

        this.mainBox = new VBox(this.oneStepButton, this.mainTabPane);
        this.window = new Scene(this.mainBox, 1000, 500);

        primaryStage.setTitle("Toy Language GUI");
        primaryStage.setScene(window);
        primaryStage.show();
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
                this.heapTab,
                this.outputTab,
                this.fileTab,
                this.programStateTab,
                this.symbolTab,
                this.executionTab);
    }

    private void instantiateExecutionTab() {
        this.executionList = new ListView<String>();
        // put execution stack values into List View
        this.executionTab = new Tab("execution");
        this.executionTab.setContent(this.executionList);
    }

    private void instantiateSymbolTab() {
        this.symbolTable = new TableView< Pair<String, String> >();
        // put symbol table values into Table View
        this.symbolTab = new Tab("symbols");
        this.symbolTab.setContent(this.symbolTable);
    }

    private void instantiateProgramStateTab() {
        this.programStateList = new ListView<Integer>();
        // put programState IDs into ListView
        this.programStateTab = new Tab("program states");
        this.programStateTab.setContent(this.programStateList);
    }

    private void instantiateFileTab() {
        this.fileList = new ListView<String>();
        // put file values into ListView
        this.fileTab = new Tab("files");
        this.fileTab.setContent(this.fileList);
    }

    private void instantiateOutputTab() {
        this.outputList = new ListView<String>();
        // put output values into ListView
        this.outputTab = new Tab("output");
        this.outputTab.setContent(this.outputList);
    }

    private void instantiateHeapTab() {
        this.heapTable = new TableView< Pair<Integer, String> >();
        // put heap values into TableView
        this.heapTab = new Tab("heap");
        this.heapTab.setContent(this.heapTable);
    }

    private void instantiateProgramTab() {
        this.noProgramStatesTitle = new Label("Number of program states : ");
        this.noProgramStatesText = new TextField("0");
        this.noProgramsBox = new HBox(this.noProgramStatesTitle,
                this.noProgramStatesText);

        this.programList = new ListView<String>(FXCollections.observableArrayList(this.programs));
        this.programBox = new VBox(this.noProgramsBox,
                this.programList);

        this.programTab = new Tab("programs");
        this.programTab.setContent(this.programBox);
    }

    public static void main(String[] args) {
        launch(args);
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

}
