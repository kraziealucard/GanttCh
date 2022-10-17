package LogicLayer;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.*;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

public class Problem implements Observed {

    private TextField ID;
    private TextField name;
    private TextField numberOfDuration;
    private ComboBox<String> categoryOfDuration;
    public DatePicker getStartDate() {
        return startDate;
    }
    public DatePicker getEndDate() {
        return endDate;
    }
    public TextField getID() {
        return ID;
    }
    public TextField getNumberOfDuration() {
        return numberOfDuration;
    }
    public ComboBox<String> getCategoryOfDuration() {
        return categoryOfDuration;
    }
    public MenuButton getPredecessor() {
        return predecessor;
    }
    private DatePicker startDate;
    private DatePicker endDate;
    private MenuButton predecessor;
    transient private final List<Observer> observers = new ArrayList<>();
    private ObservableList<Problem> problems;
    private  boolean isChangeDuration=false;

    /**
     * entity "Problem" for user description
     */
    public Problem(ObservableList<Problem> outerProblems, Observer JFC)
    {
        this.problems = outerProblems;
        AddObs(JFC);

        ID = new TextField();
        ID.setAlignment(Pos.CENTER);
        ID.setEditable(false);
        if (problems == null) ID.setText("1");
        else ID.setText(Integer.toString(problems.size() + 1));

        createName();
        createNumberOfDuration();

        categoryOfDuration = new ComboBox<String>();
        categoryOfDuration.getItems().add("Дней");
        categoryOfDuration.getItems().add("Месяцев");
        categoryOfDuration.getItems().add("Лет");
        categoryOfDuration.getSelectionModel().selectFirst();

        createStartDate();
        createEndDate();
        createPredecessor();
    }

    /**
     * Method creates of MenuButton "predecessor"
     */
    private void createPredecessor()
    {
        this.predecessor=new MenuButton();
        this.predecessor.getItems().add(new CheckMenuItem(""));

        predecessor.getItems().get(0).setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                predecessor.getItems().clear();
                predecessor.getItems().add(new CheckMenuItem(""));
                predecessor.getItems().get(0).setOnAction(this);

                for (int i = 1; i <= problems.size(); i++) {
                    if (i == Integer.parseInt(Problem.this.ID.getText())) continue;
                    Problem.this.predecessor.getItems().add(new CheckMenuItem(Integer.toString(i)));
                }

                addsEventForNotZeroPredecessors();
                predecessor.setText("");
            }
        });

        addsEventForNotZeroPredecessors();
    }

    /**
     * Method creates of DatePicker "EndDay"
     */
    private void createEndDate()
    {
        this.endDate=new DatePicker();
        endDate.getEditor().textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {

                if (Objects.equals(newValue, ""))
                {
                    endDate.setValue(null);
                    return;
                }

                if (startDate.getValue() == null) return;

                if (endDate.getValue().compareTo(startDate.getValue()) <= 0) {
                    endDate.setValue(startDate.getValue());
                }

                ChangeDate(endDate, startDate);
                Problem.this.notifyObs();
            }
        });
    }

    /**
     * Method creates of DatePicker "StartDay"
     */
    private void createStartDate()
    {
        startDate=new DatePicker();
        startDate.getEditor().textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {

                if (Objects.equals(newValue, ""))
                {
                    startDate.setValue(null);
                    return;
                }

                if (endDate.getValue() == null) return;

                if (startDate.getValue().compareTo(endDate.getValue()) >= 0) {
                    startDate.setValue(endDate.getValue());
                }
                ChangeDate(startDate, endDate);

                Problem.this.notifyObs();
            }
        });
    }

    /**
     * @param first  Date
     * @param second Date
     */
    public void ChangeDate(DatePicker first, DatePicker second) {
        if (second.getValue() != null) {

            long difference = Date.valueOf(first.getValue()).getTime() -
                    Date.valueOf(second.getValue()).getTime();
            int days =  Math.abs ( (int) (difference / (24 * 60 * 60 * 1000)) );

            if (days != 0) {
                if (isChangeDuration) {
                    days /= switch (categoryOfDuration.getValue()) {
                        case "Дней" -> 1;
                        case "Месяцев" -> 30;
                        case "Лет" -> 365;
                        default -> 0;
                    };
                }
                else {
                    categoryOfDuration.getSelectionModel().selectFirst();
                }
                isChangeDuration=false;
                numberOfDuration.setText(Integer.toString(days));
            } else {
                numberOfDuration.setText("");
            }
        }
        for (int i = 0; i < problems.size(); i++) {
            if (Integer.parseInt(this.ID.getText()) - 1 != i) problems.get(i).calculateStartDate();
        }
    }

    /**
     * Method creates of TextField "Name"
     */
    private void createName()
    {
        name=new TextField();
        name.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                CreateNewRow();
                name.textProperty().removeListener(this);
            }
        });

        name.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean oldValue, Boolean newValue) {
                if (!newValue && (!Objects.equals(name.getText(), "") || name.getText()!=null))
                {
                    duplicateNameCheck();
                    notifyObs();
                }
            }
        });
    }

    /**
     * Method for checking the task name for a duplicate
     */
    public void duplicateNameCheck()
    {
        String current;
        for (Problem problem : problems) {
            if (problem.getName() == name) continue;
            current = problem.name.getText();
            boolean currentIsNull = Objects.equals(current, "") || current == null;
            boolean currentEqualThis = Objects.equals(current, name.getText());

            if (!currentIsNull && currentEqualThis) {
                name.setText(name.getText() + "_" + ID.getText());
            }
        }
    }

    /**
     * Method creates of TextField "numberOfDuration"
     */
    private void createNumberOfDuration()
    {
        numberOfDuration=new TextField();
        numberOfDuration.setAlignment(Pos.CENTER);
        numberOfDuration.textProperty().addListener((observable,oldValue,newValue )-> {
        if (!(Pattern.compile("(\\d*)?")).matcher(newValue).matches()) {
            numberOfDuration.setText(oldValue);
            return;
        }
        if (newValue.equals("")) return;
        calculateDuration();
    });
    }


    /**
     * Method for task duration calculation
     */
    public void calculateDuration() {
        if (startDate.getValue() == endDate.getValue()) {

            numberOfDuration.setText("0");
            return;
        }

        if ((numberOfDuration.getText() != null || !Objects.equals(numberOfDuration.getText(), "")) &&
                (startDate.getValue() != null)) {
            int day = Integer.parseInt(numberOfDuration.getText()) *
                    switch (categoryOfDuration.getValue()) {
                        case "Дней" -> 1;
                        case "Месяцев" -> 30;
                        case "Лет" -> 365;
                        default -> 0;
                    };
            isChangeDuration=true;
            endDate.setValue(startDate.getValue().plusDays(day));
            this.notifyObs();
        }
    }

    /**
     * Method creates new row into table
     */
    private void CreateNewRow() {
        problems.add(new Problem(problems,observers.get(0)));
        for (int i = 0; i < problems.size(); i++) {
            if (i != problems.size() - 1) {
                problems.get(i).predecessor.getItems().add(new CheckMenuItem(String.valueOf(problems.size())));
            } else {
                for (int j = 1; j < problems.size(); j++) {
                    problems.get(i).predecessor.getItems().add(new CheckMenuItem(String.valueOf(j)));
                }
            }
            problems.get(i).addsEventForNotZeroPredecessors();
        }
    }

    /**
     * Method to add events for the first item MenuButton "predecessor"
     */
    private void addsEventForNotZeroPredecessors()
    {
        MenuButton MB = this.predecessor;
        for (int j = 1; j < this.predecessor.getItems().size(); j++) {
            CheckMenuItem temp = (CheckMenuItem) this.predecessor.getItems().get(j);

            temp.setOnAction(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent e) {
                    if (temp.isSelected()) MB.setText(MB.getText() + temp.getText() + " ");
                    else MB.setText(MB.getText().replace(temp.getText() + " ", ""));
                    calculateStartDate();
                }
            });
        }
    }

    /**
     * Method for calculation Start Date for task
     */
    public void calculateStartDate() {
        this.startDate.setDayCellFactory(d -> new DateCell());

        char[] tempChar = this.predecessor.getText().toCharArray();
        LocalDate tempDate = null;
        LocalDate tempEndDate = null;
        for (char c : tempChar) {
            if (c == ' ') continue;
            tempEndDate = problems.get(Character.getNumericValue(c) - 1).endDate.getValue();

            if (tempEndDate != null && (tempDate == null || tempEndDate.compareTo(tempDate) > 0)) {
                tempDate = tempEndDate;
            }
        }

        if (tempDate != null) {
            LocalDate finalTempDate = tempDate;

            if (this.startDate.getValue() != null && this.startDate.getValue().compareTo(tempDate) < 0) {

                if (numberOfDuration.getText()!=null && !Objects.equals(numberOfDuration.getText(), "")) {
                    int day = Integer.parseInt(numberOfDuration.getText()) *
                            switch (categoryOfDuration.getValue()) {
                                case "Дней" -> 1;
                                case "Месяцев" -> 30;
                                case "Лет" -> 365;
                                default -> 0;
                            };
                    this.endDate.setValue(tempDate.plusDays(day));
                }
                this.startDate.setValue(tempDate);
            }

            this.startDate.setDayCellFactory(d ->
                    new DateCell() {
                        @Override
                        public void updateItem(LocalDate item, boolean empty) {
                            super.updateItem(item, empty);
                            setDisable(item.isBefore(finalTempDate));
                        }
                    }
            );
        }
    }

    /**
     *
     * @param observer of tasks
     */
    @Override
    public void AddObs(Observer observer) {
        this.observers.add(observer);
    }

    /**
     *
     * @param observer of tasks
     */
    @Override
    public void RemoveObs(Observer observer) {
        this.observers.remove(observer);
    }

    /**
     * Method for notify observers of tasks
     */
    @Override
    public void notifyObs() {
        for (Observer observer : observers) {
            observer.handleEvent(this.problems);
        }
    }

    public TextField getName() {
        return name;
    }
}
