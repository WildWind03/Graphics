package chirikhin.matrix;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import java.util.Arrays;


public class Matrix {
    private final float data[][];


    @SuppressFBWarnings
    public Matrix(float[][] data) {
        if (null == data || null == data[0]) {
            throw new IllegalArgumentException("Data can not be null");
        }

        int necessaryLength = data[0].length;

        for (float[] row : data) {
            if (row == null || row.length != necessaryLength) {
                    throw new IllegalArgumentException("All rows must be not null and have the same length");
            }
        }

        this.data = data;
    }

    public int getHeight() {
        return data.length;
    }

    public int getWidth() {
        return data[0].length;
    }

    public float get(int h, int w) {
        if (h >= getHeight() || w >= getWidth()) {
            throw new IndexOutOfBoundsException("Out of matrix: " + h + " " + getHeight()
                    + " : " + w + " " + getWidth());
        }

        return data[h][w];
    }

    public void set(int h, int w, float value) {
        if (h >= getHeight() || w >= getWidth()) {
            throw new IndexOutOfBoundsException("Out of matrix");
        }

        data[h][w] = value;
    }

    public void add(int h, int w, float value) {
        if (h >= getHeight() || w >= getWidth()) {
            throw new IndexOutOfBoundsException("Out of matrix");
        }

        set(h, w, data[h][w] + value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Matrix matrix = (Matrix) o;

        return Arrays.deepEquals(data, matrix.data);

    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(data);
    }
}
