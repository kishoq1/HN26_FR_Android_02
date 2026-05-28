import ContactManagement.Contact;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class Management {
    private static List<Contact> contactList;

    public Management() {
        contactList = new ArrayList<>();
    }

    public static boolean checkExist(String phoneNumber) {
        for (Contact c : contactList) {
            if (c.getPhoneNumber().equals(phoneNumber)) {
                return true;
            }
        }
        return false;
    }

    public static void addContact(String name, String phoneNumber) {
        if (checkExist(phoneNumber)) {
            System.out.println("-> ContactManagement.Contact already exists.");
        } else {
            contactList.add(new Contact(name, phoneNumber));
            System.out.println("-> ContactManagement.Contact added successfully.");
        }
    }

    public static void editContact(String oldPhone, String newPhone) {
        if (!checkExist(oldPhone)) {
            System.out.println("-> Error: The old phone number does not exist.");
            return;
        }
        if (checkExist(newPhone)) {
            System.out.println("-> Error: The new phone number already exists.");
            return;
        }

        for (Contact c : contactList) {
            if (c.getPhoneNumber().equals(oldPhone)) {
                c.setPhoneNumber(newPhone);
                System.out.println("-> ContactManagement.Contact's phone number updated successfully.");
                break;
            }
        }
    }

    public static void searchContact(String name) {
        System.out.println("-> Search results for '" + name + "':");
        boolean found = false;
        for (Contact c : contactList) {
            if (c.getName().toLowerCase().contains(name.toLowerCase())) {
                System.out.println(c);
                found = true;
            }
        }
        if (!found) {
            System.out.println("No matching contacts found.");
        }
    }

    public void sortContacts() {
        contactList.sort((c1, c2) -> c1.getName().compareToIgnoreCase(c2.getName()));
        System.out.println("-> Contacts sorted alphabetically by name.");
    }

    public void displayAll() {
        System.out.println("--- CONTACT LIST ---");
        for (Contact c : contactList) {
            System.out.println(c.toString());
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Management manager = new Management();

        while (true) {
            System.out.println("Choose function: ");
            System.out.println("1. Add a contact");
            System.out.println("2. Edit a contact");
            System.out.println("3. Search for a contact");
            System.out.println("4. Sort contacts alphabetically");
            System.out.println("5. Show contact list");
            System.out.print("Your choice: ");
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    System.out.print("Input name: ");
                    String a = scanner.nextLine();
                    System.out.println();
                    System.out.print("Input phone number: ");
                    String b = scanner.nextLine();
                    System.out.println();
                    addContact(a, b);
                    break;
                case "2":
                    System.out.print("input old phone number:");
                    String c = scanner.nextLine();
                    System.out.println();
                    System.out.print("Input new phone number: ");
                    String d = scanner.nextLine();
                    System.out.println();
                    editContact(c, d);

                    break;
                case "3":
                    System.out.println("Input name contact:");
                    String e = scanner.nextLine();
                    searchContact(e);
                    break;
                case "4":
                    manager.sortContacts();
                    break;
                case "5":
                    manager.displayAll();
                    scanner.close();
                    System.exit(0);
                default:
                    System.out.println("Invalid choice. Please select from 1 to 5.\n");
                    break;
            }
        }
    }
}