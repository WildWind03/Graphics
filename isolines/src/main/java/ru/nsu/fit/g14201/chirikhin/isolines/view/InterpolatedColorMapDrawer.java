package ru.nsu.fit.g14201.chirikhin.isolines.view;

import com.chirikhin.interpolated_function.InterpolatedFunction;
import com.chirikhin.interpolated_function.Interpolator;
import com.chirikhin.interpolated_function.MyPoint;
import ru.nsu.fit.g14201.chirikhin.isolines.function.MyFunction;
import ru.nsu.fit.g14201.chirikhin.isolines.model.PixelCoordinateToAreaConverter;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.List;

public class InterpolatedColorMapDrawer implements Drawer {
    private final List<Double> values;
    private final List<Integer[]> colors;
    private final MyFunction myFunction;
    private final PixelCoordinateToAreaConverter pixelCoordinateToAreaConverter;
    private final Interpolator interpolator;

    public InterpolatedColorMapDrawer(List<Double> values, List<Integer[]> colors, MyFunction myFunction, PixelCoordinateToAreaConverter pixelCoordinateToAreaConverter, Interpolator interpolator) {
        this.values = values;
        this.colors = colors;
        this.myFunction = myFunction;
        this.pixelCoordinateToAreaConverter = pixelCoordinateToAreaConverter;
        this.interpolator = interpolator;
    }

    @Override
    public void draw(BufferedImage bufferedImage) {
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();

        LinkedList<MyPoint<Double, Double>> redPoints = new LinkedList<>();
        LinkedList <MyPoint<Double, Double>> greenPoints = new LinkedList<>();
        LinkedList <MyPoint<Double, Double>> bluePoints = new LinkedList<>();

        for (int k = 0; k < colors.size(); ++k) {
            redPoints.add(new MyPoint<>(values.get(k), (double) colors.get(k )[0]));
            greenPoints.add(new MyPoint<>(values.get(k), (double) colors.get(k )[1]));
            bluePoints.add(new MyPoint<>(values.get(k), (double) colors.get(k)[2]));
        }

        InterpolatedFunction redFunc = new InterpolatedFunction(redPoints, interpolator);
        InterpolatedFunction greenFunc = new InterpolatedFunction(greenPoints, interpolator);
        InterpolatedFunction blueFunc = new InterpolatedFunction(bluePoints, interpolator);


        for (int i = 0; i < height; ++i) {
            for (int k = 0; k < width; ++k) {
                double value = myFunction.apply(pixelCoordinateToAreaConverter.toRealX(k), pixelCoordinateToAreaConverter.toRealY(i));
                bufferedImage.getRaster().setPixel(k, i,
                        new int[] {
                                redFunc.getValue(value).intValue(),
                                greenFunc.getValue(value).intValue(),
                                blueFunc.getValue(value).intValue()});
            }
        }
    }
}
