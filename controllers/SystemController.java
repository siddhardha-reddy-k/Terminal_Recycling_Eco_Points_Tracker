package controllers;

import java.util.HashMap;
import models.Household;
import models.RecyclingEvent;
import java.io.IOException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import utils.HouseIDGenerator;

public class SystemController {
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
        System.out.println("The below are the details\n\n");
        for (Household house : allHouseholds.values()) {
            System.out.println("ID: " + house.getHouseholdID() + " | Name: " + house.getName());
            System.out.println("******************************");
            System.out.println("Total Weight Recycled: " + house.getTotalWeightRecycled());
            System.out.println("Total Eco Ponints Earned: " + house.getTotalEcoPoints());

            System.out.println("All the Recycling Events: ");
            for (RecyclingEvent event : house.getHouseholdAllRecyclingEvents()) {

                System.out.println("Date " + event.getDateOfEvent() + ": Recycled " + event.getMaterialType() + " of "
                        + event.getWeightInKg() + " kg and earned " + event.getEcoPointsEarned() + " points.");
            }

            System.out.println("\n");
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