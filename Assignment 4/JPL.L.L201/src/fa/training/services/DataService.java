package fa.training.services;

import fa.training.entities.*;
import java.io.*;
import java.util.*;
public class DataService {
    private static final String FILE_NAME = "airport_data.txt";

    public List<Airport> airports = new ArrayList<>();
    public List<Fixedwing> fixedwings = new ArrayList<>();
    public List<Helicopter> helicopters = new ArrayList<>();

    public void saveData(){
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))){
            oos.writeObject(airports);
            oos.writeObject(fixedwings);
            oos.writeObject(helicopters);
            System.out.println("-> Saved data successfully!");
        } catch (IOException e){
            System.out.println("Error: write file: "+e.getMessage());
        }
    }
    @SuppressWarnings("unchecked")
    public void loadData(){
        File file = new File(FILE_NAME);
        if(!file.exists()) return;
        try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))){
            airports = (List<Airport>) ois.readObject();
            fixedwings = (List<Fixedwing>) ois.readObject();
            helicopters = (List<Helicopter>) ois.readObject();
        } catch (IOException | ClassNotFoundException e){
            System.out.println("Error read file, create new list!");
        }
    }
    public Airport findAirportById(String ID){
        for(Airport a: airports){
            if(a.getID().equals(ID)) return a;
        }
        return null;
    }
    public Fixedwing findFixedwingById(String ID){
        for (Fixedwing a: fixedwings){
            if (a.getID().equals(ID)) return a;
        }
        return null;
    }
    public Helicopter findHelicopterById(String ID){
        for (Helicopter a: helicopters){
            if(a.getID().equals(ID)) return a;
        }
        return null;
    }
}
