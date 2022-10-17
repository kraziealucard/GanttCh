package DataLayer;

import LogicLayer.JFreeGanttChart;
import LogicLayer.Problem;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.TableView;
import javafx.scene.image.WritableImage;
import javafx.stage.FileChooser;

import javax.imageio.ImageIO;
import java.io.*;
import java.time.LocalDate;

public class FileManager {
    ObservableList<Problem> tasks;
    ProblemValue[] problemValues;
    JFreeGanttChart JFC;

    /**
     * @param LS Data from table
     * @param JFC Chart of Gantt
     */
    public FileManager(ObservableList<Problem> LS, JFreeGanttChart JFC) {
        this.tasks = LS;
        this.JFC =JFC;
    }

    /**
     * Method for save Table into file
     */
    public void saveTable() {
        FileChooser fileChooser=new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("ganttch files (*.ganttch)","*.ganttch"));
        File file=fileChooser.showSaveDialog(null);

        if (file==null) return;

        problemValues = new ProblemValue[tasks.size()];
        for (int i = 0; i < problemValues.length; i++) {
            problemValues[i]=new ProblemValue();
            problemValues[i].setValues(tasks.get(i));
        }

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeInt(problemValues.length);
            for (int i = 0; i < problemValues.length; i++) {
                oos.writeObject(problemValues[i]);
            }

        } catch (Exception ex) {

            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
    }

   /* *
     * Method for load Table from file
     */
    public void loadTable(TableView<Problem> Table) {
        FileChooser fileChooser=new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("ganttch files (*.ganttch)","*.ganttch"));
        File file=fileChooser.showOpenDialog(null);
        if (file==null) return;

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            problemValues = new ProblemValue[ois.readInt()];
            for (int i = 0; i < problemValues.length; i++) {
                problemValues[i] = (ProblemValue) ois.readObject();
            }
        }
        catch (Exception ex)
        {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }

        this.tasks.clear();
        Table.getItems().clear();
        Table.setItems(tasks);
        Table.refresh();
        tasks.add(new Problem(tasks,JFC));
        for (int i = 0; i < problemValues.length; i++) {
            tasks.get(i).getID().setText(problemValues[i].id);
            tasks.get(i).getName().setText(problemValues[i].name);
            tasks.get(i).getNumberOfDuration().setText(problemValues[i].numberOfDuration);
            tasks.get(i).getCategoryOfDuration().setValue(problemValues[i].itemCBCategoryOfDuration);
            if (problemValues[i].startDay != "") tasks.get(i).getStartDate().setValue(LocalDate.parse(problemValues[i].startDay));
            if (problemValues[i].endDay != "") tasks.get(i).getEndDate().setValue(LocalDate.parse(problemValues[i].endDay));
            tasks.get(i).getPredecessor().setText(problemValues[i].valueOfPredecessor);
        }
    }

   /* *
     * Method for save GanttChart into png
     */
    public void saveImg() {
        FileChooser FC = new FileChooser();
        FC.getExtensionFilters().add(new FileChooser.ExtensionFilter("pngFile (*.png)", "*.png"));
        WritableImage img = JFC.getViewer().snapshot(new SnapshotParameters(), null);
        File file = FC.showSaveDialog(null);
        if (file==null) return;
        try {
            ImageIO.write(SwingFXUtils.fromFXImage(img, null), "png", file);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
