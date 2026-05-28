package fa.training.management;

import fa.training.entities.Multimedia;
import java.util.*;
public class MultimediaManagement {
    private List<Multimedia> listOfMultimedia;

    public MultimediaManagement(List<Multimedia> listOfMultimedia){
        this.listOfMultimedia = listOfMultimedia;
    }

    public void addMultimedia(Multimedia multimedia){
        if(multimedia != null) this.listOfMultimedia.add(multimedia);
    }

    public void displayMultimedia(){
        System.out.println("\n-------LIST OF MULTIMEDIA-------");
        for (Multimedia m: listOfMultimedia){
            System.out.println(m.toString());
        }
        System.out.println("-----------------------------\n");
    }
}
