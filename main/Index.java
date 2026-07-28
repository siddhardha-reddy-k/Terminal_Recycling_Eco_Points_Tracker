package main;

import java.util.Scanner;
import controllers.SystemController;
// Main Class

public class Index {
    public static void main(String[] args) {
        SystemController systemController = new SystemController();
        Scanner sc = new Scanner(System.in);

        systemController.loadData();

        boolean isRunning = true;

        System.out.println("Welcome to the EcoPoints Recycling Tracker!");

        while (isRunning) {
            System.out.println("\n--- MAIN MENU ---");
            System.out.println("1. Register a new household");
            System.out.println("2. Log a recycling event");
            System.out.println("3. Display All household events");
            System.out.println("4. Display Reports");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");

            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    System.out.println("Enter Household Name:");
                    String name = sc.nextLine();
                    System.out.println("Enter Household Address:");
                    String address = sc.nextLine();
                    systemController.registerHousehold(name, address);
                    break;
                case 2:
                    System.out.println("Enter House Hold ID exactly:");
                    String householdID = sc.nextLine();
                    System.out.println("Enter Material Type:");
                    String materialType = sc.nextLine();

                    try {
                        System.out.println("Enter Weight in kg:");
                        double weightInKg = sc.nextDouble();

                        if (weightInKg < 0) {
                            System.out.println("weight Cant be Negative");
                        } else {
                            systemController.recycleEventOfAHouse(householdID, materialType, weightInKg);
                        }
                    } catch (Exception e) {
                        System.err.println("Invalid input. Please enter a valid number for weight.");
                        sc.nextLine();
                    }

                    break;
                case 3:
                    systemController.displayAllHouseholdDetails();
                    break;
                case 4:
                    systemController.reports();
                    break;
                case 5:
                    systemController.saveData();
                    System.out.println("System Exit - Tata");
                    isRunning = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
        sc.close();
    }
}
