package ru.nsu.fit.g14201.chirikhin.isolines.view;

import ru.nsu.fit.g14201.chirikhin.isolines.model.Function;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class ColorMap {
    private final List<Color> colors;

    public ColorMap(List<Color> colors) {
        this.colors = colors;
    }
    void draw(Function function, BufferedImage bufferedImage) throws Exception {
        int max = function.get(0, 0);
        int min = function.get(0, 0);

        for (int i = 0; i < bufferedImage.getWidth(); ++i) {
            for (int k = 0; k < bufferedImage.getHeight(); ++k) {
                int value = function.get(i, k);
                if (value > max) {
                    max = value;
                }

                if (value < min) {
                    min = value;
                }
            }
        }

        List<Integer> values = new ArrayList<>();
        int step = (max - min) / colors.size();

        for (int i = 0; i < colors.size(); ++i) {
            values.add(min + i * step);
        }

        values.add(max);

        for (int i = 0; i < bufferedImage.getHeight(); ++i) {
            for (int k = 0; k < bufferedImage.getWidth(); ++k) {
                int value = function.get(k, i);
                bufferedImage.setRGB(k, i, getColor(colors, values, value).getRGB());
            }
        }


    }

    private Color getColor(List<Color> colors, List<Integer> values, int value) throws Exception {
        for (int i = 0; i < values.size() - 1; ++i) {
            int curValue = values.get(i);
            int nextValue = values.get(i + 1);

            if (value >= curValue && value <= nextValue) {
                return colors.get(i);
            }
        }

        throw new Exception("");
    }
}
