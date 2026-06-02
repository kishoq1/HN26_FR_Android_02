package fa.training.main;

import fa.training.services.LibraryService;
import java.util.Scanner;

public class LibraryManagement {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        LibraryService libraryService = new LibraryService();
        int choice = 0;

        while (true) {
            System.out.println("\n====== LIBRARY MANAGEMENT SYSTEM ======");
            System.out.println("1. Add a book");
            System.out.println("2. Add a magazine");
            System.out.println("3. Display books and magazines");
            System.out.println("4. Add author to book");
            System.out.println("5. Display top 10 of magazines by volume");
            System.out.println("6. Search book by (isbn, author, publisher)");
            System.out.println("0. Exit");
            System.out.print("Please choose function you'd like to do: ");

            try {
                choice = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.err.println("Invalid input. Please enter a number!");
                continue;
            }

            switch (choice) {
                case 1:
                    libraryService.addBook();
                    break;
                case 2:
                    libraryService.addMagazine();
                    break;
                case 3:
                    libraryService.displayBooksAndMagazines();
                    break;
                case 4:
                    libraryService.addAuthorToBook();
                    break;
                case 5:
                    libraryService.displayTop10Magazines();
                    break;
                case 6:
                    libraryService.searchBook();
                    break;
                case 0:
                    System.out.println("Exiting program. Goodbye!");
                    scanner.close();
                    System.exit(0);
                default:
                    System.err.println("Function not found. Please choose from 0 to 6.");
            }
        }
    }
}