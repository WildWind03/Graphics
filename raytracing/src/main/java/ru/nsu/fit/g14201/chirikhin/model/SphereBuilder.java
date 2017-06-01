package ru.nsu.fit.g14201.chirikhin.model;

import chirikhin.support.Point3D;

public class SphereBuilder extends ShapeBuilder {
    private Point3D<Float, Float, Float> center;
    private float radius;

    public SphereBuilder setCenter(Point3D<Float, Float, Float> center) {
        this.center = center;
        return this;
    }

    public SphereBuilder setOpticalCharacteristics(OpticalCharacteristics opticalCharacteristics) {
        this.opticalCharacteristics = opticalCharacteristics;
        return this;
    }

    @Override
    public Shape build() {
        return new Sphere(center, opticalCharacteristics, radius);
    }

    public SphereBuilder setRadius(float radius) {
        this.radius = radius;
        return this;
    }
}