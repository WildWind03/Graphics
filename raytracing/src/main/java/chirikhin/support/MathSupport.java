package chirikhin.support;

import chirikhin.swing.util.ListUtil;

import javax.swing.text.html.ListView;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.DoubleStream;

public class MathSupport {
    private MathSupport() {

    }

    public static float getMax(double... arg) {
        return (float) DoubleStream.of(arg)
                .map(Math::abs)
                .max()
                .orElse(0);
    }

    public static float getMax(Point3D<Float, Float, Float>... p) {
        double[] arrayList = new double[p.length * 3];

        for (int k = 0; k < p.length; k+=1) {
            arrayList[3 * k] = p[k].getX();
            arrayList[3 * k + 1] = p[k].getY();
            arrayList[3 * k + 2] = p[k].getZ();
        }

        return getMax(arrayList);
    }

    public static Point3D<Float, Float, Float> crossProduct(Point3D<Float, Float, Float> vector1,
                                                             Point3D<Float, Float, Float> vector2) {
        return new Point3D<>(vector1.getY() * vector2.getZ() - vector1.getZ() * vector2.getY(),
                - (vector1.getX() * vector2.getZ() - vector1.getZ() * vector2.getX()),
                vector1.getX() * vector2.getY() - vector1.getY() * vector2.getX());
    }

    public static Point3D<Float, Float, Float> minus(Point3D<Float, Float, Float> point1, Point3D<Float, Float, Float> point2) {
        return new Point3D<>(point1.getX() - point2.getX(), point1.getY() - point2.getY(), point1.getZ() - point2.getZ());
    }
}
