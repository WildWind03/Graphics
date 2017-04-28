package ru.fit.g14201.chirikhin.wireframe.view;

import chirikhin.matrix.Matrix;
import chirikhin.support.Line;
import chirikhin.support.Point;
import chirikhin.support.Point3D;
import ru.fit.g14201.chirikhin.wireframe.bspline.BSplineFunction;

import java.util.ArrayList;
import java.util.List;

public class ShapeToLinesConverter {
    private ShapeToLinesConverter() {

    }

    public static ArrayList<Line<Point3D<Float, Float, Float>>> toLines(BSplineFunction bSplineFunction, int n, int m, int k,
                                                     float a, float b, float d, float c) {
        ArrayList<Line<Point3D<Float, Float, Float>>> lines = new ArrayList<>();

        float stepSplinePace = ((b - a) / (n * k));
        float stepRotate = ((d - c) / (m * k));
        for (float i = a; i < b; i += stepSplinePace) {
            Point<Float, Float> currentPoint = bSplineFunction.getValue(i);
            Point<Float, Float> nextPoint = bSplineFunction.getValue(i + stepSplinePace);
            for (float j = c; j < d; j += stepRotate) {
                float currentAngleInRadians = (float) Math.toRadians(j);
                float nextAngleInRadians = (float) Math.toRadians(j + stepRotate);
                Point3D<Float, Float, Float> startPoint = new Point3D<>(
                        (float) (currentPoint.getY() * Math.cos(currentAngleInRadians)),
                        (float) (currentPoint.getY() * Math.sin(currentAngleInRadians)),
                        currentPoint.getX()
                );

                Point3D<Float, Float, Float> rotateEndPoint = new Point3D<>(
                        (float) (currentPoint.getY() * Math.cos(nextAngleInRadians)),
                        (float) (currentPoint.getY() * Math.sin(nextAngleInRadians)),
                        currentPoint.getX()
                );

                lines.add(new Line<>(startPoint, rotateEndPoint));

                if (null != nextPoint) {
                    Point3D<Float, Float, Float> lengthEndPoint = new Point3D<>(
                            (float) (nextPoint.getY() * Math.cos(currentAngleInRadians)),
                            (float) (nextPoint.getY() * Math.sin(currentAngleInRadians)),
                            nextPoint.getX()
                    );
                    lines.add(new Line<>(startPoint, lengthEndPoint));
                }
            }
        }

        return lines;
    }
}
