package Formula1;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

/**
 * This class represents the details of a single pitstop during a Formula 1 race. Each pitstop is
 * associated with a specific driver, lap number, duration, and start time.
 * 
 * Implements Comparable<Pitstop> to allow sorting based on the pitstop's start time.
 */
public class Pitstop implements Comparable<Pitstop> {
    private int driverID, lap;
    private double pitDuration;
    private Instant startPitTime;

    /**
     * Constructs a Pitstop object with the specified details.
     * 
     * @param driverID     The unique ID of the driver associated with this pitstop.
     * @param lap          The lap number when the pitstop occurred.
     * @param pitDuration  The duration of the pitstop in seconds.
     * @param startPitTime The timestamp when the pitstop started.
     */
    public Pitstop(int driverID, int lap, double pitDuration, Instant startPitTime) {
        this.driverID = driverID;
        this.pitDuration = pitDuration;
        this.lap = lap;
        this.startPitTime = startPitTime;

    }

    /**
     * Gets the unique ID of the driver associated with this pitstop.
     * 
     * @return The driver's ID.
     */
    public int getDriversID() {
        return driverID;
    }

    /**
     * Gets the lap number when the pitstop occurred.
     * 
     * @return The lap number of the pitstop
     */
    public int getLapNum() {
        return lap;
    }

    /**
     * Gets the duration of the pitstop in seconds.
     * 
     * @return The pitstop duration
     */
    public double getpitDuration() {
        return pitDuration;
    }

    /**
     * Gets the start time of the pitstop.
     * 
     * @return The insant representing when the pitstop started
     */
    public Instant getstartPitTime() {
        return startPitTime;
    }

    /**
     * Calculates the end offset of the pitstop relative to the start time of the race.
     * 
     * @param race The Race object representing the race
     * @return The time in milliseconds from the start of the race to the end of the pitstop
     */
    public long getEndOffset(Race race) {
        Instant endPitTime = startPitTime.plus((long) (pitDuration * 1000), ChronoUnit.MILLIS);
        return race.getRaceStartTime().until(endPitTime, ChronoUnit.MILLIS);
    }

    /**
     * Calculates the start offset of the pitstop relative to the start time of the race.
     * 
     * @param race The Race object representing the race
     * @return The time in milliseconds from the start of the race to the start of the pitstop
     */
    public long getStartOffset(Race race) {
        return race.getRaceStartTime().until(this.getstartPitTime(), ChronoUnit.MILLIS);
    }

    /**
     * Compares this pitstop to another pitstop based on their start times.
     * 
     * @param otherPit The Pitstop object to compare to
     * @return A negative number, zero, or a positive number if this pitstop's start time is earlier
     *         than, equal to, or later than the other pitstop's start time
     */
    public int compareTo(Pitstop otherPit) {
        return (startPitTime.compareTo(otherPit.startPitTime));
    }

    /**
     * Returns a string representation of this pitstop.
     * 
     * @return A string describing the pitstop
     */
    @Override
    public String toString() {
        return "Driver: " + driverID;
    }
}