package ru.nsu.fit.g14201.chirikhin.model;

import chirikhin.support.Point3D;

import java.awt.*;

public class LightSource {
    private final Point3D<Float, Float, Float> position;
    private final Color color;

    public LightSource(Point3D<Float, Float, Float> position, Color color) {
        this.position = position;
        this.color = color;
    }

    public Point3D<Float, Float, Float> getPosition() {
        return position;
    }

    public Color getColor() {
        return color;
    }
}
