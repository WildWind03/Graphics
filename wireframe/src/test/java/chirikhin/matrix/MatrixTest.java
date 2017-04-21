package chirikhin.matrix;

import org.junit.Assert;
import org.junit.Test;

public class MatrixTest {
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidCreationMatrix() {
        Matrix matrix = new Matrix(new float[][] {{2, 4}, null});
    }

    @Test
    public void testGetters() {
        Matrix matrix = new Matrix(new float[][] {{1}});
        Assert.assertTrue(1 == matrix.get(0, 0));
        Assert.assertTrue(3 == new Matrix(new float[][] {{2, 3}, {3, 1}}).get(1, 0));
    }

    @Test
    public void testPlus() {
        Matrix m1 = new Matrix(new float[][] {{1, 2}, {-2, -3}});
        Matrix m2 = new Matrix(new float[][] {{0, 0}, {5, 6}});
        Matrix m3 = MatrixUtil.plus(m1, m2);
        Assert.assertTrue(m3.get(1, 1) == 3);
    }

    @Test
    public void testMultiply() {
        Matrix m1 = new Matrix(new float[][] {{2, 3}});
        Matrix m2 = new Matrix(new float[][] {{1, 5}, {6, 3}});
        Matrix m3 = MatrixUtil.multiply(m1, m2);
        Assert.assertTrue(m3.get(0, 0) == 20);
    }
}