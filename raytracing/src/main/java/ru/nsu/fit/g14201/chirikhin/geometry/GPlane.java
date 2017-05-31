package ru.nsu.fit.g14201.chirikhin.geometry;

import chirikhin.support.Point3D;

public class GPlane {
    private float a;
    private float b;
    private float c;
    private float d;

    public GPlane(float x1, float y1, float z1, float x2, float y2, float z2, float x3, float y3, float z3) {
        a = getA(x1, y1, z1, x2, y2, z2, x3, y3, z3);
        b = getB(x1, y1, z1, x2, y2, z2, x3, y3, z3);
        c = getC(x1, y1, z1, x2, y2, z2, x3, y3, z3);
        d = getD(x1, y1, z1, x2, y2, z2, x3, y3, z3);
    }

    public GPlane(Point3D<Float, Float, Float> p1, Point3D<Float, Float, Float> p2, Point3D<Float, Float, Float> p3) {
        this(p1.getX(), p1.getY(), p1.getZ(),
                p2.getX(), p2.getY(), p2.getZ(),
                p3.getX(), p3.getY(), p3.getZ());
    }

    private float getA(float x1, float y1, float z1, float x2, float y2, float z2, float x3, float y3, float z3) {
        return y1 * (z2 - z3) + y2 * (z3 - z1) + y3 * (z1 - z2);
    }

    private float getB(float x1, float y1, float z1, float x2, float y2, float z2, float x3, float y3, float z3) {
        return z1 * (x2 - x3) + z2 * (x3 - x1) + z3 * (x1 - x2);
    }

    private float getC(float x1, float y1, float z1, float x2, float y2, float z2, float x3, float y3, float z3) {
        return x1 * (y2 - y3) + x2 * (y3 - y1) + x3 * (y1 - y2);
    }

    private float getD(float x1, float y1, float z1, float x2, float y2, float z2, float x3, float y3, float z3) {
        return -(x1 * (y2 * z3 - y3 * z2) + x2 * (y3 * z1 - y1 * z3) + x3 * (y1 * z2 - y2 * z1));
    }

    public float getA() {
        return a;
    }

    public float getB() {
        return b;
    }

    public float getC() {
        return c;
    }

    public float getD() {
        return d;
    }

    public GPlane normalize() {
        float normalizeK = 1f / (float) Math.sqrt(a * a + b * b + c * c);
        a = a * normalizeK;
        b = b * normalizeK;
        c = c * normalizeK;
        d = d * normalizeK;

        return this;
    }

    public Point3D<Float, Float, Float> getNormal() {
        return new Point3D<>(a, b, c);
    }

    public float getDistanceToZeroPoint() {
        return -d;
    }
}
