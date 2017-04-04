package ru.nsu.fit.g14201.chirikhin.filter.model;

import ru.nsu.fit.g14201.chirikhin.filter.controller.MyPoint;
import ru.nsu.fit.g14201.chirikhin.function.DataInterpolator;
import ru.nsu.fit.g14201.chirikhin.function.InterpolatedFunction;
import ru.nsu.fit.g14201.chirikhin.function.Interpolator;
import ru.nsu.fit.g14201.chirikhin.function.LinearInterpolator;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

public class VisualizationFilter implements BaseFilter {
    private static final int IMAGE_SIZE = 350;
    private final LinkedList<MyPoint<Integer, Double>> absorptionPoints;
    private final LinkedList<int[]> emissionPoints;
    private final LinkedList<double[]> chargePoints;

    private final int maxX;
    private final int maxY;
    private final int maxZ;

    private final double dx;
    private final double dy;
    private final double dz;


    public VisualizationFilter(LinkedList<MyPoint<Integer, Double>> absorptionPoints,
                               LinkedList<int[]> emissionPoints,
                               LinkedList<double[]> chargePoints,
                               int maxX,
                               int maxY,
                               int maxZ) {
        this.absorptionPoints = absorptionPoints;
        this.emissionPoints = emissionPoints;
        this.chargePoints = chargePoints;

        this.maxX = maxX;
        this.maxY = maxY;
        this.maxZ = maxZ;

        dx = 1d / maxX;
        dy = 1d / maxY;
        dz = 1d / maxZ;
    }

    @Override
    public BufferedImage apply(BufferedImage bufferedImage) {
        MyPoint<Double, Double> myPoint = getMinAndMaxOfF();
        double maxF = myPoint.getValue2();
        double minF = myPoint.getValue1();

        LinkedList<MyPoint<Integer, Double>> redFunctionPoints = new LinkedList<>();
        LinkedList<MyPoint<Integer, Double>> greenFunctionPoints = new LinkedList<>();
        LinkedList<MyPoint<Integer, Double>> blueFunctionPoints = new LinkedList<>();

        InterpolatedFunction<Integer, Double> redFunction = null;
        InterpolatedFunction<Integer, Double> blueFunction = null;
        InterpolatedFunction<Integer, Double> greenFunction = null;

        if (null != emissionPoints) {
            emissionPoints.forEach(ints -> {
                redFunctionPoints.add(new MyPoint<>(ints[0], (double) ints[1]));
                greenFunctionPoints.add(new MyPoint<>(ints[0], (double) ints[2]));
                blueFunctionPoints.add(new MyPoint<>(ints[0], (double) ints[3]));
            });

            redFunction = new InterpolatedFunction<>(redFunctionPoints,
                    new LinearInterpolator(), (DataInterpolator<Integer, Double>) Interpolator::get);
            greenFunction = new InterpolatedFunction<>(greenFunctionPoints,
                    new LinearInterpolator(), (DataInterpolator<Integer, Double>) Interpolator::get);
            blueFunction = new InterpolatedFunction<>(blueFunctionPoints,
                    new LinearInterpolator(), (DataInterpolator<Integer, Double>) Interpolator::get);
        }

        InterpolatedFunction<Integer, Double> absorptionFunction = null;

        if (null != absorptionPoints) {
            absorptionFunction = new InterpolatedFunction<>(absorptionPoints,
                    new LinearInterpolator(), (DataInterpolator<Integer, Double>) Interpolator::get);
        }

        BufferedImage newImage = new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(), BufferedImage.TYPE_INT_RGB);

        for (int x = 0; x < bufferedImage.getWidth(); ++x) {
            for (int y = 0; y < bufferedImage.getHeight(); ++y) {

                Color color = new Color(bufferedImage.getRGB(x, y));
                double red = ((double) (color.getRed())) / 255d;
                double green = ((double) (color.getGreen())) / 255d;
                double blue = ((double) (color.getBlue())) / 255d;

                for (int z = 0; z < maxZ; ++z) {
                    double value = calculateF((double) x * (double) maxX / (double) IMAGE_SIZE, (double) y * (double) maxY / (double) IMAGE_SIZE, z);

                    int graphValue = (int) Math.round((value - minF) / (maxF - minF) * 100);
                    double ti = isAbsorptionEnabled() ? Math.exp(-(absorptionFunction.getValue(graphValue) * dz)) : 1;

                    red = red * ti + (isEmissionEnabled() ? redFunction.getValue(graphValue) / 255d * dz : 0);
                    green = green * ti + (isEmissionEnabled() ? greenFunction.getValue(graphValue) / 255d * dz : 0);
                    blue = blue * ti + (isEmissionEnabled() ? blueFunction.getValue(graphValue) / 255d * dz : 0);
                }

                newImage.setRGB(x,
                        y,
                        new Color(getRightValue(red * 255), getRightValue(green * 255), getRightValue(blue * 255)).getRGB());


            }
        }

        return newImage;
    }

    private int getRightValue(double value) {
        if (value > 255) {
            return 255;
        }

        if (value < 0) {
            return 0;
        }

        return (int) value;
    }

    private boolean isAbsorptionEnabled() {
        return null != absorptionPoints;
    }

    private boolean isEmissionEnabled() {
        return null != emissionPoints;
    }

    private MyPoint<Double, Double> getMinAndMaxOfF() {
        double fMin = Double.MAX_VALUE;
        double fMax = Double.MIN_VALUE;

        double value;
        for (int x = 0; x < maxX; x++) {
            for (int y = 0; y < maxY; y++) {
                for (int z = 0; z < maxZ; z++) {
                    value = calculateF(x, y, z);
                    fMin = Math.min(value, fMin);
                    fMax = Math.max(value, fMax);
                }
            }
        }

        return new MyPoint<>(fMin, fMax);
    }

    private double calculateF(double x, double y, double z) {
        double centerX = (x + 0.5) * dx;
        double centerY = (y + 0.5) * dy;
        double centerZ = (z + 0.5) * dz;

        double value = 0;

        for (double[] charge : chargePoints) {
            double distance = Math.sqrt(
                    Math.pow(Math.abs(centerX - charge[0]), 2)
                            + Math.pow(Math.abs(centerY - charge[1]), 2)
                            + Math.pow(Math.abs(centerZ - charge[2]), 2));
            distance = Math.max(0.1, distance);
            value += charge[3] / distance;
        }
        return value;
    }
}