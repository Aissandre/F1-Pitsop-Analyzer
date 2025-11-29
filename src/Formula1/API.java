package Formula1;

import java.io.IOException;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;

/**
 * This API class provides methods to interact with the OpenF1 API and retrieves data related to the
 * races. It fetches data in JSON format from specified endpoints and turns it into structured Java
 * objects for future processing. Includes details about races, drivers, and pitstops.
 */
public class API {
    private String pitURL = "https://api.openf1.org/vi/pit?",
        driverURL = "https://api.openf1.org/v1/drivers?",
        sessionURL = "https://api.openf1.org/v1/sessions?",
        lapURL = "https://api.openf1.org/v1/laps?";

    /**
     * Constructs an API object for interacting with the OpenF1 API.
     */
    public API() {
    }

    /**
     * Fetches all the races in a specified season.
     * 
     * @param year The year of the season.
     * @return A list of Race objects representing all the races in the given year.
     */
    public List<Race> getSeasonRaces(int year) {
        JSONArray raceData = fetchAllRacesForYear(year);
        List<Race> races = new ArrayList<>();
        for (int i = 0; i < raceData.length(); i++) {
            JSONObject object = raceData.getJSONObject(i);
            races.add(
                new Race(
                    object.getInt("session_key"),
                    object.getString("circuit_short_name"),
                    object.getString("date_start"),
                    OffsetDateTime.parse(object.getString("date_start")).toInstant()));
        }
        return races;
    }

    /**
     * Fetches information about drivers participating in a specific race session.
     * 
     * @param sessionkey The session key identifying the race session.
     * @return A list of Driver objects containing driver details for the session.
     */
    public List<Driver> fetchDriverInfo(int sessionkey) {
        JSONArray driverData = extractRaceData(driverURL, sessionkey);
        List<Driver> drivers = new ArrayList<>();
        for (int i = 0; i < driverData.length(); i++) {
            JSONObject object = driverData.getJSONObject(i);
            JSONObject lastObj = fetchLastObject(sessionkey, object.getInt("driver_number"));
            if (lastObj == null) {
                continue;
            }
            Instant startTime = OffsetDateTime.parse(lastObj.getString("date_start")).toInstant();
            Instant endTime = startTime.plus((long) (lastObj.getDouble("lap_duration") * 1000), ChronoUnit.MILLIS);
            String colorString;
            try {
                colorString = object.getString("team_colour");
            } catch (JSONException e) {
                System.out.println(e.getMessage());
                System.out.println("Error in fetchDriverInfo");
                colorString = "000000";
            }
            drivers.add(
                new Driver(
                    object.getString("name_acronym"),
                    object.getInt("driver_number"),
                    endTime,
                    lastObj.getInt("lap_number"),
                    colorString));
        }
        return drivers;
    }

    /**
     * Fetches pitstop details for a specific race session.
     * 
     * @param sessionkey The session key identifying the race session.
     * @return A list of Pitstop objects containing pitstop details for the session.
     */
    public List<Pitstop> fetchPitInfo(int sessionkey) {
        JSONArray pitData = extractRaceData(pitURL, sessionkey);
        List<Pitstop> pitStops = new ArrayList<>();
        for (int i = 0; i < pitData.length(); i++) {
            try {
                JSONObject object = pitData.getJSONObject(i);
                Integer driverID = object.getInt("driver_number");
                Integer lapNum = object.getInt("lap_number");
                Double duration = object.getDouble("pit_duration");
                if (driverID != null || lapNum != null || duration != null) {
                    pitStops.add(new Pitstop(
                        driverID,
                        lapNum,
                        duration,
                        OffsetDateTime.parse(object.getString("date")).toInstant()));
                }
            } catch (JSONException e) {
                System.out.println(e.getMessage());
                System.out.println("Error in fetchPitInfo");
                i++;
            }
        }
        return pitStops;
    }

    /**
     * Fetches all race sessions for a given year.
     * 
     * @param year The year of the season.
     * @return A JSONArray containing race session data for the given year.
     */
    private JSONArray fetchAllRacesForYear(int year) {
        String urlString = sessionURL + "session_type=Race&session_name=Race&year=" + year;
        try {
            String object = Jsoup.connect(urlString).ignoreContentType(true).header("Accept", "text/javascript").get()
                .body().text();
            JSONArray json = new JSONArray(object);
            return json;
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    /**
     * Extracts race data from a specified API endpoint for a given session key.
     * 
     * @param url        The API endpoint URL.
     * @param sessionKey The session key for the race session.
     * @return A JSONArray containing race data for the session.
     */
    private JSONArray extractRaceData(String url, int sessionKey) {
        String urlString = url + "session_key=" + sessionKey;
        try {
            String object = Jsoup.connect(urlString).ignoreContentType(true).header("Accept", "text/javascript").get()
                .body().text();
            JSONArray json = new JSONArray(object);
            return json;
        } catch (IOException e) {
            System.out.println(e.getMessage());
            System.out.println("Error in extractRaceData");
            return null;
        }
    }

    /**
     * Fetches the last JSONObject in the JSONArray.
     * 
     * @param sessionkey The session key identifying the race session.
     * @param driverID   The ID of the driver.
     * @return The last JSON object in JSON Array.
     */
    private JSONObject fetchLastObject(int sessionkey, int driverID) {
        String urlString = lapURL + "session_key=" + sessionkey + "&driver_number=" + driverID;
        try {
            String object = Jsoup.connect(urlString).ignoreContentType(true).header("Accept", "text/javascript").get()
                .body().text();
            JSONArray json = new JSONArray(object);
            if (json.length() - 1 != -1) {
                JSONObject jsonObject = jsonChecker(json, 1);
                return jsonObject;
            }
            return null;
        } catch (IOException e) {
            System.out.println(e.getMessage());
            System.out.println("Error in extractEnd");
            return null;
        }
    }

    /**
     * Iterates through JSON objects to check if last object in the JSON array causes an exception.
     * 
     * @param jsonObjects The JSON array containing lap data.
     * @param i           The index offset to check.
     * @return The last object of a JSON array.
     */
    private JSONObject jsonChecker(JSONArray jsonObjects, int i) {
        boolean test = true;
        JSONObject object = jsonObjects.getJSONObject(jsonObjects.length() - i);
        while (test) {
            try {
                Double lapTime = object.getDouble("lap_duration");
                test = false;
                return object;
            } catch (JSONException e) {
                object = jsonObjects.getJSONObject(jsonObjects.length() - i++);
            }
        }
        return null;
    }
}