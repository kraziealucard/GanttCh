package LogicLayer;

import de.saxsys.javafx.test.JfxRunner;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.JFXPanel;
import javafx.scene.control.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(JfxRunner.class)
class RowTest {

    private TableView<Problem> Left_Table;
    ObservableList<Problem> listOfProblems;

    @BeforeEach
    void Setup()
    {
        JFXPanel jfxPanel=new JFXPanel();
        JFreeGanttChart Jfc=new JFreeGanttChart(new SplitPane());
        Left_Table=new TableView<Problem>();
        ObservableList<Problem> listOfProblems = FXCollections.observableArrayList();
        listOfProblems.add(new Problem(listOfProblems,Jfc));
        Left_Table.setItems(listOfProblems);
    }

    @Test
    void testDuplicateNameCheck() {
        Left_Table.getItems().get(0).getName().setText("I am Row");
        Left_Table.getItems().get(1).getName().setText("I am Row");
        Left_Table.getItems().get(1).duplicateNameCheck();

        assertNotEquals(Left_Table.getItems().get(0).getName().getText(),Left_Table.getItems().get(1).getName().getText());
    }

    @Test
    void testChangeDate() {
        LocalDate d=LocalDate.now();
        Left_Table.getItems().get(0).getStartDate().setValue(d);
        Left_Table.getItems().get(0).getEndDate().setValue(d.plusDays(5));
        Left_Table.getItems().get(0).ChangeDate(Left_Table.getItems().get(0).getStartDate(),Left_Table.getItems().get(0).getEndDate());

        assertEquals(Left_Table.getItems().get(0).getNumberOfDuration().getText(),"5");
    }

    @Test
    void testCalculateStartDate(){
        LocalDate d=LocalDate.now();
        Left_Table.getItems().get(0).getName().setText("I am Row");
        Left_Table.getItems().get(0).getStartDate().setValue(d);
        Left_Table.getItems().get(1).getEndDate().setValue(d.plusDays(5));
        Left_Table.getItems().get(0).getPredecessor().setText("2");
        Left_Table.getItems().get(0).calculateStartDate();

        assertEquals( Left_Table.getItems().get(0).getStartDate().getValue(), Left_Table.getItems().get(1).getEndDate().getValue() );
    }
}