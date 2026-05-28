package fa.training.entities;

import java.io.Serializable;
import java.util.*;

public class Airport implements Serializable {
    private static final long serialVersion = 1L;
    private String ID;
    private String name;
    private double runwaySize;
    private int maxFixedWingCapacity;
    private List<String> fixedWingAirplaneIds;
    private int maxRotatedWingCapacity;
    private List<String> helicoptersIds;

    public Airport(){}
    public Airport(String ID, String name,double runwaySize, int maxFixedWingCapacity, int maxRotatedWingCapacity){
        this.ID = ID;
        this.name = name;
        this.runwaySize = runwaySize;
        this.maxFixedWingCapacity = maxFixedWingCapacity;
        this.fixedWingAirplaneIds = new ArrayList<>();
        this.maxRotatedWingCapacity = maxRotatedWingCapacity;
        this.helicoptersIds = new ArrayList<>();
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public double getRunwaySize() {
        return runwaySize;
    }

    public void setRunwaySize(double runwaySize) {
        this.runwaySize = runwaySize;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMaxFixedWingCapacity() {
        return maxFixedWingCapacity;
    }

    public void setMaxFixedWingCapacity(int maxFixedWingCapacity) {
        this.maxFixedWingCapacity = maxFixedWingCapacity;
    }

    public int getMaxRotatedWingCapacity() {
        return maxRotatedWingCapacity;
    }

    public void setMaxRotatedWingCapacity(int maxRotatedWingCapacity) {
        this.maxRotatedWingCapacity = maxRotatedWingCapacity;
    }

    public List<String> getFixedWingAirplaneIds() {
        return fixedWingAirplaneIds;
    }

    public void setFixedWingAirplaneIds(List<String> fixedWingAirplaneIds) {
        this.fixedWingAirplaneIds = fixedWingAirplaneIds;
    }

    public List<String> getHelicoptersIds() {
        return helicoptersIds;
    }

    public void setHelicoptersIds(List<String> helicoptersIds) {
        this.helicoptersIds = helicoptersIds;
    }
}
