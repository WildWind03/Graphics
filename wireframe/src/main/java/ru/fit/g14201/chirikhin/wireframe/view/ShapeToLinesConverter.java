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

    public static float getMaxValue(ArrayList<Line<Point3D<Float, Float, Float>>> lines) {
        final float[] globalMax = {Float.MIN_VALUE};
        lines.forEach(point3DLine -> {
            Point3D<Float, Float, Float> p1 = point3DLine.getStart();
            Point3D<Float, Float, Float> p2 = point3DLine.getEnd();

            float localMax = ListUtil.asList(p1.getX(), p1.getY(), p1.getZ(), p2.getX(), p2.getY(), p2.getZ())
                    .stream()
                    .max(Float::compareTo)
                    .orElse(0f);

            if (localMax > globalMax[0]) {
                globalMax[0] = localMax;
            }
        });

        return globalMax[0];
    }

    public static float getMinValue (ArrayList<Line<Point3D<Float, Float, Float>>> lines) {
        final float[] globalMin = {Float.MAX_VALUE};
        lines.forEach(point3DLine -> {
            Point3D<Float, Float, Float> p1 = point3DLine.getStart();
            Point3D<Float, Float, Float> p2 = point3DLine.getEnd();

            float localMin = ListUtil.asList(p1.getX(), p1.getY(), p1.getZ(), p2.getX(), p2.getY(), p2.getZ())
                    .stream()
                    .min(Float::compareTo)
                    .orElse(0f);

            if (localMin < globalMin[0]) {
                globalMin[0] = localMin;
            }
        });

        return globalMin[0];
    }

    public static ArrayList<Line<Point3D<Float, Float, Float>>> toLines(BSplineFunction bSplineFunction, int n, int m, int k,
                                                     float a, float b, float d, float c, float max) {
        if (max < 0) {
            max = 1;
        }

        ArrayList<Line<Point3D<Float, Float, Float>>> lines = new ArrayList<>();

        float stepSplinePace = ((b - a) / ((float) n * (float) k));
        float stepRotate = ((d - c) / ((float) m * (float) k));

        Point<Float, Float> currentPoint = bSplineFunction.getValue(a);
        Point<Float, Float> nextPoint = bSplineFunction.getValue(a + stepSplinePace);

        for (float i = a; i < b; i += stepSplinePace) {
            for (float j = c; j <= d; j += stepRotate) {
                float currentAngleInRadians = (float) Math.toRadians(j);
                float nextAngleInRadians = (float) Math.toRadians(j + stepRotate);
                Point3D<Float, Float, Float> startPoint = new Point3D<>(
                        (float) (currentPoint.getY() / max * Math.cos(currentAngleInRadians)),
                        (float) (currentPoint.getY() / max * Math.sin(currentAngleInRadians)),
                        currentPoint.getX() / max
                );

                if (j + stepRotate <= d + 0.1F) {
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

            currentPoint = nextPoint;
            nextPoint = bSplineFunction.getValue(i + 2 * stepSplinePace);
        }

        return lines;
    }
}
