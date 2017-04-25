package ru.nsu.fit.g14201.chirikhin.isolines.util;

import ru.nsu.fit.g14201.chirikhin.isolines.function.MyFunction;
import ru.nsu.fit.g14201.chirikhin.isolines.model.PixelCoordinateToAreaConverter;

import java.util.ArrayList;
import java.util.List;

public class Util {
    private Util() {

    }

    public static List<Double> getPointsByFunctionColorsAndArea(MyFunction function, PixelCoordinateToAreaConverter pixelCoordinateToAreaConverter,
                                                                 int countOfDivisions) {
        double max = function.apply(0d, 0d);
        double min = function.apply(0d, 0d);

        for (int i = 0; i < pixelCoordinateToAreaConverter.getPixelFieldWidth(); ++i) {
            for (int k = 0; k < pixelCoordinateToAreaConverter.getPixelFieldHeight(); ++k) {
                double value = function.apply(pixelCoordinateToAreaConverter.toRealX(i), pixelCoordinateToAreaConverter.toRealY(k));

                if (value > max) {
                    max = value;
                }

                if (value < min) {
                    min = value;
                }
            }
        }

        List<Double> values = new ArrayList<>();
        double step = (max - min) / countOfDivisions;

        for (int i = 0; i < countOfDivisions; ++i) {
            values.add(min + i * step);
        }

        values.add(max);

        return values;
    }
}
