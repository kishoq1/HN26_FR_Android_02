package fa.training.entities;

import java.util.*;
public class Fixedwing extends Airplane {
    private String planeType;
    private double minNeededRunwaySize;

    public Fixedwing(){}

    public String getPlaneType() {
        return planeType;
    }

    public void setPlaneType(String planeType) {
        this.planeType = planeType;
    }

    public double getMinNeededRunwaySize() {
        return minNeededRunwaySize;
    }

    public void setMinNeededRunwaySize(double minNeededRunwaySize) {
        this.minNeededRunwaySize = minNeededRunwaySize;
    }

    public Fixedwing(String ID, String model, String planeType, double cruiseSpeed, double emptyWeight, double maxTakeoffWeight, double minNeededRunwaySize ){
        super(ID, model, cruiseSpeed, emptyWeight, maxTakeoffWeight);
        this.planeType = planeType;
        this.minNeededRunwaySize = minNeededRunwaySize;
    }

    @Override
    public String getFlyMethod() {
        return "fixed wing";
    }
}
