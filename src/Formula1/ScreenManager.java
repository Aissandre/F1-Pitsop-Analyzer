package Formula1;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.awt.Color;

import edu.macalester.graphics.CanvasWindow;
import edu.macalester.graphics.ui.Button;

/**
 * The ScreenManager class manages the navigation and display for a series of screens representing
 * Formula 1 race data for a specific season. It allows users to view race details, navigate between
 * races, and access visualized data.
 */
public class ScreenManager {
    private ScreenNode head, tail;
    private ScreenNode current;

    private API openf1;

    private Button nextButton;
    private Button prevButton;

    private Main menu;
    private Button backtoMenu;

    private Button dataButton;

    /**
     * Constructs a screen manager for managing and displaying screens representing F1 race data for a
     * specific season. Initializes a linked structure for navigating through race data and sets up the
     * data retrieval machanism.
     * 
     * @param year The year of the F1 season for which race data will be displayed
     * @param menu The menu instance for navigating back to the main menu
     */
    public ScreenManager(int year, Main menu) {
        this.menu = menu;
        head = tail = new ScreenNode();
        head.setNext(tail);
        tail.setPrev(head);

        openf1 = new API();
        initializeRaces(year);
    }

    /**
     * Navigates to the next screen in the sequence.
     * 
     * @param canvas The canvas to render the next screen
     */
    public void next(CanvasWindow canvas) {
        if (current != null && current.getNext() != tail) {
            current = current.getNext();
            display(canvas);
        }
    }

    /**
     * Navigates to the previous screen in the sequence.
     * 
     * @param canvas The canvas to render the previous screen
     */
    public void previous(CanvasWindow canvas) {
        if (current != null && current.getPrev() != head) {
            current = current.getPrev();
            display(canvas);
        }
    }

    /**
     * Displays the current screen on the canvas
     * 
     * @param canvas The canvas where the current screen will be displayed
     */
    public void display(CanvasWindow canvas) {
        if (current != null) {
            canvas.removeAll();
            current.display(canvas);
            addButtons(canvas);
        }
    }

    /**
     * Initializes the screens with race data for the specified season.
     * 
     * @param year The year of the season to initialize
     */
    private void initializeRaces(int year) {
        List<Race> raceList = openf1.getSeasonRaces(year);

        for (int i = 0; i < raceList.size(); i++) {
            ScreenNode node = new ScreenNode(
                raceList.get(i),
                raceList.get(i).getCircuitName(),
                formatDate(raceList.get(i).getDate()),
                year);
            node.setBackgroundColor(Color.LIGHT_GRAY);

            if (head.getNext() == tail) {
                head.setNext(node);
                tail.setPrev(node);
                node.setNext(tail);
                node.setPrev(head);
            } else {
                tail.getPrev().setNext(node);
                node.setPrev(tail.getPrev());
                tail.setPrev(node);
                node.setNext(tail);
            }
        }
        current = head.getNext();
    }

    /**
     * Formats an ISO 8601 date string into a more readable format.
     * 
     * @param ISODate The date string in ISO 8601 format
     * @return A formatted date string
     */
    public String formatDate(String ISODate) {
        ZonedDateTime date = ZonedDateTime.parse(ISODate);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM - dd");
        String formattedDate = date.format(formatter);
        return formattedDate;
    }

    /**
     * Adds navigation and interaction buttons to the canvas.
     * 
     * @param canvas The canvas to which the buttons will be added
     */
    private void addButtons(CanvasWindow canvas) {
        nextButton = new Button("→");
        nextButton.setCenter(canvas.getWidth() - 100, canvas.getHeight() - 350);
        nextButton.onClick(() -> next(canvas));

        prevButton = new Button("←");
        prevButton.setCenter(100, canvas.getHeight() - 350);
        prevButton.onClick(() -> previous(canvas));

        backtoMenu = new Button("Menu");
        backtoMenu.setCenter(canvas.getWidth() / 2, canvas.getHeight() / 2 - 250);
        backtoMenu.onClick(() -> {
            menu.start();
        });

        dataButton = new Button("Data");
        dataButton.setCenter(canvas.getWidth() / 2, canvas.getHeight() - 100);

        dataButton.onClick(() -> {
            Integer seshKey = current.getRace().getSessionKey();
            RaceData info = new RaceData(openf1.fetchDriverInfo(seshKey), openf1.fetchPitInfo(seshKey));
            DataVisualizer dataGraph = new DataVisualizer(canvas, this, menu, current.getRace(), info);
        });

        canvas.add(nextButton);
        canvas.add(prevButton);
        canvas.add(backtoMenu);
        canvas.add(dataButton);
    }
}
