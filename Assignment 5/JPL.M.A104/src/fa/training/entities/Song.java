package fa.training.entities;

import java.util.*;
public class Song extends Multimedia{
    private String singer;

    public Song(){
        super();
    }

    public Song(String name, double duration, String singer){
        super(name, duration);
        this.singer = singer;
    }

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public void createSong(){
        System.out.println("-----Enter song infomation-----");
        super.createMultimedia();

        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter singer: ");
        this.singer = scanner.nextLine();
        System.out.print("\nAdd song successfully");
    }

    @Override
    public String toString(){
        return String.format("Song:\t\t%-20s\t%.1f\t%s", name, duration, singer);
    }
}
