package com.example.ganttchart;

import DataLayer.FileManager;
import LogicLayer.JFreeGanttChart;
import LogicLayer.Problem;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
/**
 * Controller
 */
public class HelloController {
    @FXML private GridPane mainPane;
    @FXML private SplitPane Split_Pane;
    @FXML private TableView<Problem> Left_Table;
    @FXML private TableColumn<Problem,  TextField> Left_ID;
    @FXML private TableColumn<Problem, TextField> Left_Name;
    @FXML private TableColumn<Problem, TextField> Left_NumberOfDuration;
    @FXML private TableColumn<Problem, ComboBox<String>> Left_MS;
    @FXML private TableColumn<Problem, DatePicker> Left_StartDate;
    @FXML private TableColumn<Problem, DatePicker> Left_EndDate;
    @FXML private TableColumn<Problem, MenuButton> Left_Predecessor;

    public void initialize(){
        setValueFactor();

        JFreeGanttChart Jfc=new JFreeGanttChart(getSplit_Pane());
        ObservableList<Problem> listOfProblems = FXCollections.observableArrayList();
        listOfProblems.add(new Problem(listOfProblems,Jfc));
        Left_Table.setItems(listOfProblems);


        addMenu(listOfProblems,Jfc);

    }

    /**
     * Which is used to populate individual cells in the column
     */
    private void setValueFactor()
    {
        this.Left_ID.setCellValueFactory(new PropertyValueFactory<>("ID"));
        this.Left_Name.setCellValueFactory(new PropertyValueFactory<>("name"));
        this.Left_NumberOfDuration.setCellValueFactory(new PropertyValueFactory<>("numberOfDuration"));
        this.Left_MS.setCellValueFactory(new PropertyValueFactory<>("categoryOfDuration"));
        this.Left_StartDate.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        this.Left_EndDate.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        this.Left_Predecessor.setCellValueFactory(new PropertyValueFactory<>("predecessor"));
        this.Left_ID.setSortable(false);
        this.Left_Name.setSortable(false);
        this.Left_NumberOfDuration.setSortable(false);
        this.Left_MS.setSortable(false);
        this.Left_StartDate.setSortable(false);
        this.Left_EndDate.setSortable(false);
        this.Left_Predecessor.setSortable(false);
    }
    
    /**
     * @param LS for Save to file
     * @param JFC for Save to png
     */
    private void addMenu(ObservableList<Problem> LS, JFreeGanttChart JFC)
    {
        MenuBar MB=new MenuBar();
        Menu File=new Menu("Файл");
        MenuItem savePng= new MenuItem("Сохранить диаграмму в png");
        savePng.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                FileManager FM=new FileManager(LS,JFC);
                FM.saveImg();
            }
        });
        

        MenuItem saveFile= new MenuItem("Сохранить таблицу в файл");
        saveFile.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                FileManager FM=new FileManager(LS,JFC);
                FM.saveTable();
            }
        });

        MenuItem loadFile=new MenuItem("Загрузить таблицу из файла");
        loadFile.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                FileManager FM=new FileManager(LS,JFC);
                FM.loadTable(Left_Table);

            }
        });

        File.getItems().add(loadFile);
        File.getItems().add(saveFile);
        File.getItems().add(savePng);

        Menu help=new Menu("Помощь");
        MenuItem about=new MenuItem("О программе");
        about.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, """
                Левая панель: Таблица с задачами
                ID - Нумерное значение задачи;
                Имя - Поле для установки имени задачи;
                Длительность - Поле для устоновки длительности выполнения задачи;
                СИ - Поле для установки измерения длительности задачи;
                Начало - Поле для указания даты начала задачи;
                Окончание - Поле для указания даты коца задачи;
                Предшественник - Поле для указания задач-предшественников после окончания
                которых может начаться выделенная задача\s""");
                alert.setTitle("Справка");
                alert.setHeaderText("Краткое руководство");
                alert.setHeight(900);
                alert.setX(600);
                alert.show();
            }
        });
        help.getItems().add(about);
        MB.getMenus().addAll(File,help);
        mainPane.add(MB,0,0);
    }

    /**
     * @return Pane
     */
    public SplitPane getSplit_Pane()
    {
        return this.Split_Pane;
    }

    /**
     * @return Table of task
     */
    public TableView<Problem> getLeft_Table() {
        return Left_Table;
    }
}