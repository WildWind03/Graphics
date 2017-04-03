package ru.nsu.fit.g14201.chirikhin.isolines.view;

import ru.nsu.fit.g14201.chirikhin.isolines.model.Function;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class ColorMap {
    private final List<Integer[]> colors;
    private final int startX = -2;
    private final int endX = 2;
    private final int startY = -4;
    private final int endY = 4;

    public ColorMap(List<Integer[]> colors) {
        this.colors = colors;
    }

    private double toRealX(int u, int width) {
        return (endX - startX) * (double) u / (double) width + startX;
    }

    private double toRealY(int v, int height) {
        return (endY - startY) * (double) v / (double) height + startY;
    }

    void draw(Function function, BufferedImage bufferedImage) throws Exception {
        double max = function.get(0, 0);
        double min = function.get(0, 0);

        for (int i = 0; i < bufferedImage.getWidth(); ++i) {
            for (int k = 0; k < bufferedImage.getHeight(); ++k) {
                double value = function.get(toRealX(i, bufferedImage.getWidth()), toRealY(k, bufferedImage.getHeight()));

                if (value > max) {
                    max = value;
                }

                if (value < min) {
                    min = value;
                }
            }
        }

        List<Double> values = new ArrayList<>();
        double step = (max - min) / colors.size();

        for (int i = 0; i < colors.size(); ++i) {
            values.add(min + i * step);
        }

        values.add(max);

        for (int i = 0; i < bufferedImage.getHeight(); ++i) {
            for (int k = 0; k < bufferedImage.getWidth(); ++k) {
                double value = function.get(toRealX(k, bufferedImage.getWidth()), toRealY(i, bufferedImage.getHeight()));
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
