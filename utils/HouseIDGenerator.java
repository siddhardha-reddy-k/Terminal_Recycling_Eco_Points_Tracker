package utils;

// HouseIDGenerator
public class HouseIDGenerator {
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