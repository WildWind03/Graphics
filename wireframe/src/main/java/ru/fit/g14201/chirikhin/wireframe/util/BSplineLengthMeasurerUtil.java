package ru.fit.g14201.chirikhin.wireframe.util;

import ru.fit.g14201.chirikhin.wireframe.bspline.BSplineFunction;
import chirikhin.support.Point;

public class BSplineLengthMeasurerUtil {
    private BSplineLengthMeasurerUtil() {

    }

    public static float getLength(BSplineFunction bSplineFunction) {
        float totalLength = 0;
        for (int i = bSplineFunction.getMinI(); i < bSplineFunction.getMaxI(); ++i) {
            float localLength = 0;

            Point<Float, Float> prevPoint = null;
            for (float t = 0; t < 1; t = t + 0.01f) {
                Point<Float, Float> p = bSplineFunction.getValue(i, t);
                if (t > 0) {
                    localLength += Math.sqrt(Math.pow(p.getX() - prevPoint.getX(), 2)
                            + Math.pow(p.getY() - prevPoint.getY(), 2));
                }
                prevPoint = p;
            }
            totalLength += localLength;
        }

        return totalLength;
    }

    public static Point<Float, Integer> getIAndTOfSpline(BSplineFunction bSplineFunction, float u) {
        float appropriateLength = getLength(bSplineFunction) * u;

        float totalLength = 0;
        for (int i = bSplineFunction.getMinI(); i < bSplineFunction.getMaxI(); ++i) {

            Point<Float, Float> prevPoint = null;
            for (float t = 0; t < 1; t = t + 0.01f) {
                Point<Float, Float> p = bSplineFunction.getValue(i, t);
                if (t > 0) {
                    totalLength += Math.sqrt(Math.pow(p.getX() - prevPoint.getX(), 2)
                            + Math.pow(p.getY() - prevPoint.getY(), 2));

                    if (totalLength > appropriateLength) {
                        return new Point<>(t, i);
                    }
                }
                prevPoint = p;
            }
        }

        return new Point<Float, Integer>(0f, bSplineFunction.getMaxI());
    }
}
