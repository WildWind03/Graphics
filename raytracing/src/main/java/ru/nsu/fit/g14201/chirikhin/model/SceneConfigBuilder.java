package ru.nsu.fit.g14201.chirikhin.model;


import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class SceneConfigBuilder {
    private Color sceneColor = null;
    private final List<LightSource> lightSourceList = new ArrayList<>();
    private final List<Shape> shapes = new ArrayList<>();

    public SceneConfigBuilder() {

    }

    public SceneConfigBuilder withSceneColor(Color color) {
        sceneColor = color;
        return this;
    }

    public SceneConfigBuilder withLightSource(LightSource lightSource) {
        lightSourceList.add(lightSource);
        return this;
    }

    public SceneConfigBuilder withShape(Shape shape) {
        shapes.add(shape);
        return this;
    }

    public SceneConfig build() throws BuilderException {
        if (null == sceneColor) {
            throw new BuilderException("You need to specify sceneColor");
        }

        return new SceneConfig(sceneColor, lightSourceList, shapes);
    }
}
