package fa.training.main;

import fa.training.entities.Song;
import fa.training.entities.Video;
import fa.training.management.MultimediaManagement;

import java.util.*;
public class Test {
    public static void main(String[] args){
        MultimediaManagement manager = new MultimediaManagement(new ArrayList<>());
        Scanner scanner = new Scanner(System.in);

        while (true){
            System.out.println("Choose function: ");
            System.out.println("1. Add a new Video");
            System.out.println("2. Add a new Song");
            System.out.println("3. Show all Multimedia");
            System.out.println("4. Exit");
            System.out.print("Your choice: ");
            String choice = scanner.nextLine().trim();

            switch (choice){
                case "1":
                    Video v = new Video();
                    v.createVideo();
                    manager.addMultimedia(v);
                    break;
                case "2":
                    Song s = new Song();
                    s.createSong();
                    manager.addMultimedia(s);
                    break;
                case "3":
                    manager.displayMultimedia();
                    break;
                case "4":
                    System.out.println("Program terminated.");
                    scanner.close();
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid choice. Please select from 1 to 4.\n");
                    break;
            }
        }
    }
}
