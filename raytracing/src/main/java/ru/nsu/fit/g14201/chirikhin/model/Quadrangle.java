package ru.nsu.fit.g14201.chirikhin.model;

import chirikhin.support.MathSupport;
import chirikhin.support.Point3D;

public class Quadrangle extends Shape {
    private final Point3D<Float, Float, Float> point1;
    private final Point3D<Float, Float,Float> point2;
    private final Point3D<Float, Float, Float> point3;
    private final Point3D<Float, Float, Float> point4;

    public Quadrangle(Point3D<Float, Float, Float> point1, Point3D<Float, Float, Float> point2,
                      Point3D<Float, Float, Float> point3, Point3D<Float, Float, Float> point4,
                      OpticalCharacteristics opticalCharacteristics) {
        super(opticalCharacteristics);
        this.point1 = point1;
        this.point2 = point2;
        this.point3 = point3;
        this.point4 = point4;
    }

    public Point3D<Float, Float, Float> getPoint1() {
        return point1;
    }

    public Point3D<Float, Float, Float> getPoint2() {
        return point2;
    }

    public Point3D<Float, Float, Float> getPoint3() {
        return point3;
    }

    public Point3D<Float, Float, Float> getPoint4() {
        return point4;
    }

    @Override
    public float getMaxCoordinate() {
        return MathSupport.getMax(point1, point2, point3, point4);
    }
}
