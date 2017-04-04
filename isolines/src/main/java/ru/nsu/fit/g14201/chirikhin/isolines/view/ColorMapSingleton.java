package ru.nsu.fit.g14201.chirikhin.isolines.view;

import ru.nsu.fit.g14201.chirikhin.isolines.function.MyFunction;
import ru.nsu.fit.g14201.chirikhin.isolines.model.PixelCoordinateToAreaConverter;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

public class ColorMapSingleton {

    private static class SingletonHolder {
        private static final ColorMapSingleton INSTANCE = new ColorMapSingleton();
    }

    void draw(BufferedImage bufferedImage, List<Double> values, List<Integer[]> colors,
              MyFunction myFunction,
              PixelCoordinateToAreaConverter pixelCoordinateToAreaConverter) {
        for (int i = 0; i < bufferedImage.getHeight(); ++i) {
            for (int k = 0; k < bufferedImage.getWidth(); ++k) {
                double value = myFunction.apply(pixelCoordinateToAreaConverter.toRealX(k), pixelCoordinateToAreaConverter.toRealY(i));
                bufferedImage.setRGB(k, i, getColor(colors, values, value).getRGB());
            }
        }


    }

    private Color getColor(List<Integer[]> colors, List<Double> values, double value) {
        for (int i = 0; i < values.size() - 1; ++i) {
            double curValue = values.get(i);
            double nextValue = values.get(i + 1);

            if (value >= curValue && value <= nextValue) {
                Integer[] color = colors.get(i);
                return new Color(color[0], color[1], color[2]);
            }
        }

        throw new IndexOutOfBoundsException("Inner error! Can't choose color by point!");
    }

    public static ColorMapSingleton getInstance() {
        return SingletonHolder.INSTANCE;
    }
}
