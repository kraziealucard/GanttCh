package LogicLayer;

public interface Observed {
    void AddObs(Observer observer);
    void RemoveObs(Observer observer);
    void notifyObs();
}
