package graphics;

import java.awt.image.BufferedImage;
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
    private int rgb;
    private BufferedImage bufferedImage;
    private int replacedColor;

    public SpanFiller(BufferedImage bufferedImage, int x, int y, int rgb) {
        this.rgb = rgb;
        this.bufferedImage = bufferedImage;
        points.push(new Point(x, y));
        replacedColor = bufferedImage.getRGB(x, y);
    }

    public void fill() {
        Point point = points.pop();
        int x = point.getX();
        int y = point.getY();

        int startX = x;
        int finishX = x;

        while (startX >= 0 && bufferedImage.getRGB(startX, y) == replacedColor) {
            bufferedImage.setRGB(startX, y, rgb);
            startX--;
        }

        while (finishX < bufferedImage.getWidth() && bufferedImage.getRGB(finishX, y) == replacedColor) {
            bufferedImage.setRGB(startX, y, rgb);
            finishX++;
        }

        boolean isPrevAboveAdded = false;
        boolean isPrevUnderAdded = false;


        for (int k = startX; k < finishX; ++k) {
            if (bufferedImage.getRGB(k, y + 1) != replacedColor) {
                if (!isPrevAboveAdded) {
                    isPrevAboveAdded = true;
                    points.push(new Point(k, y + 1));
                }
            } else {
                isPrevAboveAdded = false;
            }

            if (bufferedImage.getRGB(k, y - 1) != replacedColor) {
                if (!isPrevUnderAdded) {
                    isPrevUnderAdded = true;
                    points.push(new Point(k, y - 1));
                }
            } else {
                isPrevUnderAdded = false;
            }
        }

        if (!points.isEmpty()){
            fill();
        }
    }
}
