package fa.training.entities;

import java.io.Serializable;
import java.util.*;
public abstract class Airplane implements Serializable {
    private static final long serialVersion = 1L;
    protected String ID;
    protected String model;
    protected double cruiseSpeed;
    protected double emptyWeight;
    protected double maxTakeoffWeight;

    public Airplane(){}

    public Airplane(String ID, String model, double cruiseSpeed, double emptyWeight, double maxTakeoffWeight){
        this.ID = ID;
        this.model = model;
        this.cruiseSpeed = cruiseSpeed;
        this.emptyWeight = emptyWeight;
        this.maxTakeoffWeight = maxTakeoffWeight;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public double getCruiseSpeed() {
        return cruiseSpeed;
    }

    public void setCruiseSpeed(double cruiseSpeed) {
        this.cruiseSpeed = cruiseSpeed;
    }

    public double getMaxTakeoffWeight() {
        return maxTakeoffWeight;
    }

    public void setMaxTakeoffWeight(double maxTakeoffWeight) {
        this.maxTakeoffWeight = maxTakeoffWeight;
    }

    public double getEmptyWeight() {
        return emptyWeight;
    }

    public void setEmptyWeight(double emptyWeight) {
        this.emptyWeight = emptyWeight;
    }

    public abstract String getFlyMethod();
}
