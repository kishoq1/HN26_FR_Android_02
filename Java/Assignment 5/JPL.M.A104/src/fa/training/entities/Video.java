package fa.training.entities;

import java.util.*;
public class Video extends Multimedia{
    public Video(){
        super();
    }
    public Video (String name, double duration){
        super(name, duration);
    }

    public void createVideo(){
        System.out.println("-----Enter video infomation-----");
        super.createMultimedia();
        System.out.println("\nAdd video successfully");
    }

    @Override
    public String toString(){
        return String.format("Video:\t\t%-20s\t%.1f", name, duration);
    }
}
