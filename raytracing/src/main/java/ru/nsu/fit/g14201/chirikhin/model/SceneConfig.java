package ru.nsu.fit.g14201.chirikhin.model;


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

    public List<LightSource> getLightSources() {
        return lightSources;
    }

    public List<Shape> getShapes() {
        return shapes;
    }
}
