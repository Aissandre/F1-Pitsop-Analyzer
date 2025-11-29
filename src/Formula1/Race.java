package Formula1;

import java.time.Instant;

/**
 * The Race class represents a Formula 1 race session, encapsulating details such as the session
 * key, circuit name, race date, and start time. Serves as a data model for storing and retrieving
 * race information.
 */
public class Race {
    private Integer sessionKey;
    private Instant startTime;
    private String circuitName;
    private String date;

    /**
     * Constructs a race object with the specified details.
     * 
     * @param sessionKey    The unique session key identifying the race
     * @param circuitName   The name of the circuit where the race took place
     * @param date          The date of the race in a formatted string
     * @param raceStartTime The instant representing the start time of a race
     */
    public Race(Integer sessionKey, String circuitName, String date, Instant raceStartTime) {
        this.sessionKey = sessionKey;
        this.circuitName = circuitName;
        this.date = date;
        startTime = raceStartTime;
    }

    /**
     * Gets the unique session key identifying the race.
     *
     * @return The session key as an integer
     */
    public Integer getSessionKey() {
        return sessionKey;
    }

    /**
     * Gets the name of the circuit where the race took place.
     *
     * @return The circuit name as a string
     */
    public String getCircuitName() {
        return circuitName;
    }

    /**
     * Gets the date of the race.
     *
     * @return The race date as a formatted string
     */
    public String getDate() {
        return date;
    }

    /**
     * Gets the start time of the race.
     *
     * @return The start time as an instant
     */
    public Instant getRaceStartTime() {
        return startTime;
    }
}
