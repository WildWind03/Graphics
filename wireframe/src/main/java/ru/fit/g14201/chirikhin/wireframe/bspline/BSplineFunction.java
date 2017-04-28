package ru.fit.g14201.chirikhin.wireframe.bspline;

import chirikhin.matrix.Matrix;
import chirikhin.matrix.MatrixUtil;
import chirikhin.support.Point;

import java.util.ArrayList;

public class BSplineFunction {
    private final ArrayList<Point<Float, Float>> points;
    private final Matrix matrix = new Matrix(new float[][] { {-1, 3, -3, 1},
            {3, -6, 3, 0},
            {-3, 0, 3, 0},
            {1, 4, 1, 0}});
    private static final float k = 1f/6f;

    public BSplineFunction(ArrayList<Point<Float, Float>> points) {
        this.points = points;
    }

    public Point<Float, Float> getValue(int i, float t) {
        Matrix giMatrixX = new Matrix(new float[][] {{points.get(i - 1).getX()},
                {points.get(i).getX()}, {points.get(i + 1).getX()}, {points.get(i + 2).getX()}});
        Matrix giMatrixY = new Matrix(new float[][]{{points.get(i - 1).getY()}, {points.get(i).getY()},
                {points.get(i + 1).getY()}, {points.get(i + 2).getY()}});

        Matrix tMatrix = new Matrix(new float[][] {{t * t * t, t * t, t, 1}});

        Matrix tempMatrix = MatrixUtil.multiply(tMatrix, MatrixUtil.multiply(matrix, k));

        Matrix xResultMatrix = MatrixUtil.multiply(tempMatrix, giMatrixX);
        Matrix yResultMatrix = MatrixUtil.multiply(tempMatrix, giMatrixY);

        return new Point<>(xResultMatrix.get(0, 0), yResultMatrix.get(0, 0));
    }

    public Point<Float, Float> getValue(float percentOfSplineLength) {
        Point<Integer, Float> iAndTPoint = getIAndT(percentOfSplineLength);
        return getValue(iAndTPoint.getX(), iAndTPoint.getY());
    }

    public float getLength() {
        float totalLength = 0;
        for (int i = getMinI(); i < getMaxI(); ++i) {
            float localLength = 0;

            Point<Float, Float> prevPoint = null;
            for (float t = 0; t < 1; t = t + 0.01f) {
                Point<Float, Float> p = getValue(i, t);
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

    public Point<Integer, Float> getIAndT(float u) {
        float appropriateLength = getLength() * u;

        float totalLength = 0;
        for (int i = getMinI(); i < getMaxI(); ++i) {

            Point<Float, Float> prevPoint = null;
            for (float t = 0; t < 1; t = t + 0.01f) {
                Point<Float, Float> p = getValue(i, t);
                if (t > 0) {
                    totalLength += Math.sqrt(Math.pow(p.getX() - prevPoint.getX(), 2)
                            + Math.pow(p.getY() - prevPoint.getY(), 2));

                    if (totalLength > appropriateLength) {
                        return new Point<>(i, t);
                    }
                }
                prevPoint = p;
            }
        }

        return new Point<>(getMaxI(), 0f);
    }

    public int getMinI() {
        return 1;
    }

    public int getMaxI() {
        return points.size() - 2;
    }

}
