package ru.nsu.fit.g14201.chirikhin.isolines.view;

import com.chirikhin.swing.util.DoubleUtil;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

public class ColorPaletteRecordsDrawer implements Drawer {

    private final List<Double> points;

    public ColorPaletteRecordsDrawer(List<Double> points) {
        this.points = points;
    }

    @Override
    public void draw(BufferedImage bufferedImage) {
        Graphics2D graphics2D = bufferedImage.createGraphics();
        graphics2D.setColor(Color.WHITE);
        graphics2D.fillRect(0, 0, bufferedImage.getWidth(), bufferedImage.getHeight());
        graphics2D.setColor(Color.BLACK);

        double step = (double) bufferedImage.getWidth() / (double) (points.size() - 1);

        for (int i = 1; i < points.size() - 1; ++i) {
            graphics2D.drawString(DoubleUtil.getDouble(points.get(i), 2), (int) (step * i - step * 0.2d), (int) (bufferedImage.getHeight() * 0.6d));
        }

        graphics2D.dispose();
    }
}
