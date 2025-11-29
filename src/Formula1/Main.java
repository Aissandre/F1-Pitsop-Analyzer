package Formula1;

import java.awt.Color;

import edu.macalester.graphics.CanvasWindow;
import edu.macalester.graphics.FontStyle;
import edu.macalester.graphics.GraphicsText;
import edu.macalester.graphics.Image;
import edu.macalester.graphics.Rectangle;
import edu.macalester.graphics.TextAlignment;
import edu.macalester.graphics.ui.Button;


/**
 * This Main class represents the main menu interface for the F1 Pit Stop Data Visualization. It
 * provides a graphical user interface that allows users to learn more about pit stop strategies and
 * then choose a race year to further explore.
 * 
 * Includes a background image, introductory text, and buttons for navigating to specific race
 * years.
 */
public class Main {
    private CanvasWindow canvas;
    private GraphicsText text;
    private Image bg;
    private GraphicsText introText;
    private Rectangle introTextBG;

    /**
     * Constructs a menu object that initializes the main menu interface for the F1 PitStop Data
     * Visualization program
     */
    public Main() {
        canvas = new CanvasWindow("Map Verstackpen", 1280, 720);

        text = new GraphicsText("PitStop Performance");
        text.setFillColor(Color.WHITE);
        text.setFontSize(60);
        text.setStrokeWidth(3);
        text.setFontStyle(FontStyle.BOLD_ITALIC);

        text.setCenter(canvas.getWidth() / 2, canvas.getHeight() / 2 - 200);

        introText = new GraphicsText(
            "Formula 1 is a high-speed motorsport where strategy is just as important as speed. A key part of this strategy is the pit stop-a quick pause\n"
                +
                "during the race to change tires or make adjustments. The timing and duration of these pit stops can greatly influence a driver's race.\n"
                +
                "Choose a year below to take the wheel and explore!");

        introText.setFillColor(Color.WHITE);
        introText.setAlignment(TextAlignment.CENTER);
        introText.setFont("Arial", FontStyle.ITALIC, 20);
        introText.setCenter(canvas.getWidth() / 2, canvas.getHeight() / 2);

        introTextBG = new Rectangle(0, 0, introText.getWidth() + 20, introText.getHeight() + 10);
        introTextBG.setFillColor(new Color(50, 0, 0, 150));
        introTextBG.setCenter(introText.getCenter());

        bg = new Image("bg.jpg");
        bg.setCenter(canvas.getCenter().getX(), canvas.getCenter().getY());
        bg.setScale(2);
    }

    /**
     * Main method that creates and starts Menu
     * 
     * @param args
     */
    public static void main(String[] args) {
        Main menu = new Main();
        menu.start();
    }

    /**
     * Starts the menu by displaying the background, text, and buttons onto the canvas. Adds buttons for
     * interacting and navigating to different race years.
     */
    public void start() {
        canvas.removeAll();
        canvas.add(bg);
        canvas.add(introTextBG);
        canvas.add(text);
        canvas.add(introText);

        Button button2023 = new Button("2023 Races");
        button2023.setCenter(canvas.getWidth() / 2 - 100, canvas.getHeight() / 2 + 250);
        canvas.add(button2023);

        Button button2024 = new Button("2024 Races");
        button2024.setCenter(canvas.getWidth() / 2 + 100, canvas.getHeight() / 2 + 250);
        canvas.add(button2024);

        button2023.onClick(() -> navigateToScreenManager(2023));
        button2024.onClick(() -> navigateToScreenManager(2024));
        canvas.draw();
    }

    /**
     * Navigates to the ScreenManager class for the specified race year.
     * 
     * @param year The year of the races to display
     */
    private void navigateToScreenManager(int year) {
        ScreenManager manager = new ScreenManager(year, this);
        manager.display(canvas);
    }
}