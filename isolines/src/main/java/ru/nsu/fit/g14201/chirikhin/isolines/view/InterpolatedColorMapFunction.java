package ru.nsu.fit.g14201.chirikhin.isolines.view;

import com.chirikhin.interpolated_function.InterpolatedFunction;
import com.chirikhin.interpolated_function.Interpolator;
import com.chirikhin.interpolated_function.MyPoint;
import ru.nsu.fit.g14201.chirikhin.isolines.function.MyFunction;
import ru.nsu.fit.g14201.chirikhin.isolines.model.PixelCoordinateToAreaConverter;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;

public class InterpolatedColorMapFunction implements ColorFunction {

    private final InterpolatedFunction redFunc;
    private final InterpolatedFunction greenFunc;
    private final InterpolatedFunction blueFunc;
    private final MyFunction myFunction;
    private final PixelCoordinateToAreaConverter pixelCoordinateToAreaConverter;

    public InterpolatedColorMapFunction(List<Double> values, List<Integer[]> colors, MyFunction myFunction, PixelCoordinateToAreaConverter pixelCoordinateToAreaConverter, Interpolator interpolator) {
        this.myFunction = myFunction;
        this.pixelCoordinateToAreaConverter = pixelCoordinateToAreaConverter;

        LinkedList<MyPoint<Double, Double>> redPoints = new LinkedList<>();
        LinkedList <MyPoint<Double, Double>> greenPoints = new LinkedList<>();
        LinkedList <MyPoint<Double, Double>> bluePoints = new LinkedList<>();

        for (int k = 0; k < colors.size(); ++k) {
            redPoints.add(new MyPoint<>(values.get(k), (double) colors.get(k )[0]));
            greenPoints.add(new MyPoint<>(values.get(k), (double) colors.get(k )[1]));
            bluePoints.add(new MyPoint<>(values.get(k), (double) colors.get(k)[2]));
        }

        redFunc = new InterpolatedFunction(redPoints, interpolator);
        greenFunc = new InterpolatedFunction(greenPoints, interpolator);
        blueFunc = new InterpolatedFunction(bluePoints, interpolator);
    }

    @Override
    public Integer apply(Integer integer, Integer integer2) {
        double value = myFunction.apply(pixelCoordinateToAreaConverter.toRealX(integer), pixelCoordinateToAreaConverter.toRealY(integer2));
        Color currentColor = new Color(redFunc.getValue(value).intValue(), greenFunc.getValue(value).intValue(), blueFunc.getValue(value).intValue());
        return currentColor.getRGB();
    }
}
