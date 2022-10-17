package LogicLayer;

import javafx.collections.ObservableList;

public interface Observer{
     void handleEvent(ObservableList<Problem> problems);
}
