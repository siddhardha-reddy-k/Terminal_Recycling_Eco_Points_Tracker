import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.io.Serializable;
import java.io.IOException;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
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

class SystemController {
    private HashMap<String, Household> allHouseholds = new HashMap<>();
    private static final String SAVE_FILE = "houseHolds.dat";

    // Methods to Register
    public void registerHousehold(String name, String address) {
        Household house = new Household(name, address);
        this.allHouseholds.put(house.getHouseholdID(), house);

        System.out.println("SucessFully Added the house with id '" + house.getHouseholdID() + "' to the register.");

    }

    // Method for Recyclngn event

    public void recycleEventOfAHouse(String householdID, String materialType, double weightInKg) {
        RecyclingEvent event = new RecyclingEvent(materialType, weightInKg);
        Household eventHouseHold = allHouseholds.get(householdID);
        eventHouseHold.addToHouseholdAllRecyclingEvents(event);

        System.out.println("House: " + householdID + " has recycled " + materialType + " of " + weightInKg + " kg.");
    }

    // method to Display All household events
    public void displayAllHouseholdDetails() {

        System.out.println("There are a total of " + allHouseholds.size() + "  houses.");
        System.out.println("The below are the details\n\n\n");
        for (Household house : allHouseholds.values()) {
            System.out.println("ID: " + house.getHouseholdID() + " | Name: " + house.getName());
            System.out.println("*******************");
            System.out.println("Total Weight Recycled: " + house.getTotalWeightRecycled());
            System.out.println("Total Eco Ponints Earned: " + house.getTotalEcoPoints());

            System.out.println("All the Recycling Events: ");
            for (RecyclingEvent event : house.getHouseholdAllRecyclingEvents()) {

                System.out.println("Date " + event.getDateOfEvent() + ": Recycled " + event.getMaterialType() + " of "
                        + event.getWeightInKg() + " kg and earned " + event.getEcoPointsEarned() + " points.");
            }

            System.out.println("\n\n\n");
        }
    }

    // method for reports
    public void reports() {
        double householdWithHighestTotalPoints = 0;
        String nameOfHouseholdWithHighestEcoPoints = null;
        double currentHighestsPoints = 0;
        double totalRecycledWeightOfCommunity = 0;
        for (Household house : allHouseholds.values()) {
            if (house.getTotalEcoPoints() > currentHighestsPoints) {
                currentHighestsPoints = house.getTotalEcoPoints();
                nameOfHouseholdWithHighestEcoPoints = house.getName();
                householdWithHighestTotalPoints = house.getTotalEcoPoints();
            }

            totalRecycledWeightOfCommunity += house.getTotalWeightRecycled();
        }
        System.out.println("House with Highest Eco Points is " + nameOfHouseholdWithHighestEcoPoints + " with "
                + householdWithHighestTotalPoints + " points.");

        System.out.println("Total Weight Recycled by the community : " + totalRecycledWeightOfCommunity);
    }

    // save file object ouput
    public void saveData() {
        try (FileOutputStream fos = new FileOutputStream(SAVE_FILE);
                ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(allHouseholds);
            System.out.println("Data Saved SuccuessFully");

        } catch (IOException e) {
            System.err.println("An error Occured while Saving the Data" + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public void loadData() {
        File file = new File(SAVE_FILE);

        if (!file.exists()) {
            System.out.println("No previous save file found. Starting Fresh");
            return;
        }

        try (
                FileInputStream fis = new FileInputStream(SAVE_FILE);
                ObjectInputStream ois = new ObjectInputStream(fis)) {
            allHouseholds = (HashMap<String, Household>) ois.readObject();
            System.out.println("Data loaded successfully. " + allHouseholds.size() + " households found.");
            restoreIDCounter();

        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading data: " + e.getMessage());
        }
    }

    private void restoreIDCounter() {
        int maxID = 0;

        for (String id : allHouseholds.keySet()) {
            String numberPart = id.substring(id.lastIndexOf('-') + 1).trim();

            int num = Integer.parseInt(numberPart);
            if (num > maxID) {
                maxID = num;
            }

        }

        HouseIDGenerator.setCounter(maxID + 1);
    }
}

class RecyclingEvent implements Serializable {
    private String materialType;
    private double weightInKg;
    private String dateOfEvent;
    private double ecoPointsEarned;

    RecyclingEvent(String materialType, double weightInKg) {
        this.materialType = materialType;
        this.weightInKg = weightInKg;

        this.dateOfEvent = LocalDate.now().toString();
        this.ecoPointsEarned = (weightInKg * 10);
    }

    double getEcoPointsEarned() {
        return ecoPointsEarned;
    }

    double getWeightInKg() {
        return weightInKg;
    }

    String getDateOfEvent() {
        return dateOfEvent;
    }

    String getMaterialType() {
        return materialType;
    }
}

class Household implements Serializable {
    private String householdID;
    private String name;
    private String address;
    private String dateOfJoining;
    private double totalEcoPoints;
    private double totalWeightRecycled;
    private ArrayList<RecyclingEvent> householdAllRecyclingEvents;

    Household(String name, String address) {
        this.householdID = HouseIDGenerator.getNextID();
        this.name = name;
        this.address = address;
        this.dateOfJoining = LocalDate.now().toString();
        this.totalEcoPoints = 0;
        this.totalWeightRecycled = 0;
        this.householdAllRecyclingEvents = new ArrayList<>();
    }

    void addToHouseholdAllRecyclingEvents(RecyclingEvent event) {
        householdAllRecyclingEvents.add(event);
        totalEcoPoints += event.getEcoPointsEarned();
        totalWeightRecycled += event.getWeightInKg();
    }

    ArrayList<RecyclingEvent> getHouseholdAllRecyclingEvents() {
        return householdAllRecyclingEvents;
    }

    String getHouseholdID() {
        return householdID;
    }

    String getName() {
        return name;
    }

    String getAddress() {
        return address;
    }

    String getDateOfJoining() {
        return dateOfJoining;
    }

    double getTotalEcoPoints() {
        return totalEcoPoints;
    }

    double getTotalWeightRecycled() {
        return totalWeightRecycled;
    }

}

// HouseIDGenerator
class HouseIDGenerator {
    private static int counter = 1;

    public static String getNextID() {
        String formattedNumeber = String.format("%04d", counter);
        counter++;
        return "HouseNo - " + formattedNumeber;
    }

    public static void setCounter(int value) {
        counter = value;
    }
}