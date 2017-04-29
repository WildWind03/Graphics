package ru.fit.g14201.chirikhin.wireframe.view;

import chirikhin.support.Line;
import chirikhin.support.Point;
import chirikhin.support.Point3D;
import ru.fit.g14201.chirikhin.wireframe.bspline.BSplineFunction;

import java.util.ArrayList;

public class ShapeToLinesConverter {
    private ShapeToLinesConverter() {

    }

    private static float getMaxOfSpline(BSplineFunction bSplineFunction, int n, int k, float a, float b) {
        float max = 0;

        float stepSplinePace = ((b - a) / (n * k));
        for (float i = a; i < b; i += stepSplinePace) {
            Point<Float, Float> currentPoint = bSplineFunction.getValue(i);

            if (Math.abs(currentPoint.getX()) > max) {
                max = Math.abs(currentPoint.getX());
            }

            if (Math.abs(currentPoint.getY()) > max) {
                max = Math.abs(currentPoint.getY());
            }
        }

        return max;
    }

    public static ArrayList<Line<Point3D<Float, Float, Float>>> toLines(BSplineFunction bSplineFunction, int n, int m, int k,
                                                     float a, float b, float d, float c) {
        float max = getMaxOfSpline(bSplineFunction, n, k, a, b);

        ArrayList<Line<Point3D<Float, Float, Float>>> lines = new ArrayList<>();

        float stepSplinePace = ((b - a) / (n * k));
        float stepRotate = ((d - c) / (m * k));
        for (float i = a; i < b; i += stepSplinePace) {
            Point<Float, Float> currentPoint = bSplineFunction.getValue(i);
            Point<Float, Float> nextPoint = bSplineFunction.getValue(i + stepSplinePace);
            for (float j = c; j <= d; j += stepRotate) {
                float currentAngleInRadians = (float) Math.toRadians(j);
                float nextAngleInRadians = (float) Math.toRadians(j + stepRotate);
                Point3D<Float, Float, Float> startPoint = new Point3D<>(
                        (float) (currentPoint.getY() / max * Math.cos(currentAngleInRadians)),
                        (float) (currentPoint.getY() / max * Math.sin(currentAngleInRadians)),
                        currentPoint.getX() / max
                );

                if (j + stepRotate <= d) {
                    Point3D<Float, Float, Float> rotateEndPoint = new Point3D<>(
                            (float) (currentPoint.getY() / max * Math.cos(nextAngleInRadians)),
                            (float) (currentPoint.getY() / max * Math.sin(nextAngleInRadians)),
                            currentPoint.getX() / max
                    );

                    lines.add(new Line<>(startPoint, rotateEndPoint));
                }

                if (null != nextPoint && i + stepSplinePace < b) {
                    Point3D<Float, Float, Float> lengthEndPoint = new Point3D<>(
                            (float) (nextPoint.getY() / max * Math.cos(currentAngleInRadians)),
                            (float) (nextPoint.getY() / max * Math.sin(currentAngleInRadians)),
                            nextPoint.getX() / max
                    );
                    lines.add(new Line<>(startPoint, lengthEndPoint));
                }
            }
        }

        return lines;
    }
}
