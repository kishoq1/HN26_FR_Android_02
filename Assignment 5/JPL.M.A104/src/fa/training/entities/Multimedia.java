package fa.training.entities;

import java.util.*;
public abstract class Multimedia {
    protected String name;
    protected double duration;

    public Multimedia(){};

    public Multimedia(String name, double duration){
        this.name = name;
        this.duration = duration;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public void createMultimedia(){
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter name: ");
        this.name = scanner.nextLine();
        System.out.print("\nEnter duration: ");
        this.duration = Double.parseDouble(scanner.nextLine());
    }
}
