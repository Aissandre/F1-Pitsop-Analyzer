import Formula1.*;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import Formula1.Pitstop;
import Formula1.Race;
import Formula1.RaceData;

public class APITesting {
    public static void main(String[] args) {
        API openf1 = new API();
        List<Race> test2 = openf1.getSeasonRaces(2024);
        
        for (Race race : test2) {
            System.out.println("-----------Race---------");
            System.out.println("Name: " + race.getCircuitName() + " | Key: " + race.getSessionKey());
            List<Driver> test = openf1.fetchDriverInfo(race.getSessionKey());
            List<Pitstop> pitstops = openf1.fetchPitInfo(race.getSessionKey());
            RaceData testData = new RaceData(test, pitstops);

        // Driver race end testing
        System.out.println("Size = " + test.size());

        for (Driver driver : testData.getDrivers().values()) {
            System.out.println("Name: " + driver.getName() + " | Number: " + driver.getDriverNumber() + " | Finished @ Lap " + driver.getFinalLap() + " | End Time: " + driver.getRaceFinishTime(race));        
        }
        System.out.println("Start of race: " + race.getRaceStartTime() + " X = 0");
        
        // Pitstop testing
        for (Map.Entry<Integer, PriorityQueue<Pitstop>> entry : testData.getDriversPitstops().entrySet()) {
            System.out.println("Driver Number: " + entry.getKey());
            while(!entry.getValue().isEmpty()){
                Pitstop stop = entry.getValue().poll();
                
                System.out.println("Pitted at Lap: " + stop.getLapNum() + 
                    " | Entered Time From Start: " + stop.getStartOffset(race) + " = (X1)" +
                    " | Exitted Time From Start: " + stop.getEndOffset(race) + " = (X2)");
                }
            System.out.println("");
            }
        }
    }
}
