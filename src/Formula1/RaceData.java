package Formula1;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

/**
 * This RaceData class represents the data associated with a Formula1 race. Organizes drivers and
 * pitstops into easily accessible maps for efficient retrieval and processing. Includes information
 * about drivers and their pitstops.
 */
public class RaceData {
    private Map<Integer, PriorityQueue<Pitstop>> driversPitstops;
    private Map<Integer, Driver> driversMap;

    /**
     * Constructs a race data object by organizing the given drivers and pitstops into maps
     * 
     * @param drivers  A list of driver objects participating in the race
     * @param pitstops A list of pitstop objects representing pitstops during the race
     */
    public RaceData(List<Driver> drivers, List<Pitstop> pitstops) {
        setDriversPitstops(pitstops);
        arrangeDrivers(drivers);
    }

    /**
     * Gets the map of drivers' ID to their priority queues of pitstops
     * 
     * @return A Map where the key is the driver's ID and the value is a PriorityQueue of pitstop
     *         objects sorted by their starting times
     */
    public Map<Integer, PriorityQueue<Pitstop>> getDriversPitstops() {
        return driversPitstops;
    }

    /**
     * Gets the map of drivers' IDs to their corresponding Driver objects.
     * 
     * @return A Map where the key is the driver's ID, and the value is the Driver object
     */
    public Map<Integer, Driver> getDrivers() {
        return driversMap;
    }

    /**
     * Organizes the given list of drivers into a map where the key is the driver's ID and the value is
     * the Driver object.
     * 
     * @param drivers A list of Driver objects participating in the race
     */
    private void arrangeDrivers(List<Driver> drivers) {
        driversMap = new HashMap<>();
        for (Driver driver : drivers) {
            driversMap.put(driver.getDriverNumber(), driver);
        }
    }

    /**
     * Organizes the given list of pitstops into a map where the key is the driver's ID and the value is
     * a PriorityQueue of pitstop objects sorted by their start times.
     * 
     * @param racePitstops A list of pitstop objects representing pitstops during the race
     */
    private void setDriversPitstops(List<Pitstop> racePitstops) {
        driversPitstops = new HashMap<>();
        for (Pitstop pitstop : racePitstops) {
            PriorityQueue<Pitstop> pits = null;
            if (!driversPitstops.containsKey(pitstop.getDriversID())) {
                pits = new PriorityQueue<>();
            } else {
                pits = driversPitstops.get(pitstop.getDriversID());
            }
            pits.offer(pitstop);
            driversPitstops.put(pitstop.getDriversID(), pits);
        }
    }
}
