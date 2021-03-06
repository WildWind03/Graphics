package ru.nsu.fit.g14201.chirikhin.util;

import ru.nsu.fit.g14201.chirikhin.graphics.SpanFiller;
import ru.nsu.fit.g14201.chirikhin.support.Point;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;

public class GraphicsUtil {

    private static int DEFAULT_COLOR = 0x000000;

    private GraphicsUtil() {

    }

    public static void drawLine(BufferedImage bufferedImage, int x0, int y0, int x1, int y1, int lineLength) {
        if (1 == lineLength) {
            drawLine(bufferedImage, x0, y0, x1, y1);
        } else {
            Graphics2D graphics2D = (Graphics2D) bufferedImage.getGraphics();
            graphics2D.setStroke(new BasicStroke(lineLength));
            graphics2D.setColor(Color.BLACK);
            graphics2D.draw(new Line2D.Float(x0, y0, x1, y1));
        }
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
                int d = lengthX;

                length++;
                while (length-- != 0) {
                    bufferedImage.setRGB(x, y, DEFAULT_COLOR);
                    x += dx;
                    d -= 2 * lengthY;

                    if (d < 0) {
                        d += 2 * lengthX;
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

    public static void drawHexagon(BufferedImage bufferedImage, int x, int y, int lineLength, int maxY, int lineWidth) {
        Point<Integer> point = fromCellPositionToCoordinatesUpd(x, y, lineLength);
        int realX = point.getX();
        int realY = point.getY();

        if (0 == y % 2) {
            drawLine(bufferedImage, realX, realY, realX, realY + lineLength, lineWidth);
            drawLine(bufferedImage, realX, realY + lineLength, realX + getHalfOfHorizontalLength(lineLength), realY + lineLength + getVerticalPart(lineLength), lineWidth);
            drawLine(bufferedImage, realX + getHalfOfHorizontalLength(lineLength), realY + lineLength + getVerticalPart(lineLength), realX + getHorizontalLength(lineLength), realY + lineLength, lineWidth);
            drawLine(bufferedImage, realX + getHorizontalLength(lineLength), realY + lineLength, realX + getHorizontalLength(lineLength), realY, lineWidth);
            drawLine(bufferedImage, realX + getHorizontalLength(lineLength), realY, realX + getHalfOfHorizontalLength(lineLength), realY - getVerticalPart(lineLength), lineWidth);
            drawLine(bufferedImage, realX, realY, realX + getHalfOfHorizontalLength(lineLength), realY - getVerticalPart(lineLength), lineWidth);
        } else {
            drawLine(bufferedImage, realX, realY, realX, realY + lineLength, lineWidth);
            drawLine(bufferedImage, realX + getHorizontalLength(lineLength), realY + lineLength, realX + getHorizontalLength(lineLength), realY, lineWidth);

            if ( y == maxY - 1) {
                drawLine(bufferedImage, realX, realY + lineLength, realX + getHalfOfHorizontalLength(lineLength), realY + lineLength + getVerticalPart(lineLength), lineWidth);
                drawLine(bufferedImage, realX + getHalfOfHorizontalLength(lineLength), realY + lineLength + getVerticalPart(lineLength), realX + getHorizontalLength(lineLength), realY + lineLength, lineWidth);
            }
        }
    }

    public static void fillHexagon(BufferedImage bufferedImage, int x, int y, int lineLength, int[] color) {
        Point<Integer> point = fromCellPositionToCoordinatesUpd(x, y, lineLength);
        SpanFiller spanFiller = new SpanFiller(bufferedImage, point.getX() + getHorizontalLength(lineLength) / 2, point.getY() + lineLength / 2, color);
        spanFiller.applyFiller();
    }

    public static Point fromCellPositionToCoordinates(int x0, int y0, int lineLength) {
        final int horizontalLength = getHorizontalLength(lineLength);
        final int verticalLength = getVerticalLength(lineLength);

        final int horizontalStart = x0 * horizontalLength;

        if (0 == y0 % 2) {
            return new Point<>(horizontalStart + getHalfOfHorizontalLength(lineLength), (int) (y0 * verticalLength) - y0 * getVerticalPart(lineLength) );
        } else {
            return new Point<>(horizontalStart + horizontalLength, (int) (y0 * verticalLength) - y0 * getVerticalPart(lineLength));
        }
    }

    public static Point<Integer> fromCoordinatesToPositionInField(int x, int y, int lineLength) {
        int gridHeight = lineLength + getVerticalPart(lineLength);
        int row = y / gridHeight;
        int column;

        boolean isRowOdd = (row % 2 == 1);

        if (isRowOdd) {
            column = (x - getHalfOfHorizontalLength(lineLength)) / getHorizontalLength(lineLength);
        } else {
            column = (x / getHorizontalLength(lineLength));
        }

        int relY = y - (row * gridHeight);
        int relX;

        if (isRowOdd) {
            relX = (x - (column * getHorizontalLength(lineLength))) - getHalfOfHorizontalLength(lineLength);
        } else {
            relX = x - (column * getHorizontalLength(lineLength));
        }

        double k = (double) getVerticalPart(lineLength) / (double) getHalfOfHorizontalLength(lineLength);

        if (relY < -k * relX + getVerticalPart(lineLength)) {
            row--;
            if (!isRowOdd) {
                column--;
            }
        } else {
            if (relY < k * relX - getVerticalPart(lineLength)) {
                row--;
                if (isRowOdd) {
                    column++;
                }
            }
        }

        return new Point<>(column, row);
    }


    public static Point<Integer> fromCellPositionToCoordinatesUpd(int x0, int y0, int lineLength) {
        return (0 == y0 % 2) ? new Point<>(getHorizontalLength(lineLength) * x0, y0 * getVerticalLength(lineLength) - y0 * getVerticalPart(lineLength) + getVerticalPart(lineLength))
                : new Point<>(getHorizontalLength(lineLength) * x0 + getHalfOfHorizontalLength(lineLength), y0 * getVerticalLength(lineLength) - y0 * getVerticalPart(lineLength) + getVerticalPart(lineLength));
    }

    public static int getHorizontalLength(int lineLength) {
        final double k = 1.73;
        return (int) (k * lineLength);
    }

    public static int getVerticalPart(int lineLength) {
        return (int) Math.sqrt(((int) (lineLength * lineLength)) - (int) (getHalfOfHorizontalLength(lineLength) * getHalfOfHorizontalLength(lineLength)));
    }

    public static int getHalfOfHorizontalLength(int lineLength) {
        return getHorizontalLength(lineLength) / 2;
    }

    public static int getVerticalLength(int lineLength) {
        return 2 * getVerticalPart(lineLength) + lineLength;
    }
}
