package LogicLayer;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.SplitPane;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;

import org.jfree.chart.fx.ChartViewer;
import org.jfree.data.category.IntervalCategoryDataset;
import org.jfree.data.gantt.Task;
import org.jfree.data.gantt.TaskSeries;
import org.jfree.data.gantt.TaskSeriesCollection;


import java.sql.Date;
import java.util.Objects;

/**
 * Class for create GanttChart
 */
public class JFreeGanttChart implements Observer {
    JFreeChart chart;

    public ChartViewer getViewer() {
        return viewer;
    }

    ChartViewer viewer;

    public JFreeGanttChart(SplitPane sp)
    {
        viewer = new ChartViewer();
        this.chart=ChartFactory.createGanttChart("", "", "", createDataset(FXCollections.observableArrayList()),
                false, false, false);
        this.viewer.setChart(chart);
        sp.getItems().add(viewer);
    }

    /**
     *
     * @param problems defined by the user to be displayed on the chart
     * @return chart dataset
     */
    private IntervalCategoryDataset createDataset(ObservableList<Problem> problems) {

        TaskSeriesCollection dataset = new TaskSeriesCollection();
        TaskSeries expected = new TaskSeries("Expected Date");
        Date start,end;
        String name;

        for (int i = 0; i < problems.size(); i++) {
            boolean nameIsNull = Objects.equals(problems.get(i).getName().getText(), "") || problems.get(i).getName()==null;
            boolean startIsNull = problems.get(i).getStartDate().getValue() ==null;
            boolean endIsNull = problems.get(i).getEndDate().getValue() ==null;
            if ( nameIsNull || startIsNull || endIsNull ) continue;
            name=problems.get(i).getName().getText();
            start=java.sql.Date.valueOf(problems.get(i).getStartDate().getValue());
            end=java.sql.Date.valueOf(problems.get(i).getEndDate().getValue());
            expected.add(new Task(name,start,end));
        }
        dataset.add(expected);

        return dataset;
    }

    /**
     *
     * @param problems defined by the user to be displayed on the chart
     */
    @Override
    public void handleEvent(ObservableList<Problem> problems) {
        this.chart=ChartFactory.createGanttChart("", "", "", createDataset(problems),
                    false, false, false);
        this.viewer.setChart(chart);
    }
}
