package models;

import java.io.Serializable;
import java.util.ArrayList;
import utils.HouseIDGenerator;
import java.time.LocalDate;

public class Household implements Serializable {
    private String householdID;
    private String name;
    private String address;
    private String dateOfJoining;
    private double totalEcoPoints;
    private double totalWeightRecycled;
    private ArrayList<RecyclingEvent> householdAllRecyclingEvents;

    public Household(String name, String address) {
        this.householdID = HouseIDGenerator.getNextID();
        this.name = name;
        this.address = address;
        this.dateOfJoining = LocalDate.now().toString();
        this.totalEcoPoints = 0;
        this.totalWeightRecycled = 0;
        this.householdAllRecyclingEvents = new ArrayList<>();
    }

    public void addToHouseholdAllRecyclingEvents(RecyclingEvent event) {
        householdAllRecyclingEvents.add(event);
        totalEcoPoints += event.getEcoPointsEarned();
        totalWeightRecycled += event.getWeightInKg();
    }

    public ArrayList<RecyclingEvent> getHouseholdAllRecyclingEvents() {
        return householdAllRecyclingEvents;
    }

    public String getHouseholdID() {
        return householdID;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getDateOfJoining() {
        return dateOfJoining;
    }

    public double getTotalEcoPoints() {
        return totalEcoPoints;
    }

    public double getTotalWeightRecycled() {
        return totalWeightRecycled;
    }

}