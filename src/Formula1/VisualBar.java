package Formula1;

import java.awt.Color;

import edu.macalester.graphics.CanvasWindow;
import edu.macalester.graphics.GraphicsGroup;
import edu.macalester.graphics.Rectangle;

/**
 * This VisualBar class represents a visual bar for scolling that allows users to interactively
 * navigate a graphical area within the canvas.
 * 
 * Includes a horizontal bar and a slider, where the slider's position controls the offset of the
 * displayed content.
 */
public class VisualBar {
    private final GraphicsGroup graphGroup;

    private final double canvasWidth;

    private final Rectangle bar;
    private final Rectangle slider;

    private double sliderWidth;
    private double barRatio;

    /**
     * Constructs an instance of the VisualBar
     * 
     * @param canvas     The canvas where the content is displayed
     * @param graphGroup The graphical group whose position is controlled by the sidebar.
     * @param contWidth  The width of the graphical content that exceeds the canvas
     */
    public VisualBar(CanvasWindow canvas, GraphicsGroup graphGroup, Double contWidth) {
        this.graphGroup = graphGroup;
        this.canvasWidth = canvas.getWidth();

        sliderWidth = 30;
        barRatio = (contWidth / canvasWidth);

        bar = new Rectangle(0, canvas.getHeight() - 30, canvasWidth, 5);
        bar.setFillColor(Color.GRAY);

        slider = new Rectangle(0, canvas.getHeight() - 35, sliderWidth, 15);
        slider.setFillColor(Color.BLACK);

        canvas.add(bar);
        canvas.add(slider);

        barAnimate(canvas);
    }

    /**
     * Handles the drag events on the slider to adjust the content's position relative to the bar's
     * slider movement.
     * 
     * @param canvas The canvas where the drag movements are registered.
     */
    private void barAnimate(CanvasWindow canvas) {
        canvas.onDrag(event -> {
            double newX = event.getPosition().getX() - sliderWidth / 2;
            newX = Math.max(0, Math.min(newX, canvasWidth - sliderWidth));

            slider.setPosition(newX, slider.getY());
            double graphMove = -(newX * barRatio);
            graphGroup.setPosition(graphMove, graphGroup.getY());
        });
    }
}