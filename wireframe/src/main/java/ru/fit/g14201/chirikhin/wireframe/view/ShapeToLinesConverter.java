package ru.fit.g14201.chirikhin.wireframe.view;

import chirikhin.support.Line;
import chirikhin.support.Point;
import chirikhin.support.Point3D;
import chirikhin.swing.util.ListUtil;
import com.sun.org.apache.bcel.internal.generic.FLOAD;
import ru.fit.g14201.chirikhin.wireframe.bspline.BSplineFunction;

import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.function.Function;

public class ShapeToLinesConverter {
    private ShapeToLinesConverter() {

    }

    public static ArrayList<Line<Point3D<Float, Float, Float>>> toLines(BSplineFunction bSplineFunction, int n, int m, int k,
                                                     float a, float b, float d, float c) {

        ArrayList<Line<Point3D<Float, Float, Float>>> lines = new ArrayList<>();

        float stepSplinePace = ((b - a) / ((float) n * (float) k));
        float stepRotate = ((d - c) / ((float) m * (float) k));

        Point<Float, Float> currentPoint = bSplineFunction.getValue(a);
        Point<Float, Float> nextPoint = bSplineFunction.getValue(a + stepSplinePace);

        if (null == currentPoint || nextPoint == null) {
            return lines;
        }

        for (float i = a; i < b; i += stepSplinePace) {
            for (float j = c; j <= d; j += stepRotate) {
                float currentAngleInRadians = (float) Math.toRadians(j);
                float nextAngleInRadians = (float) Math.toRadians(j + stepRotate);
                Point3D<Float, Float, Float> startPoint = new Point3D<>(
                        (float) (currentPoint.getY() * Math.cos(currentAngleInRadians)),
                        (float) (currentPoint.getY() * Math.sin(currentAngleInRadians)),
                        currentPoint.getX()
                );

                if (j + stepRotate <= d + 0.1F) {
                    Point3D<Float, Float, Float> rotateEndPoint = new Point3D<>(
                            (float) (currentPoint.getY() * Math.cos(nextAngleInRadians)),
                            (float) (currentPoint.getY() * Math.sin(nextAngleInRadians)),
                            currentPoint.getX()
                    );

                    lines.add(new Line<>(startPoint, rotateEndPoint));
                }

                if (null != nextPoint && i + stepSplinePace < b) {
                    Point3D<Float, Float, Float> lengthEndPoint = new Point3D<>(
                            (float) (nextPoint.getY() * Math.cos(currentAngleInRadians)),
                            (float) (nextPoint.getY() * Math.sin(currentAngleInRadians)),
                            nextPoint.getX()
                    );
                    lines.add(new Line<>(startPoint, lengthEndPoint));
                }
            }

            currentPoint = nextPoint;
            nextPoint = bSplineFunction.getValue(i + 2 * stepSplinePace);
        }

        return lines;
    }
}
