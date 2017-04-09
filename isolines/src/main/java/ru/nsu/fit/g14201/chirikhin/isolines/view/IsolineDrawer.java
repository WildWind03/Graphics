package ru.nsu.fit.g14201.chirikhin.isolines.view;

import javafx.util.Pair;
import ru.nsu.fit.g14201.chirikhin.isolines.function.MyFunction;
import ru.nsu.fit.g14201.chirikhin.isolines.model.PixelCoordinateToAreaConverter;

import java.awt.*;
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
                    addCrossPoint(virtualImage, k, i, k + 1, i, value, stepX, stepY, points);
                    addCrossPoint(virtualImage, k, i + 1, k + 1, i + 1, value, stepX, stepY, points);
                    addCrossPoint(virtualImage, k, i, k, i + 1, value, stepX, stepY, points);
                    addCrossPoint(virtualImage, k + 1, i, k + 1, i + 1, value, stepX, stepY, points);

                    if (2 == points.size()) {
                        Pair<Integer, Integer> point1 = points.get(0);
                        Pair<Integer, Integer> point2 = points.get(1);

                        graphics2D.drawLine(point1.getKey(), point1.getValue(), point2.getKey(), point2.getValue());
                    }


                    if (4 == points.size()) {
                        ArrayList<Pair<Integer, Integer>> triangleLineCrossPoints = new ArrayList<>();
                       // addCrossPoint();
                    }
                }
            }
        }

        graphics2D.dispose();
    }

    private void addCrossPoint(double[][] virtualImage, int i0, int k0, int i1, int k1, double value,
            int stepX, int stepY, List<Pair<Integer, Integer>> points) {
        if (!isCrossed(virtualImage, i0, k0, i1, k1, value)) {
            return;
        }

        int dx = i1 - i0;
        int dy = k1 - k0;

        int x = i0 * stepX;
        int y = k0 * stepY;

        if (virtualImage[i0][k0] > virtualImage[i1][k1]) {
            y += dy * stepY - stepY * dy * ((value - virtualImage[i1][k1]) / (virtualImage[i0][k0] - virtualImage[i1][k1]));
            x += dx * stepX - stepX * dx * ((value - virtualImage[i1][k1]) / (virtualImage[i0][k0] - virtualImage[i1][k1]));
        } else {
            y += stepY * dy * ((value - virtualImage[i0][k0]) / (virtualImage[i1][k1] - virtualImage[i0][k0]));
            x += stepX * dx * ((value - virtualImage[i0][k0]) / (virtualImage[i1][k1] - virtualImage[i0][k0]));
        }

        points.add(new Pair<>(x, y));
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
