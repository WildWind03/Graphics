package ru.nsu.fit.g14201.chirikhin.filter.model;

public abstract class BaseMatrix {
    protected final double[][] matrix;
    private final int width;
    private final int height;

    public BaseMatrix(int width, int height) {
        this.width = width;
        this.height = height;

        matrix = new double[height][];

        for (int k = 0; k < height; ++k) {
            matrix[k] = new double[width];
        }
    }

    public double getValue(int x, int y) {
        return matrix[y][x];
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
