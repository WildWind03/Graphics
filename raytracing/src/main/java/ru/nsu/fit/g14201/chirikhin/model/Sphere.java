package ru.nsu.fit.g14201.chirikhin.model;

import chirikhin.support.Point3D;

public class Sphere extends Shape {
    private final Point3D<Float, Float, Float> center;
    private final float radius;

    public Sphere(Point3D<Float, Float, Float> center, OpticalCharacteristics opticalCharacteristics, float radius) {
        super(opticalCharacteristics);
        this.center = center;
        this.radius = radius;
    }

    public Point3D<Float, Float, Float> getCenter() {
        return center;
    }

    public float getRadius() {
        return radius;
    }

    @Override
    public float getMaxCoordinate() {
        return 0;
    }
}
