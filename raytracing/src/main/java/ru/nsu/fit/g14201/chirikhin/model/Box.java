package ru.nsu.fit.g14201.chirikhin.model;

import chirikhin.support.MathSupport;
import chirikhin.support.Point3D;

public class Box extends Shape{
    private final Point3D<Float, Float, Float> minPoint;
    private final Point3D<Float, Float, Float> maxPoint;

    public Box(Point3D<Float, Float, Float> minPoint, Point3D<Float, Float, Float> maxPoint, OpticalCharacteristics opticalCharacteristics) {
        super(opticalCharacteristics);
        this.minPoint = minPoint;
        this.maxPoint = maxPoint;
    }

    public Point3D<Float, Float, Float> getMinPoint() {
        return minPoint;
    }

    public Point3D<Float, Float, Float> getMaxPoint() {
        return maxPoint;
    }

    @Override
    public float getMaxCoordinate() {
        return MathSupport.getMax(minPoint, maxPoint);
    }
}
