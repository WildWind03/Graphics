package ru.nsu.fit.g14201.chirikhin.isolines.view;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

public class Legend {
    private final List<Color> colors;

    public Legend(List<Color> colors) {
        this.colors = colors;
    }

    public void draw(BufferedImage bufferedImage) {
        Graphics2D graphics2D = bufferedImage.createGraphics();
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();

        int step = width / colors.size();

        for (int i = 0; i < colors.size(); ++i) {
            graphics2D.setColor(colors.get(i));
            graphics2D.fillRect(i * step, 0, i * step + step, height);
        }

        graphics2D.dispose();
    }
}
