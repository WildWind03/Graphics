package ru.nsu.fit.g14201.chirikhin.isolines.view;

import com.chirikhin.interpolated_function.InterpolatedFunction;
import com.chirikhin.interpolated_function.Interpolator;
import com.chirikhin.interpolated_function.MyPoint;

import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.List;

public class InterpolatedColorPaletteDrawer implements Drawer {
    private final List<Integer[]> colors;
    private final Interpolator interpolator;

    public InterpolatedColorPaletteDrawer(List<Integer[]> colors, Interpolator interpolator) {
        this.colors = colors;
        this.interpolator = interpolator;
    }

    @Override
    public void draw(BufferedImage bufferedImage) {

        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();

        int step = width / colors.size();

        LinkedList<MyPoint<Double, Double>> redPoints = new LinkedList<>();
        LinkedList <MyPoint<Double, Double>> greenPoints = new LinkedList<>();
        LinkedList <MyPoint<Double, Double>> bluePoints = new LinkedList<>();

        for (int k = 0; k < colors.size(); ++k) {
            redPoints.add(new MyPoint<>((double) step * k, (double) colors.get(k)[0]));
            greenPoints.add(new MyPoint<>((double) step * k, (double) colors.get(k)[1]));
            bluePoints.add(new MyPoint<>((double) step * k, (double) colors.get(k)[2]));
        }

        InterpolatedFunction redFunc = new InterpolatedFunction(redPoints, interpolator);
        InterpolatedFunction greenFunc = new InterpolatedFunction(greenPoints, interpolator);
        InterpolatedFunction blueFunc = new InterpolatedFunction(bluePoints, interpolator);

        for (int k = 0; k < height; ++k) {
            for (int i = 0; i < width; i++) {
                bufferedImage.getRaster().setPixel(i, k,
                        new int[] {
                                redFunc.getValue((double) i).intValue(),
                                greenFunc.getValue((double) i).intValue(),
                                blueFunc.getValue((double) i).intValue()});
            }
        }


    }
}
