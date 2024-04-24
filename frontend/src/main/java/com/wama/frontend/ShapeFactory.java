package com.wama.frontend;

import java.util.Random;
import javafx.animation.TranslateTransition;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class ShapeFactory {

    private static final int BASE_WIDTH = 40;
    private static final int RECTS_PER_ROW = 7;
    private static final int MIN_HEIGHT = 15;
    private static final int MAX_HEIGHT_INCREMENT = 20;
    private static final int VERTICAL_GAP = 20;
    private static final int MIN_WIDTH_INCREMENT = 101;
    private static final int GAP_INCREMENT = 21;
    private static final int MIN_COLOR_VALUE = 100;
    private static final int COLOR_RANGE = 156;
    private static final int MIN_ANIM_DURATION = 2;
    private static final int ANIM_DURATION_RANGE = 2;
    private static final int OFFSET_RANGE = 50;  // Max offset range for random end positions per row

    private static Rectangle createRectangle(double width, double height, Color color, double x, double y) {
        Rectangle rectangle = new Rectangle(width, height);
        rectangle.setFill(color);
        rectangle.setX(x);
        rectangle.setY(y);
        rectangle.setSmooth(false);
        return rectangle;
    }

    private static Color randomColor(Random rand) {
        return Color.rgb(
            rand.nextInt(COLOR_RANGE) + MIN_COLOR_VALUE, 
            rand.nextInt(COLOR_RANGE) + MIN_COLOR_VALUE, 
            rand.nextInt(COLOR_RANGE) + MIN_COLOR_VALUE
        );
    }

    public static void generateRectangles(Pane root, int numRows, int initialX, int initialY, int dX) {
        Random rand = new Random();
        int startY = initialY;
        int previousHeight = 0;
        boolean animateRight = dX >= initialX;

        for (int row = 0; row < numRows; row++) {
            int height = MIN_HEIGHT + rand.nextInt(MAX_HEIGHT_INCREMENT);
            startY += (row == 0) ? 0 : (previousHeight + VERTICAL_GAP);
            double randomDuration = MIN_ANIM_DURATION + rand.nextDouble() * ANIM_DURATION_RANGE;

            // calculate row-specific random endX within OFFSET_RANGE of the base endX
            int rowEndX = dX + (animateRight ? rand.nextInt(OFFSET_RANGE) : -rand.nextInt(OFFSET_RANGE));
            int startX = initialX;

            int fromX = (int) (animateRight ? -900 : root.getWidth() + 900);  // uniform off-screen start position for the row

            for (int i = 0; i < RECTS_PER_ROW; i++) {
                int width = BASE_WIDTH + rand.nextInt(MIN_WIDTH_INCREMENT);
                int gap = 10 + rand.nextInt(GAP_INCREMENT);
                Color color = randomColor(rand);
                Rectangle rect = createRectangle(width, height, color, startX, startY);

                TranslateTransition transition = new TranslateTransition(Duration.seconds(randomDuration), rect);
                transition.setFromX(fromX);
                transition.setToX(rowEndX); // all rectangles in row transition to the row-specific endX
                transition.play();

                root.getChildren().add(rect);
                startX += width + gap;
            }
            
            previousHeight = height;
        }
    }
}