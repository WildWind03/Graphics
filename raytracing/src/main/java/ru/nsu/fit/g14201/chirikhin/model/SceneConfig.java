package ru.nsu.fit.g14201.chirikhin.model;


import chirikhin.support.Point3D;

import java.awt.*;
import java.util.List;

public class SceneConfig {
    private final Color spaceColor;
    private final List<LightSource> lightSources;
    private final List<Shape> shapes;

    public SceneConfig(Color spaceColor, List<LightSource> lightSources, List<Shape> shapes) {
        this.spaceColor = spaceColor;
        this.lightSources = lightSources;
        this.shapes = shapes;
    }

    public Color getSpaceColor() {
        return spaceColor;
    }

    public Point3D<Float, Float, Float> getSpaceColorAsPoint() {
        return new Point3D<>(spaceColor.getRed() / 255f, spaceColor.getGreen() / 255f, spaceColor.getBlue() / 255f);
    }

    public List<LightSource> getLightSources() {
        return lightSources;
    }

    public List<Shape> getShapes() {
        return shapes;
    }
}
