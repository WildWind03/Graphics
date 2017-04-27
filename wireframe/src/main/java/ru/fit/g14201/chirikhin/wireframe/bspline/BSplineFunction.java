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

    public int getMinI() {
        return 1;
    }

    public int getMaxI() {
        return points.size() - 2;
    }

}
