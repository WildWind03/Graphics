package ru.nsu.fit.g14201.chirikhin.isolines.view;

import com.chirikhin.interpolated_function.InterpolatedFunction;
import com.chirikhin.interpolated_function.Interpolator;
import com.chirikhin.interpolated_function.MyPoint;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;

public class InterpolatedColorPaletteFunction implements ColorFunction {

    private final InterpolatedFunction redFunc;
    private final InterpolatedFunction greenFunc;
    private final InterpolatedFunction blueFunc;

    public InterpolatedColorPaletteFunction(List<Integer[]> colors, Interpolator interpolator, int width) {
        int step = width / colors.size();
        LinkedList<MyPoint<Double, Double>> redPoints = new LinkedList<>();
        LinkedList <MyPoint<Double, Double>> greenPoints = new LinkedList<>();
        LinkedList <MyPoint<Double, Double>> bluePoints = new LinkedList<>();

        for (int k = 0; k < colors.size(); ++k) {
            redPoints.add(new MyPoint<>((double) step * k, (double) colors.get(k)[0]));
            greenPoints.add(new MyPoint<>((double) step * k, (double) colors.get(k)[1]));
            bluePoints.add(new MyPoint<>((double) step * k, (double) colors.get(k)[2]));
        }

        redFunc = new InterpolatedFunction(redPoints, interpolator);
        greenFunc = new InterpolatedFunction(greenPoints, interpolator);
        blueFunc = new InterpolatedFunction(bluePoints, interpolator);
    }


    @Override
    public Integer apply(Integer integer, Integer integer2) {
        return new Color(redFunc.getValue(integer.doubleValue()).intValue(),
                greenFunc.getValue(integer.doubleValue()).intValue(),
                blueFunc.getValue(integer.doubleValue()).intValue()).getRGB();
    }
}
