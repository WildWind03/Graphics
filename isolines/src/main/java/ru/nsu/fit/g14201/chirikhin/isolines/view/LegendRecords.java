package ru.nsu.fit.g14201.chirikhin.isolines.view;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

public class LegendRecords {
    void draw(BufferedImage bufferedImage, List<Double> points) {
        Graphics2D graphics2D = bufferedImage.createGraphics();
        graphics2D.setColor(Color.WHITE);
        graphics2D.fillRect(0, 0, bufferedImage.getWidth(), bufferedImage.getHeight());
        graphics2D.setColor(Color.BLACK);

        double step = (double) bufferedImage.getWidth() / (double) (points.size() - 1);

        for (int i = 1; i < points.size() - 1; ++i) {
            graphics2D.drawString(toString(points.get(i)), (int) (step * i - step * 0.04d), (int) (bufferedImage.getHeight() * 0.6d));
        }

        graphics2D.dispose();
    }

    private String toString(double d) {
        String str = Double.toString(d);
        int posOfWholePart = str.indexOf('.');
        return str.substring(0, Math.min(posOfWholePart + 2, str.length()));
    }

}
