package models;

import java.io.Serializable;
import java.time.LocalDate;;

public class RecyclingEvent implements Serializable {
    private String materialType;
    private double weightInKg;
    private String dateOfEvent;
    private double ecoPointsEarned;

    public RecyclingEvent(String materialType, double weightInKg) {
        this.materialType = materialType;
        this.weightInKg = weightInKg;

        this.dateOfEvent = LocalDate.now().toString();
        this.ecoPointsEarned = (weightInKg * 10);
    }

    public double getEcoPointsEarned() {
        return ecoPointsEarned;
    }

    public double getWeightInKg() {
        return weightInKg;
    }

    public String getDateOfEvent() {
        return dateOfEvent;
    }

    public String getMaterialType() {
        return materialType;
    }
}