package fa.training.entities;

import java.util.*;
public class Helicopter extends Airplane{
    private double range;

    public Helicopter(){}
    public Helicopter(String ID, String model, double cruiseSpeed, double emptyWeight, double maxTakeoffWeight,double range){
        super(ID,model, cruiseSpeed, emptyWeight, maxTakeoffWeight);
        this.range = range;
    }

    public double getRange() {
        return range;
    }

    public void setRange(double range) {
        this.range = range;
    }

    @Override
    public String getFlyMethod() {
        return "rotated wing";
    }
}
