package Formula1;

import edu.macalester.graphics.*;
import java.awt.Color;


/**
 * This ScreenNode class represents a node in a linked data structure designed to manage and display
 * information about a F1 race.
 * 
 * Includes details about a specific race, manages a visual representation, and allows traversal
 * through linked nodes.
 */
public class ScreenNode {
    private ScreenNode next, prev;
    private GraphicsText nameText, yearText, dateText;
    private String name, date;
    private Integer year;
    private Race race;
    private Color bgColor;

    /**
     * Constructs a screen node with details about a specific race.
     * 
     * @param race  The Race object containing race metadata
     * @param name  The name of the race 
     * @param date  The date of the race in a readable format
     * @param year  The year the race took place
     */
    public ScreenNode(Race race, String name, String date, Integer year) {
        this.name = name;
        this.date = date;
        this.year = year;
        this.race = race;
        this.bgColor = Color.WHITE;

        next = null;
        prev = null;

        setVisuals(year);
    }

    /**
     * Constructs an empty screen node and initializes a node with no race details.
     */
    public ScreenNode() {
        next = null;
        prev = null;
    }

    /**
     * Sets the next node in the linked structure
     * 
     * @param node Sets as the previous node
     */
    public void setNext(ScreenNode node) {
        next = node;
    }

    /**
     * Sets the previous node in the linked structure.
     * 
     * @param node The screen node to set as the previous node
     */
    public void setPrev(ScreenNode node) {
        prev = node;
    }

    /**
     * Gets the next node in the linked structure.
     * 
     * @return The next screen node
     */
    public ScreenNode getNext() {
        return next;
    }

    /**
     * Gets the previous node in the linked structure.
     * 
     * @return The previous screen node
     */
    public ScreenNode getPrev() {
        return prev;
    }

    /**
     * Gets the race object associated with this node.
     * 
     * @return The race object representing the race details
     */
    public Race getRace() {
        return race;
    }

    /**
     * Initializes the visual components for the node like the race's name, date, and year.
     * 
     * @param years The year of the race to be displayed
     */
    private void setVisuals(Integer years) {
        nameText = new GraphicsText(name);
        dateText = new GraphicsText(date);
        yearText = new GraphicsText(year.toString());
    }

    /**
     * Sets the background color for the node's visual representation.
     * 
     * @param color The color to set as the background.
     */
    public void setBackgroundColor(Color color) {
        this.bgColor = color;
    }

    /**
     * Gets the background color for the node's visual representation.
     * 
     * @return The current background color
     */
    public Color getBackgroundColor() {
        return bgColor;
    }

    /**
     * Displays the visual representation of the node on the canvas like the race name, year, and date
     * 
     * @param canvas The canvas on which to display the node's visuals
     */
    public void display(CanvasWindow canvas) {
        canvas.removeAll();
        canvas.setBackground(bgColor);

        nameText.setCenter(canvas.getWidth() / 2, canvas.getHeight() / 2 - 50);
        yearText.setCenter(canvas.getWidth() / 2, canvas.getHeight() / 2);
        dateText.setCenter(canvas.getWidth() / 2, canvas.getHeight() / 2 + 50);

        canvas.add(nameText);
        canvas.add(yearText);
        canvas.add(dateText);
    }
}
