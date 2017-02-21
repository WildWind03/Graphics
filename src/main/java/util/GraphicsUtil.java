package util;

import java.awt.image.BufferedImage;
import java.util.logging.Logger;

public class GraphicsUtil {
    private static final Logger logger = Logger.getLogger(GraphicsUtil.class.getName());

    private static int DEFAULT_COLOR = 0x000000;

    private GraphicsUtil() {

    }

    public static void drawLine(BufferedImage bufferedImage, int x0, int y0, int x1, int y1) {
        int deltaX = Math.abs(x1 - x0);
        int deltaY = Math.abs(y1 - y0);

        int error = 0;
        int deltaError = deltaY;

        int y = y0;

        for (int x = x0; x < x1; ++x) {
            bufferedImage.setRGB(x, y, DEFAULT_COLOR);
            error += error + deltaError;

            if (error * 2 >= deltaX) {
                y += 1;
                error = error - deltaX;
            }
        }
    }
}
