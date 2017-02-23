package util;

import java.awt.image.BufferedImage;
import java.util.Stack;
import java.util.logging.Logger;

public class GraphicsUtil {
    private static final Logger logger = Logger.getLogger(GraphicsUtil.class.getName());

    public static class Point {
        private int x;
        private int y;

        Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }
    }

    private static int DEFAULT_COLOR = 0x000000;

    private GraphicsUtil() {

    }

    public static void drawLine(BufferedImage bufferedImage, int x0, int y0, int x1, int y1) {
        int lengthX = Math.abs(x1 - x0);
        int lengthY = Math.abs(y1 - y0);

        int dx = (x1 - x0 >= 0 ? 1 : -1);
        int dy = (y1 - y0 >= 0 ? 1 : -1);

        int length = Math.max(lengthX, lengthY);

        try {
            if (0 == length) {
                bufferedImage.setRGB(x0, y0, DEFAULT_COLOR);
            }

            if (lengthX >= lengthY) {
                int y = y0;
                int x = x0;
                int d = -1 * lengthX;

                length++;
                while (length-- != 0) {
                    bufferedImage.setRGB(x, y, DEFAULT_COLOR);
                    x += dx;
                    d += 2 * lengthY;

                    if (d > 0) {
                        d -= 2 * lengthX;
                        y += dy;
                    }
                }
            } else {
                int x = x0;
                int y = y0;
                int d = -1 * lengthY;

                length++;
                while (length-- != 0) {
                    bufferedImage.setRGB(x, y, DEFAULT_COLOR);
                    y += dy;
                    d += 2 * lengthX;

                    if (d > 0) {
                        d -= 2 * lengthY;
                        x += dx;
                    }
                }

            }
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void drawHexagon(BufferedImage bufferedImage, int x, int y, int lineLength) {
        Point point = fromCellPositionToCoordinates(x, y, lineLength);
        int realX = point.getX();
        int realY = point.getY();

        int endLeftX = realX - getHorizontalLength(lineLength) / 2;
        int endLeftY = realY + getVerticalPart(lineLength);

        drawLine(bufferedImage, realX, realY, endLeftX, endLeftY);
        drawLine(bufferedImage, endLeftX, endLeftY, endLeftX, endLeftY + lineLength);
        drawLine(bufferedImage, endLeftX, endLeftY + lineLength, realX, endLeftY + lineLength + getVerticalPart(lineLength));

        int endRightX = realX + getHorizontalLength(lineLength) / 2;
        int endRightY = realY + getVerticalPart(lineLength);

        drawLine(bufferedImage, realX, realY, endRightX, endRightY);
        drawLine(bufferedImage, endRightX, endRightY, endRightX, endRightY + lineLength);
        drawLine(bufferedImage, endRightX, endRightY + lineLength, realX, endLeftY + lineLength + getVerticalPart(lineLength));

    }

    public static Point fromCellPositionToCoordinates(int x0, int y0, int lineLength) {
        final int horizontalLength = getHorizontalLength(lineLength);
        final double c = getVerticalPart(lineLength);

        final double verticalLength = 2 * c + lineLength;

        if (0 == y0 % 2) {
            return new Point((int) (x0 * horizontalLength + horizontalLength / 2), (int) (y0 * verticalLength) - y0 * getVerticalPart(lineLength) );
        } else {
            return new Point(x0 * horizontalLength + horizontalLength, (int) (y0 * verticalLength) - y0 * getVerticalPart(lineLength));
        }
    }

    public static int getHorizontalLength(int lineLength) {
        final double k = 1.73;
        return (int) (k * lineLength);
    }

    public static int getVerticalPart(int lineLength) {
        int horizontalLength = getHorizontalLength(lineLength);
        return (int) Math.sqrt(lineLength * lineLength - (horizontalLength * horizontalLength) / 4);
    }

    public static int getVerticalLength(int lineLength) {
        return 2 * getVerticalPart(lineLength) + lineLength;
    }
}
