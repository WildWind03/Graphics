package ru.nsu.fit.g14201.chirikhin.isolines.view;

import ru.nsu.fit.g14201.chirikhin.isolines.util.Util;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

public class ColorMap {
    private final List<Integer[]> colors;

    public ColorMap(List<Integer[]> colors) {
        this.colors = colors;
    }

    void draw(BufferedImage bufferedImage, List<Double> values) throws Exception {
        for (int i = 0; i < bufferedImage.getHeight(); ++i) {
            for (int k = 0; k < bufferedImage.getWidth(); ++k) {
                double value = Util.function(Util.toRealX(k, bufferedImage.getWidth()), Util.toRealY(i, bufferedImage.getHeight()));
                bufferedImage.setRGB(k, i, getColor(colors, values, value).getRGB());
            }
        }


    }

    private Color getColor(List<Integer[]> colors, List<Double> values, double value) throws Exception {
        for (int i = 0; i < values.size() - 1; ++i) {
            double curValue = values.get(i);
            double nextValue = values.get(i + 1);

            if (value >= curValue && value <= nextValue) {
                Integer[] color = colors.get(i);
                return new Color(color[0], color[1], color[2]);
            }
        }

        throw new Exception("");
    }
}
