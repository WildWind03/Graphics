package ru.nsu.fit.g14201.chirikhin.model;

import chirikhin.support.Point3D;

public class QuadrangleBuilder extends ShapeBuilder {
    private Point3D<Float, Float, Float> point1;
    private Point3D<Float, Float, Float> point2;
    private Point3D<Float, Float, Float> point3;
    private Point3D<Float, Float, Float> point4;

    public QuadrangleBuilder setPoint1(Point3D<Float, Float, Float> point1) {
        this.point1 = point1;
        return this;
    }

    public QuadrangleBuilder setPoint2(Point3D<Float, Float, Float> point2) {
        this.point2 = point2;
        return this;
    }

    public QuadrangleBuilder setPoint3(Point3D<Float, Float, Float> point3) {
        this.point3 = point3;
        return this;
    }

    public QuadrangleBuilder setPoint4(Point3D<Float, Float, Float> point4) {
        this.point4 = point4;
        return this;
    }

    public QuadrangleBuilder setOpticalCharacteristics(OpticalCharacteristics opticalCharacteristics) {
        this.opticalCharacteristics = opticalCharacteristics;
        return this;
    }

    @Override
    public Shape build() {
        return new Quadrangle(point1, point2, point3, point4, opticalCharacteristics);
    }
}