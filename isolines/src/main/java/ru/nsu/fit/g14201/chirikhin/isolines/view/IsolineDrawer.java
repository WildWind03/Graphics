package ru.nsu.fit.g14201.chirikhin.isolines.view;

import javafx.util.Pair;
import ru.nsu.fit.g14201.chirikhin.isolines.function.MyFunction;
import ru.nsu.fit.g14201.chirikhin.isolines.model.PixelCoordinateToAreaConverter;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class IsolineDrawer implements Drawer {

    private static final int ENTER_POINT_WIDTH = 6;
    private static final int ENTER_POINT_HEIGHT = 6;

    public class Point {
        private final double value;
        private final int x;
        private final int y;

        public Point(double value, int x, int y) {
            this.value = value;
            this.x = x;
            this.y = y;
        }

        public double getValue() {
            return value;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Point point = (Point) o;

            if (Double.compare(point.value, value) != 0) return false;
            return x == point.x && y == point.y;

        }

        @Override
        public int hashCode() {
            int result;
            long temp;
            temp = Double.doubleToLongBits(value);
            result = (int) (temp ^ (temp >>> 32));
            result = 31 * result + x;
            result = 31 * result + y;
            return result;
        }
    }


    private static final double epsilon = 0.00001;

    private final int widthDiv;
    private final int heightDiv;
    private final int isolineColor;
    private final PixelCoordinateToAreaConverter pixelCoordinateToAreaConverter;
    private final MyFunction myFunction;
    private List<Double> values;
    private boolean isNeedToDrawEnterPoints;

    public IsolineDrawer(int widthDiv,
                         int heightDiv,
                         int isolineColor,
                         PixelCoordinateToAreaConverter pixelCoordinateToAreaConverter,
                         MyFunction myFunction,
                         List<Double> values,
                         boolean isNeedToDrawEnterPoints) {
        this.widthDiv = widthDiv;
        this.heightDiv = heightDiv;
        this.isolineColor = isolineColor;
        this.pixelCoordinateToAreaConverter = pixelCoordinateToAreaConverter;
        this.myFunction = myFunction;
        this.values = values;
        this.isNeedToDrawEnterPoints = isNeedToDrawEnterPoints;
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
                    double tempValue = value;

                    while (true) {
                        ArrayList<Pair<Integer, Integer>> points = new ArrayList<>();

                        addCrossPoint(new Point(virtualImage[k][i], k * stepX, i * stepY), new Point(virtualImage[k + 1][i], (k + 1) * stepX, i * stepY), tempValue, points);
                        addCrossPoint(new Point(virtualImage[k][i + 1], k * stepX, (i + 1) * stepY), new Point(virtualImage[k + 1][i + 1], (k + 1) * stepX, (i + 1) * stepY), tempValue, points);
                        addCrossPoint(new Point(virtualImage[k][i], k * stepX, i * stepY), new Point(virtualImage[k][i + 1], k * stepX, (i + 1) * stepY), tempValue, points);
                        addCrossPoint(new Point(virtualImage[k + 1][i], (k + 1) * stepX, (i) * stepY), new Point(virtualImage[k + 1][i + 1], (k + 1) * stepX, (i + 1) * stepY), tempValue, points);

                        if (0 == points.size()) {
                            break;
                        }
                        if (2 == points.size()) {
                            drawLine(graphics2D, points);
                            break;
                        }

                        if (3 == points.size()) {
                            tempValue += epsilon;
                            points.clear();
                            continue;
                        }

                        if (4 == points.size()) {
                            tempValue = value;
                            int centerX = stepX * k + stepX / 2;
                            int centerY = stepY * i + stepY / 2;

                            double centerValue = myFunction.apply(pixelCoordinateToAreaConverter.toRealX(centerX), pixelCoordinateToAreaConverter.toRealY(centerY));

                            ArrayList<Pair<Integer, Integer>> leftTriangleLineCrossPoints = new ArrayList<>();
                            while (true) {
                                addCrossPoint(new Point(virtualImage[k][i], k * stepX, i * stepY),
                                        new Point(virtualImage[k][i + 1], k * stepX, (i + 1) * stepY),
                                        tempValue,
                                        leftTriangleLineCrossPoints);
                                addCrossPoint(new Point(virtualImage[k][i], k * stepX, i * stepY),
                                        new Point(centerValue, centerX, centerY),
                                        tempValue,
                                        leftTriangleLineCrossPoints);
                                addCrossPoint(new Point(virtualImage[k][i + 1], k * stepX, (i + 1) * stepY),
                                        new Point(centerValue, centerX, centerY),
                                        tempValue,
                                        leftTriangleLineCrossPoints);

                                if (2 == leftTriangleLineCrossPoints.size()) {
                                    drawLine(graphics2D, leftTriangleLineCrossPoints);
                                    break;
                                }

                                if (0 == leftTriangleLineCrossPoints.size()) {
                                    break;
                                }

                                leftTriangleLineCrossPoints.clear();
                                tempValue += epsilon;
                            }

                            tempValue = value;
                            ArrayList<Pair<Integer, Integer>> upTriangleLineCrossPoints = new ArrayList<>();
                            while (true) {
                                addCrossPoint(new Point(virtualImage[k][i], k * stepX, i * stepY),
                                        new Point(virtualImage[k + 1][i], (k + 1)* stepX, (i) * stepY),
                                        tempValue,
                                        upTriangleLineCrossPoints);
                                addCrossPoint(new Point(virtualImage[k][i], k * stepX, i * stepY),
                                        new Point(centerValue, centerX, centerY),
                                        tempValue,
                                        upTriangleLineCrossPoints);
                                addCrossPoint(new Point(virtualImage[k + 1][i], (k + 1) * stepX, (i) * stepY),
                                        new Point(centerValue, centerX, centerY),
                                        tempValue,
                                        upTriangleLineCrossPoints);

                                if (2 == upTriangleLineCrossPoints.size()) {
                                    drawLine(graphics2D, upTriangleLineCrossPoints);
                                    break;
                                }

                                if (0 == upTriangleLineCrossPoints.size()) {
                                    break;
                                }

                                upTriangleLineCrossPoints.clear();
                                tempValue += epsilon;
                            }

                            tempValue = value;
                            ArrayList<Pair<Integer, Integer>> rightTriangleLineCrossPoints = new ArrayList<>();
                            while (true) {
                                addCrossPoint(new Point(virtualImage[k + 1][i], (k + 1) * stepX, i * stepY),
                                        new Point(virtualImage[k + 1][i + 1], (k + 1)* stepX, (i + 1) * stepY),
                                        tempValue,
                                        rightTriangleLineCrossPoints);
                                addCrossPoint(new Point(virtualImage[k + 1][i], (k + 1) * stepX, i * stepY),
                                        new Point(centerValue, centerX, centerY),
                                        tempValue,
                                        rightTriangleLineCrossPoints);
                                addCrossPoint(new Point(virtualImage[k + 1][i + 1], (k + 1) * stepX, (i + 1) * stepY),
                                        new Point(centerValue, centerX, centerY),
                                        tempValue,
                                        rightTriangleLineCrossPoints);

                                if (2 == rightTriangleLineCrossPoints.size()) {
                                    drawLine(graphics2D, rightTriangleLineCrossPoints);
                                    break;
                                }

                                if (0 == rightTriangleLineCrossPoints.size()) {
                                    break;
                                }

                                rightTriangleLineCrossPoints.clear();
                                tempValue += epsilon;
                            }

                            tempValue = value;
                            ArrayList<Pair<Integer, Integer>> downTriangleLineCrossPoints = new ArrayList<>();
                            while (true) {
                                addCrossPoint(new Point(virtualImage[k][i + 1], (k) * stepX, (i + 1) * stepY),
                                        new Point(virtualImage[k + 1][i + 1], (k + 1)* stepX, (i + 1) * stepY),
                                        tempValue,
                                        downTriangleLineCrossPoints);
                                addCrossPoint(new Point(virtualImage[k][i + 1], (k) * stepX, (i + 1) * stepY),
                                        new Point(centerValue, centerX, centerY),
                                        tempValue,
                                        downTriangleLineCrossPoints);
                                addCrossPoint(new Point(virtualImage[k + 1][i + 1], (k + 1) * stepX, (i + 1) * stepY),
                                        new Point(centerValue, centerX, centerY),
                                        tempValue,
                                        downTriangleLineCrossPoints);

                                if (2 == downTriangleLineCrossPoints.size()) {
                                    drawLine(graphics2D, downTriangleLineCrossPoints);
                                    break;
                                }

                                if (0 == downTriangleLineCrossPoints.size()) {
                                        break;
                                }

                                downTriangleLineCrossPoints.clear();
                                tempValue += epsilon;
                            }

                            break;
                        }

                    }
                }
            }
        }

        graphics2D.dispose();
    }

    private void addCrossPoint(Point p1, Point p2, double value, ArrayList<Pair<Integer, Integer>> points) {
        if (!isCrossed(p1.getValue(), p2.getValue(), value)) {
            return;
        }

        double value1 = p1.getValue();
        double value2 = p2.getValue();

        int dx = p2.getX() - p1.getX();
        int dy = p2.getY() - p1.getY();

        int x;
        int y;

        if (value1 == value2) {
            points.add(new Pair<>(p1.getX(), p1.getY()));
            return;
        }

        if (value1 > value2) {
            x = (int)(dx  - dx * ((value - value2) / (value1 - value2))) + p1.getX();
            y = (int)(dy  - dy * ((value - value2) / (value1 - value2))) + p1.getY();
        } else {
            x = (int)(dx * ((value - value1) / (value2 - value1))) + p1.getX();
            y = (int)(dy * ((value - value1) / (value2 - value1))) + p1.getY();
        }

        points.add(new Pair<>(x, y));

    }

    private void drawLine(Graphics2D graphics2D, ArrayList<Pair<Integer, Integer>> points) {
        Pair<Integer, Integer> point1 = points.get(0);
        Pair<Integer, Integer> point2 = points.get(1);

        if (point1.equals(point2)) {
            return;
        }

        graphics2D.drawLine(point1.getKey(), point1.getValue(), point2.getKey(), point2.getValue());

        if (isNeedToDrawEnterPoints) {
            graphics2D.drawOval(point1.getKey() - ENTER_POINT_WIDTH / 2,
                    point1.getValue() - ENTER_POINT_WIDTH / 2,
                    ENTER_POINT_WIDTH,
                    ENTER_POINT_HEIGHT);
            graphics2D.drawOval(point2.getKey() - ENTER_POINT_WIDTH / 2,
                    point2.getValue() - ENTER_POINT_HEIGHT / 2,
                    ENTER_POINT_WIDTH,
                    ENTER_POINT_HEIGHT);
        }
    }

    private boolean isCrossed(double value1, double value2, double value) {
        return !((value > value2 && value > value1) || (value < value1 && value < value2));
    }

    private double getValue(int x, int y) {
        double virtualX = pixelCoordinateToAreaConverter.toRealX(x);
        double virtualY = pixelCoordinateToAreaConverter.toRealY(y);
        return myFunction.apply(virtualX, virtualY);
    }
}
