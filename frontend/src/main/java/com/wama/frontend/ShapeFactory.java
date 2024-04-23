package com.wama.frontend;

import java.util.Random;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.animation.TranslateTransition;
import javafx.util.Duration;

public class ShapeFactory {
    /**
     * Creates a rectangle with specified parameters
     * @param width The width of the rectangle
     * @param height The height of the rectangle
     * @param color The fill color of the rectangle
     * @param x The x-coordinate of the upper-left corner of the rectangle
     * @param y The y-coordinate of the upper-left corner of the rectangle
     * @return A configured Rectangle object
     */
    private static Rectangle createRectangle(double width, double height, Color color, double x, double y) {
        Rectangle rectangle = new Rectangle(width, height);
        rectangle.setFill(color);
        rectangle.setX(x);
        rectangle.setY(y);
        rectangle.setSmooth(false);
        return rectangle;
    }

    /**
     * Generates specified number of rows of rectangles on the given Pane and animates them from the specified off-screen side
     * @param root The Pane on which to add the rectangles
     * @param numRows The number of rows of rectangles to create
     * @param initialX The x-coordinate of the first rectangle of the first row
     * @param initialY The y-coordinate of the first rectangle of the first row
     * @param transitionDuration The duration of the animation in seconds
     * @param fromLeft Whether the transition comes in from the left side (true) or right side (false) of the window
     */
    public static void generateRectangles(Pane root, int numRows, int initialX, int initialY, double transitionDuration, boolean fromLeft) {
        Random rand = new Random();
        int baseWidth = 40;
        int verticalGap = 20;
        int previousHeight = 0;
        int startY = initialY;

        for (int row = 0; row < numRows; row++) {
            int height = 15 + rand.nextInt(21);
            startY += (row == 0) ? 0 : (previousHeight + verticalGap);
            int startX;

            // calculate total width of rectangles in row
            int totalWidth = 0;
            for (int i = 0; i < 5; i++) {
                totalWidth += baseWidth + rand.nextInt(101);
            }

            // calculate starting x-coord based on direction and total width
            startX = (fromLeft) ? initialX : (int) (root.getWidth() - totalWidth - 10);  // 10 for gap
            for (int i = 0; i < 5; i++) {
                int width = baseWidth + rand.nextInt(101);
                int gap = 10 + rand.nextInt(21);
                Color color = Color.rgb(rand.nextInt(156) + 100, rand.nextInt(156) + 100, rand.nextInt(156) + 100);
                Rectangle rect = createRectangle(width, height, color, startX, startY);

                // animation with variable direction
                TranslateTransition transition = new TranslateTransition(Duration.seconds(transitionDuration), rect);
                if (fromLeft)
                    transition.setFromX(-900); // start from -900 pixels off-screen (left side)
                else
                    transition.setFromX(root.getWidth()); // start from right edge of the window
                transition.setToX(0);
                transition.play();

                root.getChildren().add(rect);
                startX += width + gap;
            }

            previousHeight = height;
        }
    }
}