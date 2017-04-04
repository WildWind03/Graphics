package ru.nsu.fit.g14201.chirikhin.isolines.view;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

public class ColorPaletteDrawer implements Drawer {
    private final List<Integer[]> colors;

    public ColorPaletteDrawer(List<Integer[]> colors) {
        this.colors = colors;
    }

    @Override
    public void draw(BufferedImage bufferedImage) {
        Graphics2D graphics2D = bufferedImage.createGraphics();
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();

        int step = width / colors.size();

        for (int i = 0; i < colors.size(); ++i) {
            Integer[] color = colors.get(i);
            graphics2D.setColor(new Color(color[0], color[1], color[2]));
            graphics2D.fillRect(i * step, 0, i * step + step, height);
        }

        graphics2D.dispose();
    }
}
