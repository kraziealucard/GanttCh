package DataLayer;

import LogicLayer.Problem;

import java.io.Serializable;

public class ProblemValue implements Serializable {

    public String id;
    public String name;
    public String numberOfDuration;
    public String itemCBCategoryOfDuration;
    public String startDay;
    public String endDay;
    public String valueOfPredecessor;

    /**
     * Class for save and load task into file
     */
    public ProblemValue()
    {
        id="";
        name="";
        numberOfDuration="";
        itemCBCategoryOfDuration="";
        startDay="";
        endDay ="";
        valueOfPredecessor="";
    }

    /**
     * @param row Described task
     */
    public void setValues(Problem row) {
        if (row.getID()!=null) id=row.getID().getText();
        if (row.getName()!=null) name = row.getName().getText();
        if (row.getNumberOfDuration()!=null)numberOfDuration = row.getNumberOfDuration().getText();
        if (row.getCategoryOfDuration()!=null)itemCBCategoryOfDuration = row.getCategoryOfDuration().getValue();
        if (row.getStartDate()!=null && row.getStartDate().getValue()!=null)startDay = row.getStartDate().getValue().toString();
        if (row.getEndDate()!=null && row.getEndDate().getValue()!=null) endDay = row.getEndDate().getValue().toString();
        if (row.getPredecessor()!=null)valueOfPredecessor = row.getPredecessor().getText();
    }
}
