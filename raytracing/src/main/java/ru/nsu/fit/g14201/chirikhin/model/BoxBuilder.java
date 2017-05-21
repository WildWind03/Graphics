package ru.nsu.fit.g14201.chirikhin.model;

import chirikhin.support.Point3D;

public class BoxBuilder extends ShapeBuilder {
    private Point3D<Float, Float, Float> minPoint;
    private Point3D<Float, Float, Float> maxPoint;

    public BoxBuilder setMinPoint(Point3D<Float, Float, Float> minPoint) {
        this.minPoint = minPoint;
        return this;
    }

    public BoxBuilder setMaxPoint(Point3D<Float, Float, Float> maxPoint) {
        this.maxPoint = maxPoint;
        return this;
    }

    public BoxBuilder setOpticalCharacteristics(OpticalCharacteristics opticalCharacteristics) {
        this.opticalCharacteristics = opticalCharacteristics;
        return this;
    }

    @Override
    public Shape build() {
        return new Box(minPoint, maxPoint, opticalCharacteristics);
    }
}