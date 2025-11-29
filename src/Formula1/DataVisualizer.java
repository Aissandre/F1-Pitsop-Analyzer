package Formula1;

import java.awt.Color;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.PriorityQueue;

import edu.macalester.graphics.CanvasWindow;
import edu.macalester.graphics.GraphicsGroup;
import edu.macalester.graphics.GraphicsText;
import edu.macalester.graphics.Line;
import edu.macalester.graphics.Rectangle;
import edu.macalester.graphics.ui.Button;

/**
 * This DataVisualizer class is responsible for graphically representing the race data on a canvas
 * and visualizes the driver's lap performances, pit stops, and other race details using graphical
 * elements.
 * 
 * Includes the interactive components like buttons to navigate back to the main menu or the races
 * screen. The visualization adjusts to the race data provided and supports a detailed exploration
 * of the driver performance.
 */
public class DataVisualizer {
    private CanvasWindow canvas;

    private Button backButton;
    private Button menuButton;

    private ScreenManager manager;
    private Main menu;
    private Race race;
    private RaceData raceData;
    private GraphicsGroup graphGroup;

    private final double XSCALE, YSCALE, PADDDING = 50;

    /**
     * Constructs a DataVisualizer object to represent race data graphically on a canvas.
     * 
     * @param canvas   The canvas window where the race data will be visualized
     * @param manager  The screen manager instance for navigation between screens
     * @param menu     The menu instance to return back to the main menu
     * @param race     The race object containing the race details
     * @param raceData The race data object containing drivers and pit stop data for the race
     */
    public DataVisualizer(CanvasWindow canvas, ScreenManager manager, Main menu, Race race, RaceData raceData) {
        this.canvas = canvas;
        this.manager = manager;
        this.menu = menu;
        this.race = race;
        this.raceData = raceData;
        this.graphGroup = new GraphicsGroup();

        XSCALE = (canvas.getWidth() - 2 * PADDDING - 50) / getTotalTime(raceData.getDrivers());
        YSCALE = (canvas.getHeight() - 2 * PADDDING) / getAllLaps(raceData.getDrivers());

        if (checkDataAvailability()) {
            start();
        } else {
            canvas.removeAll();
            GraphicsText noDataText = new GraphicsText("Not enough data to be displayed. Try another race.");
            noDataText.setFontSize(20);
            noDataText.setCenter(canvas.getWidth() / 2, canvas.getHeight() / 2);
            canvas.add(noDataText);
            System.out.println("NOT ENOUGH DATA AVAILABLE");
            addButton(canvas);
        }
    }

    /**
     * Starts the visualization by drawing the race data and associated components.
     */
    private void start() {
        canvas.removeAll();
        graphGroup.removeAll();
        canvas.add(graphGroup);
        drawDriverNames(raceData.getDrivers());

        displayRaceInfo();

        drawLapLabels();
        displayPitsops(raceData.getDriversPitstops(), raceData.getDrivers(), race);
        drawAxes();
        addButton(canvas);
        new VisualBar(canvas, graphGroup, contWidth());
    }

    /**
     * Computes the total scrollable width of the visualization based on race data.
     * 
     * @return The width required to display the race data in its entirety.
     */
    private double contWidth() {
        return PADDDING + getTotalTime(raceData.getDrivers()) * XSCALE + PADDDING;
    }

    /**
     * Checks if there is enough data to visualize the race.
     * 
     * @return true if the data is avaialble
     */
    private boolean checkDataAvailability() {
        if (raceData.getDriversPitstops().size() == 0 || raceData.getDrivers().size() == 0) {
            return false;
        }
        return true;
    }

    /**
     * Draws the labels for lap intervals along the Y-axis.
     */
    private void drawLapLabels() {
        int[] laps = new int[7];
        int lastLap = getLastLap(raceData.getDrivers());
        int lapInterval = lastLap / 6;
        for (int i = lastLap, j = laps.length - 1; i > 0; i -= lapInterval, j--) {
            laps[j] = i;
        }

        double startY = canvas.getHeight() - 70;
        double endY = 50;
        double xPos = PADDDING + 30;
        double spacing = (startY - endY) / (laps.length - 1);
        for (int i = 0; i < laps.length; i++) {
            double yPos = startY - i * spacing;
            GraphicsText lapLabel = new GraphicsText(String.valueOf(laps[i]));
            lapLabel.setCenter(xPos, yPos);
            graphGroup.add(lapLabel);
        }
    }

    /**
     * Draws the driver names and their corresponding colors along the Y-axis.
     * 
     * @param driversMap A map of the driver IDs to the Driver objects.
     */
    private void drawDriverNames(Map<Integer, Driver> driversMap) {
        double startY = canvas.getHeight() - PADDDING;
        double endY = PADDDING;
        double spacing = (startY - endY) / driversMap.size();
        double xPos = PADDDING - 10;

        int i = 0;
        for (Driver driver : driversMap.values()) {
            double yPos = startY - i * spacing;

            GraphicsText driverName = new GraphicsText(driver.getName());
            driverName.setCenter(xPos + 10, yPos);
            graphGroup.add(driverName);

            Rectangle colorBox = new Rectangle(xPos - 30, yPos - 10, 20, 20);
            colorBox.setFillColor(driver.getDriverColor());
            graphGroup.add(colorBox);
            i++;
        }
    }

    /**
     * Displays the information about the race.
     */
    private void displayRaceInfo() {
        String raceInfo = race.getCircuitName();
        GraphicsText raceDetails = new GraphicsText(raceInfo);
        raceDetails.setFontSize(18);
        raceDetails.setCenter(canvas.getWidth() / 2, (PADDDING / 2) + 50);
        graphGroup.add(raceDetails);
    }

    /**
     * Draws the pit stop data on the graph for all drivers.
     * 
     * @param pitstopsMap A map of driver IDs to priority queues of objects.
     * @param driversMap  A map of the driver IDs to the Driver objects.
     * @param race        The current Race object.
     */
    private void
        displayPitsops(Map<Integer, PriorityQueue<Pitstop>> pitstopsMap, Map<Integer, Driver> driversMap, Race race) {
        List<Map.Entry<Integer, Driver>> sortedDrivers = driversMap.entrySet().stream()
            .sorted(Comparator.comparing(entry -> entry.getValue().getRaceFinishTime(race)))
            .toList();

        int interval = 18;
        for (Map.Entry<Integer, Driver> entry : sortedDrivers) {
            Driver driver = entry.getValue();
            Color driverColor = driver.getDriverColor();

            PriorityQueue<Pitstop> pitstops = pitstopsMap.get(entry.getKey());
            Pitstop prevPitstop = null;
            double prevX = 0, prevY = 0;

            while (pitstops != null && !pitstops.isEmpty()) {
                Pitstop currPitStop = pitstops.poll();
                int pitLapNum = currPitStop.getLapNum();

                double x1 = (currPitStop.getStartOffset(race) * XSCALE) + PADDDING + interval / 1.7;
                double y = (canvas.getHeight() - (pitLapNum * YSCALE)) - PADDDING;
                double x2 = (currPitStop.getEndOffset(race) * XSCALE) + (currPitStop.getpitDuration() * 1.5) + PADDDING
                    + interval / 1.7;

                drawDataPoint(x1, y, driverColor);
                drawDataPoint(x2, y, driverColor);
                drawLine(x1, y, x2, y, driverColor);

                if (prevPitstop == null) {
                    double startingX = PADDDING + 50;
                    double startingY = canvas.getHeight() - PADDDING;
                    drawLine(startingX, startingY, x1, y, driverColor);
                } else {
                    drawLine(prevX, prevY, x1, y, driverColor);
                }

                prevX = x2;
                prevY = y;
                prevPitstop = currPitStop;
            }

            double lastX = PADDDING + (driver.getRaceFinishTime(race) * XSCALE) + interval;
            double lastY = (canvas.getHeight() - PADDDING) - (driver.getFinalLap() * YSCALE);

            drawDataPoint(lastX, lastY, driverColor);
            drawLine(prevX, prevY, lastX, lastY, driverColor);

            interval += 18;
        }
    }

    /**
     * Retrieves the total race time of the last driver to finish the race.
     * 
     * @param driverIDToDriver A map of the driver IDs to the Driver objects.
     * @return The total race time in seconds.
     */
    private long getTotalTime(Map<Integer, Driver> driverIDToDriver) {
        Optional<Driver> lastDriver = driverIDToDriver.values().stream()
            .max(Comparator.comparingLong(e -> e.getRaceFinishTime(race)));
        try {
            return lastDriver.get().getRaceFinishTime(race);
        } catch (NoSuchElementException e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * Retrieves the last lap number completed by drivers.
     * 
     * @param driverIDToDriver A map of the driver IDs to the Driver objects.
     * @return The number of the last lap.
     */
    private int getLastLap(Map<Integer, Driver> driverIDToDriver) {
        Optional<Driver> totalNumLaps = driverIDToDriver.values().stream()
            .max(Comparator.comparingInt(e -> e.getFinalLap()));
        try {
            return totalNumLaps.get().getFinalLap();
        } catch (NoSuchElementException e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * Retrieves the highest number of laps completed by drivers.
     * 
     * @param driverIDToDriver A map of driver IDs to Driver objects.
     * @return The total number of laps.
     */
    private long getAllLaps(Map<Integer, Driver> driverIDToDriver) {
        Optional<Driver> finishedDriver = driverIDToDriver.values().stream()
            .max(Comparator.comparingInt(e -> e.getFinalLap()));
        try {
            return finishedDriver.get().getFinalLap();
        } catch (NoSuchElementException e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * Draws a point on the graph to represent data.
     * 
     * @param x           The x-coordinate of the point.
     * @param y           The y-coordinate of the point.
     * @param label       An optional label for the point.
     * @param driverColor The color representing the driver.
     */
    private void drawDataPoint(double x, double y, Color driverColor) {
        Rectangle point = new Rectangle(x - 5, y - 5, 10, 10);
        point.setFillColor(driverColor);
        graphGroup.add(point);
    }


    /**
     * Draws a line between two points on the graph.
     * 
     * @param x1    The x-coordinate of the first point.
     * @param y1    The y-coordinate of the first point.
     * @param x2    The x-coordinate of the second point.
     * @param y2    The y-coordinate of the second point.
     * @param color The color of the line.
     */
    private void drawLine(double x1, double y1, double x2, double y2, Color color) {
        Line line = new Line(x1, y1, x2, y2);
        line.setStrokeColor(color);
        graphGroup.add(line);
    }


    /**
     * Adds the navigation buttons to the canvas.
     * 
     * @param canvas The canvas on which to add the buttons.
     */
    private void addButton(CanvasWindow canvas) {
        double buttonWidth = 100;
        double gap = 20;

        double centerX = canvas.getWidth() / 2;
        double bottomY = canvas.getHeight() - 700;

        backButton = new Button("Races");
        backButton.setCenter(centerX - (buttonWidth + gap), bottomY);
        backButton.onClick(() -> manager.display(canvas));

        menuButton = new Button("Menu");
        menuButton.setCenter(centerX + (buttonWidth + gap), bottomY);
        backButton.onClick(() -> manager.display(canvas));

        menuButton.onClick(() -> {
            menu.start();
        });

        canvas.add(backButton);
        canvas.add(menuButton);
    }

    /**
     * Draws the axes on the canvas for the data visualization.
     */
    private void drawAxes() {
        double xAxisY = canvas.getHeight() - PADDDING;
        double yAxisX = PADDDING + 50;

        double scrollableWidth = canvas.getWidth() * 2;
        Line xAxis = new Line(yAxisX, xAxisY, scrollableWidth, xAxisY);

        xAxis.setStrokeColor(Color.BLACK);
        graphGroup.add(xAxis);

        Line yAxis = new Line(yAxisX, xAxisY, yAxisX, PADDDING);
        yAxis.setStrokeColor(Color.BLACK);
        graphGroup.add(yAxis);

        GraphicsText xAxisLabel = new GraphicsText("Race Time Elapsed (seconds)");
        xAxisLabel.setCenter(yAxisX + 550, xAxisY + 40);
        graphGroup.add(xAxisLabel);

        GraphicsText yAxisLabel = new GraphicsText("Lap #");
        yAxisLabel.setCenter(yAxisX - 20, 30);
        graphGroup.add(yAxisLabel);
    }

}

