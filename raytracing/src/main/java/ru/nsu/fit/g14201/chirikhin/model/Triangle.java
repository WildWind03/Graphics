package ru.nsu.fit.g14201.chirikhin.model;

import chirikhin.support.Point3D;

public class Triangle extends Shape {
    private final Point3D<Float, Float, Float> point1;
    private final Point3D<Float, Float,Float> point2;
    private final Point3D<Float, Float, Float> point3;

    public Triangle(Point3D<Float, Float, Float> point1, Point3D<Float, Float, Float> point2,
                    Point3D<Float, Float, Float> point3,
                    OpticalCharacteristics opticalCharacteristics) {
        super(opticalCharacteristics);
        this.point1 = point1;
        this.point2 = point2;
        this.point3 = point3;
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
}
