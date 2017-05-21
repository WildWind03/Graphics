package ru.nsu.fit.g14201.chirikhin.model;

import chirikhin.support.Point;
import chirikhin.support.Point3D;

import java.awt.*;

public class LightSourceBuilder {
    private Point3D<Float, Float, Float> point3D;
    private Color color;

    public LightSourceBuilder() {

    }

    public LightSourceBuilder withColor(Color color) {
        this.color = color;
        return this;
    }

    public LightSourceBuilder withPosition(Point3D<Float, Float, Float> point3D) {
        this.point3D = point3D;
        return this;
    }

    public LightSource build() throws BuilderException {
        if (null == color || null == point3D) {
            throw new BuilderException("You must specify color and position for light source");
        }

        return new LightSource(point3D, color);
    }
}
