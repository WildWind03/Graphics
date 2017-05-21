package ru.nsu.fit.g14201.chirikhin.model;

import chirikhin.support.Point3D;

public class TriangleBuilder extends ShapeBuilder {
    private Point3D<Float, Float, Float> point1;
    private Point3D<Float, Float, Float> point2;
    private Point3D<Float, Float, Float> point3;

    public TriangleBuilder setPoint1(Point3D<Float, Float, Float> point1) {
        this.point1 = point1;
        return this;
    }

    public TriangleBuilder setPoint2(Point3D<Float, Float, Float> point2) {
        this.point2 = point2;
        return this;
    }

    public TriangleBuilder setPoint3(Point3D<Float, Float, Float> point3) {
        this.point3 = point3;
        return this;
    }

    public TriangleBuilder setOpticalCharacteristics(OpticalCharacteristics opticalCharacteristics) {
        this.opticalCharacteristics = opticalCharacteristics;
        return this;
    }

    @Override
    public Shape build() {
        return new Triangle(point1, point2, point3, opticalCharacteristics);
    }
}