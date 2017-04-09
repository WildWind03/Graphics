package ru.nsu.fit.g14201.chirikhin.isolines.view;

import javafx.util.Pair;
import ru.nsu.fit.g14201.chirikhin.isolines.function.MyFunction;
import ru.nsu.fit.g14201.chirikhin.isolines.model.PixelCoordinateToAreaConverter;

import java.awt.*;
import java.awt.geom.Arc2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class IsolineDrawer implements Drawer {

    private final int widthDiv;
    private final int heightDiv;
    private final int isolineColor;
    private final PixelCoordinateToAreaConverter pixelCoordinateToAreaConverter;
    private final MyFunction myFunction;
    private List<Double> values;

    public IsolineDrawer(int widthDiv,
                         int heightDiv,
                         int isolineColor,
                         PixelCoordinateToAreaConverter pixelCoordinateToAreaConverter,
                         MyFunction myFunction,
                         List<Double> values) {
        this.widthDiv = widthDiv;
        this.heightDiv = heightDiv;
        this.isolineColor = isolineColor;
        this.pixelCoordinateToAreaConverter = pixelCoordinateToAreaConverter;
        this.myFunction = myFunction;
        this.values = values;
    }

    @Override
    public void draw(BufferedImage bufferedImage) {
        Graphics2D graphics2D = bufferedImage.createGraphics();
        graphics2D.setColor(new Color(isolineColor));

        int stepX = bufferedImage.getWidth() / (widthDiv);
        int stepY = bufferedImage.getHeight() / (heightDiv);

        double[][] virtualImage = new double[widthDiv + 1][heightDiv + 1];
        for (int i = 0; i < heightDiv + 1; i++) {
            for (int k = 0; k < widthDiv + 1; k++) {
                virtualImage[k][i] = getValue(k * stepX, i * stepY);
            }
        }

        for (Double value : values) {
            for (int  i = 0; i < heightDiv; ++i) {
                for (int k = 0; k < widthDiv; ++k) {
                    ArrayList<Pair<Integer, Integer>> points = new ArrayList<>();
//                    addCrossPoint(virtualImage[k][i], virtualImage[k+1][i], value, 1, 0, points, k * stepX, i * stepY, stepX, stepY);
//                    addCrossPoint(virtualImage[k][i + 1], virtualImage[k + 1][i + 1], value, 1, 0, points, k * stepX, (i + 1) * stepY, stepX, stepY);
//                    addCrossPoint(virtualImage[k][i], virtualImage[k][i + 1], value, 0, 1, points, k * stepX, i * stepY, stepX, stepY);
//                    addCrossPoint(virtualImage[k + 1][i], virtualImage [k + 1][i + 1], value, 0, 1, points, (k + 1) * stepX, i * stepY, stepX, stepY);

                    addCrossPointOfSquare(getCrossPoint(virtualImage[k][i], virtualImage[k+1][i], value, stepX, 0), points, k * stepX, i * stepY);
                    addCrossPointOfSquare(getCrossPoint(virtualImage[k][i + 1], virtualImage[k + 1][i + 1], value, stepX, 0), points, k * stepX, (i + 1) * stepY);
                    addCrossPointOfSquare(getCrossPoint(virtualImage[k][i], virtualImage[k][i + 1], value, 0, stepY), points, k * stepX, i * stepY);
                    addCrossPointOfSquare(getCrossPoint(virtualImage[k + 1][i], virtualImage [k + 1][i + 1], value, 0, stepY), points, (k + 1) * stepX, i * stepY);



                    if (2 == points.size()) {
                        Pair<Integer, Integer> point1 = points.get(0);
                        Pair<Integer, Integer> point2 = points.get(1);

                        graphics2D.drawLine(point1.getKey(), point1.getValue(), point2.getKey(), point2.getValue());
                    }


                    if (4 == points.size()) {
//                        ArrayList<Pair<Integer, Integer>> firstTriangleLineCrossPoints = new ArrayList<>();
//                        addCrossPoint(virtualImage[k][i], virtualImage[k][i + 1], value, 0, 1, firstTriangleLineCrossPoints, k * stepX, i * stepY, stepX, stepY);
//
//                        int centerX = stepX * k + stepX / 2;
//                        int centerY = stepY * i + stepY / 2;
//
//                        double centerValue = myFunction.apply(pixelCoordinateToAreaConverter.toRealX(centerX), pixelCoordinateToAreaConverter.toRealY(centerY));
//
//                        addCrossPoint(virtualImage[k][i], centerValue, value, 1, 1, points, k * stepX, i * stepY, stepX, stepY);
//
//                        addCrossPoint();
                    }
                }
            }
        }

        graphics2D.dispose();
    }

    private void addCrossPointOfSquare(Pair<Integer, Integer> point, List<Pair<Integer, Integer>> points, int startX, int startY) {
        if (null == point) {
            return;
        }

        int x = point.getKey() + startX;
        int y = point.getValue() + startY;

        points.add(new Pair<>(x, y));
    }

    private Pair<Integer, Integer> getCrossPoint(double value1, double value2, double value, double dx, double dy) {
        if (!isCrossed(value1, value2, value)) {
            return null;
        }

        int x;
        int y;

        if (value1 > value2) {
            x = (int)(dx  - dx * ((value - value2) / (value1 - value2)));
            y = (int)(dy  - dy * ((value - value2) / (value1 - value2)));
        } else {
            x = (int)(dx * ((value - value1) / (value2 - value1)));
            y = (int)(dy * ((value - value1) / (value2 - value1)));
        }

        return new Pair<>(x, y);
    }

    private boolean isCrossed(double value1, double value2, double value) {
        return !((value > value2 && value > value1) || (value < value1 && value < value2));
    }

    private boolean isCrossed(double[][] virtualImage, int i0, int k0, int i1, int k1, double value) {
        return !((value < virtualImage[i0][k0] && value < virtualImage[i1][k1])
                || (value > virtualImage[i0][k0] && value > virtualImage[i1][k1]));
    }

    private double getValue(int x, int y) {
        double virtualX = pixelCoordinateToAreaConverter.toRealX(x);
        double virtualY = pixelCoordinateToAreaConverter.toRealY(y);
        return myFunction.apply(virtualX, virtualY);
    }
}
