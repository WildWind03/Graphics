package graphics;

import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Stack;
import java.util.logging.Logger;

public class SpanFiller {
    private static final Logger logger = Logger.getLogger(SpanFiller.class.getName());

    private static class Point {
        private int x, y;

        private Point(int x, int y) {
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

    private final Stack<Point> points = new Stack<Point>();
    private final int[] fillColor;
    private final BufferedImage bufferedImage;
    private final int replacedColor[] = new int[3];

    public SpanFiller(BufferedImage bufferedImage, int x, int y, int[] fillColor) {
        this.fillColor = fillColor;
        this.bufferedImage = bufferedImage;
        points.push(new Point(x, y));

        bufferedImage.getRaster().getPixel(x, y, replacedColor);
    }

    public void applyFiller() {
        Point point = points.peek();
        int x = point.getX();
        int y = point.getY();

        int[] currentColor = new int[3];

        if (!Arrays.equals(bufferedImage.getRaster().getPixel(x, y, currentColor), fillColor)) {
            fill();
        }
    }

    private void fill() {
        Point point = points.pop();
        int x = point.getX();
        int y = point.getY();

        int startX = x - 1;
        int finishX = x + 1;

        int[] currentColor = new int[3];

        while (true) {
            if (startX < 0) {
                break;
            }

            bufferedImage.getRaster().getPixel(startX, y, currentColor);

            if (Arrays.equals(currentColor, replacedColor)) {
                bufferedImage.getRaster().setPixel(startX--, y, fillColor);
            } else {
                break;
            }
        }

        while(true) {
            if (finishX >= bufferedImage.getWidth()) {
                break;
            }

            bufferedImage.getRaster().getPixel(finishX , y, currentColor);

            if (Arrays.equals(currentColor, replacedColor)) {
                bufferedImage.getRaster().setPixel(finishX++, y, fillColor);
            } else {
                break;
            }
        }

        boolean isPrevAboveAdded = false;
        boolean isPrevUnderAdded = false;


        for (int k = startX + 1; k < finishX; ++k) {
            if (y + 1 < bufferedImage.getHeight()) {

                bufferedImage.getRaster().getPixel(k, y + 1, currentColor);

                if (Arrays.equals(currentColor, replacedColor)) {
                    if (!isPrevAboveAdded) {
                        isPrevAboveAdded = true;
                        points.push(new Point(k, y + 1));
                    }
                } else {
                    isPrevAboveAdded = false;
                }
            }

            if (y - 1 >= 0) {
                bufferedImage.getRaster().getPixel(k, y - 1, currentColor);

                if (Arrays.equals(currentColor, replacedColor)) {
                    if (!isPrevUnderAdded) {
                        isPrevUnderAdded = true;
                        points.push(new Point(k, y - 1));
                    }
                } else {
                    isPrevUnderAdded = false;
                }
            }
        }

        if (!points.isEmpty()){
            fill();
        }
    }
}
