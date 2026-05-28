package fa.training.main;

import fa.training.entities.*;
import fa.training.services.DataService;
import fa.training.utils.Validator;

import java.util.Scanner;

public class AirplaneManagement {
    private static final DataService data = new DataService();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        data.loadData();

        while (true) {
            System.out.println("\n=== AIRPORT MANAGEMENT SYSTEM ===");
            System.out.println("1. Create new Airport");
            System.out.println("2. Add Fixedwing to Airport");
            System.out.println("3. Add Helicopter to Airport");
            System.out.println("4. Remove Helicopter from Airport");
            System.out.println("5. Display list of all airports");
            System.out.println("0. Save and Close program");
            System.out.print("Choose option: ");

            String choice = scanner.nextLine();
            switch (choice) {
                case "1": createAirport(); break;
                case "2": addFixedwingToAirport(); break;
                case "3": addHelicopterToAirport(); break;
                case "4": removeHelicopter(); break;
                case "5": displayAirports(); break;
                case "0":
                    data.saveData();
                    System.out.println("Program closed.");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Error: Invalid option.");
            }
        }
    }

    private static void createAirport() {
        System.out.print("Enter Airport ID (APxxxxx): ");
        String id = scanner.nextLine();
        if (!Validator.isValidId(id, "AP") || data.findAirportById(id) != null) {
            System.out.println("Error: Invalid or duplicate Airport ID.");
            return;
        }

        System.out.print("Enter Name: "); String name = scanner.nextLine();
        System.out.print("Enter Runway Size: "); double runwaySize = Double.parseDouble(scanner.nextLine());
        System.out.print("Max Fixed Wing Capacity: "); int maxFW = Integer.parseInt(scanner.nextLine());
        System.out.print("Max Helicopter Capacity: "); int maxRW = Integer.parseInt(scanner.nextLine());

        data.airports.add(new Airport(id, name, runwaySize, maxFW, maxRW));
        System.out.println("-> Airport created successfully.");
    }

    private static void addFixedwingToAirport() {
        System.out.print("Enter Airport ID to park: ");
        String apId = scanner.nextLine();
        Airport ap = data.findAirportById(apId);
        if (ap == null) {
            System.out.println("Error: Airport not found.");
            return;
        }

        if (ap.getFixedWingAirplaneIds().size() >= ap.getMaxFixedWingCapacity()) {
            System.out.println("Error: Airport fixed wing parking is full.");
            return;
        }

        System.out.print("Enter Fixedwing ID (FWxxxxx): ");
        String fwId = scanner.nextLine();
        if (!Validator.isValidId(fwId, "FW") || data.findFixedwingById(fwId) != null) {
            System.out.println("Error: Invalid or duplicate Fixedwing ID.");
            return;
        }

        System.out.print("Min needed runway size: ");
        double minRunway = Double.parseDouble(scanner.nextLine());
        if (minRunway > ap.getRunwaySize()) {
            System.out.println("Error: Airplane min runway size exceeds airport runway size.");
            return;
        }

        System.out.print("Plane Type (CAG, LGR, PRV): "); String type = scanner.nextLine();
        if(!Validator.isValidPlaneType(type)) {
            System.out.println("Error: Invalid plane type.");
            return;
        }

        // Tạo máy bay và thêm vào danh sách của hệ thống và sân bay
        Fixedwing fw = new Fixedwing(fwId, "ModelX", type, 5000, 10000, 800, minRunway);
        data.fixedwings.add(fw);
        ap.getFixedWingAirplaneIds().add(fwId);
        System.out.println("-> Fixedwing parked successfully.");
    }

    private static void addHelicopterToAirport() {
        System.out.print("Enter Airport ID to park: ");
        String apId = scanner.nextLine();
        Airport ap = data.findAirportById(apId);
        if (ap == null) {
            System.out.println("Error: Airport not found.");
            return;
        }

        System.out.print("Enter Helicopter ID (RWxxxxx): ");
        String rwId = scanner.nextLine();
        if (!Validator.isValidId(rwId, "RW") || data.findHelicopterById(rwId) != null) {
            System.out.println("Error: Invalid or duplicate Helicopter ID.");
            return;
        }

        System.out.print("Empty weight: "); double empty = Double.parseDouble(scanner.nextLine());
        System.out.print("Max Takeoff weight: "); double max = Double.parseDouble(scanner.nextLine());

        if (!Validator.isValidHelicopterWeight(empty, max)) {
            System.out.println("Error: Max takeoff weight exceeds 1.5 times empty weight.");
            return;
        }

        Helicopter h = new Helicopter(rwId, "CopterX", 250, empty, max, 1000);
        data.helicopters.add(h);
        ap.getHelicoptersIds().add(rwId);
        System.out.println("-> Helicopter parked successfully.");
    }

    private static void removeHelicopter() {
        System.out.print("Enter Airport ID: ");
        Airport ap = data.findAirportById(scanner.nextLine());
        if (ap == null) {
            System.out.println("Error: Airport not found.");
            return;
        }

        System.out.print("Enter Helicopter ID to remove: ");
        String rwId = scanner.nextLine();
        if (ap.getHelicoptersIds().remove(rwId)) {
            System.out.println("-> Helicopter removed from airport.");
        } else {
            System.out.println("Error: Helicopter not found in this airport.");
        }
    }

    private static void displayAirports() {
        // Sắp xếp sân bay theo ID
        data.airports.sort((a1, a2) -> a1.getID().compareTo(a2.getID()));
        for (Airport a : data.airports) {
            System.out.println("\nAirport ID: " + a.getID() + " | Name: " + a.getName());
            System.out.println("FW Parked: " + a.getFixedWingAirplaneIds().toString());
            System.out.println("RW Parked: " + a.getHelicoptersIds().toString());
        }
    }
}