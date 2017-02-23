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

        while (bufferedImage.getRGB(startX, y) == replacedColor) {
            bufferedImage.setRGB(startX, y, rgb);
            startX--;
        }

        while (bufferedImage.getRGB(finishX, y) == replacedColor) {
            bufferedImage.setRGB(startX, y, rgb);
            finishX++;
        }

        for (int k = startX; k < finishX; ++k) {

        }
    }
}
