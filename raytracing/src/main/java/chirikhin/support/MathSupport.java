package chirikhin.support;

import chirikhin.matrix.Matrix;
import chirikhin.swing.util.ListUtil;
import ru.nsu.fit.g14201.chirikhin.model.Triangle;

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

    public static Point3D<Float, Float, Float> createVector(Point3D<Float, Float, Float> p1, Point3D<Float, Float, Float> p2) {
        return new Point3D<>(p2.getX() - p1.getX(), p2.getY() - p1.getY(), p2.getZ() - p1.getZ());
    }

    public static float scalarMultiply(Point3D<Float, Float, Float> p1, Point3D<Float, Float, Float> p2) {
        return p1.getX() * p2.getX() + p1.getY() * p2.getY() + p1.getZ() * p2.getZ();
    }

    public static boolean isInTriangle(Triangle triangle, Point3D<Float, Float, Float> point3D) {
        Matrix A = new Matrix(new float[][]{{triangle.getPoint1().getX()}, {triangle.getPoint1().getY()}, {triangle.getPoint1().getZ()}});
        Matrix v0 =  new Matrix(new float[][]{{triangle.getPoint3().getX()}, {triangle.getPoint3().getY()}, {triangle.getPoint3().getZ()}}).subtract(A);
        Matrix v1 =  new Matrix(new float[][]{{triangle.getPoint2().getX()}, {triangle.getPoint2().getY()}, {triangle.getPoint2().getZ()}}).subtract(A);
        Matrix v2 =  new Matrix(new float[][]{{point3D.getX()}, {point3D.getY()}, {point3D.getZ()}}).subtract(A);

        double dot00 = v0.scalarMult(v0);
        double dot01 = v0.scalarMult(v1);
        double dot02 = v0.scalarMult(v2);
        double dot11 = v1.scalarMult(v1);
        double dot12 = v1.scalarMult(v2);

        double invDenom = 1 / (dot00 * dot11 - dot01 * dot01);
        double u = (dot11 * dot02 - dot01 * dot12) * invDenom;
        double v = (dot00 * dot12 - dot01 * dot02) * invDenom;

        return (u >= 0) && (v >= 0) && (u + v < 1);
    }
}
