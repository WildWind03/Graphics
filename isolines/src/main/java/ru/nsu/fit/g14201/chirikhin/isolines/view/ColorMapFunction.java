package ru.nsu.fit.g14201.chirikhin.isolines.view;

import ru.nsu.fit.g14201.chirikhin.isolines.function.MyFunction;
import ru.nsu.fit.g14201.chirikhin.isolines.model.PixelCoordinateToAreaConverter;

import java.awt.*;
import java.util.List;

public class ColorMapFunction implements ColorFunction {

    private final List<Double> values;
    private final List<Integer[]> colors;
    private final MyFunction myFunction;
    private final PixelCoordinateToAreaConverter pixelCoordinateToAreaConverter;

    public ColorMapFunction(List<Double> values, List<Integer[]> colors, MyFunction myFunction, PixelCoordinateToAreaConverter pixelCoordinateToAreaConverter) {
        this.values = values;
        this.colors = colors;
        this.myFunction = myFunction;
        this.pixelCoordinateToAreaConverter = pixelCoordinateToAreaConverter;
    }

    @Override
    public Integer apply(Integer i1, Integer i2) {
        double value = myFunction.apply(pixelCoordinateToAreaConverter.toRealX(i1), pixelCoordinateToAreaConverter.toRealY(i2));
        for (int i = 0; i < values.size() - 1; ++i) {
            double curValue = values.get(i);
            double nextValue = values.get(i + 1);

            if (value >= curValue && value <= nextValue) {
                Integer[] color = colors.get(i);
                return new Color(color[0], color[1], color[2]).getRGB();
            }
        }

        throw new IndexOutOfBoundsException("Inner error! Can't choose color by point!");
    }
}
