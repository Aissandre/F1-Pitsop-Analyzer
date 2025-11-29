package Formula1;

import java.util.List;

/**
 * This Season class represents a Formula! racing season.
 * Encapsulates the year of the season and a list of races that occurred during that year
 * 
 */
public class Season {
    private int year;
    private List<Race> races;

    public Season(int year, List<Race> races){
        this.year = year;
        this.races = races;
    }

    /**
     * Returns the year of the F1 season
     * @return The year of the season
     */
    public int getYear(){
        return year;
    }

    /**
     * Returns the races 
     */
    public List<Race> getRaces(){
        return races;
    }
}
