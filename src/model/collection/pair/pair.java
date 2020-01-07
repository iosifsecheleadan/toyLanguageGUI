package model.collection.pair;

import javafx.beans.property.SimpleStringProperty;

public class pair implements pairInterface {
    private final SimpleStringProperty first;
    private final SimpleStringProperty second;

    public pair(String first, String second) {
        this.first = new SimpleStringProperty(first);
        this.second = new SimpleStringProperty(second);
    }

    public String getFirst(){ return this.first.get(); }
    public String getSecond() { return this.second.get(); }
    public void setFirst(String first) { this.first.set(first); }
    public void setSecond(String second) { this.second.set(second); }
}
