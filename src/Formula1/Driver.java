package Formula1;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.awt.Color;

/**
 * This Driver class represents a Formula 1 driver, encapsulating key information like the driver's
 * name, number, race finish time, final lap, and the associated colors for visualizations.
 */
public class Driver {
    private String name;
    private int driverNumber, finalLap;
    private Instant finishTime;
    private Color driverColor;

    /**
     * Constructs a driver object with the specified details.
     * 
     * @param name          The name of the driver
     * @param driverNumber  The unique number assigned to the driver
     * @param raceFinishTime The instant representing the time the driver finished the race
     * @param finalLap      The final lap number completed by the driver
     * @param color         A hex string representing the color associated to the driver
     */
    public Driver(String name, int driverNumber, Instant raceFinishTime, int finalLap, String color) {
        this.name = name;
        this.driverNumber = driverNumber;
        finishTime = raceFinishTime;
        this.finalLap = finalLap;
        driverColor = Color.decode("#" + color);
    }

    /**
     * Returns the name of the driver.
     * 
     * @return The driver's name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the unique number assigned to the driver.
     * 
     * @return The driver's number
     */
    public int getDriverNumber() {
        return driverNumber;
    }

    /**
     * Returns the final lap completed by the driver.
     * 
     * @return The number of the last lap the driver completed
     */
    public int getFinalLap() {
        return finalLap;
    }

    /**
     * Returns the color associated with the driver.
     * 
     * @return The Color object representing the driver's color
     */
    public Color getDriverColor() {
        return driverColor;
    }

    /**
     * Calculates the time taken by the driver to finish the race, in milliseconds, later converted to
     * seconds for visulization
     * 
     * @param race The Race object containing the race's start time.
     * @return The race finish time in milliseconds since the race start
     */
    public long getRaceFinishTime(Race race) {
        return race.getRaceStartTime().until(finishTime, ChronoUnit.MILLIS);
    }
}
