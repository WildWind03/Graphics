package ru.fit.g14201.chirikhin.wireframe.model;

import chirikhin.matrix.Matrix;
import chirikhin.support.Point;

import java.awt.*;
import java.util.ArrayList;

public class ShapeBuilder {
    private Color color = null;
    private Integer cx = null;
    private Integer cy = null;
    private Integer cz = null;
    private Matrix roundMatrix = null;
    private final ArrayList<Point<Float, Float>> points = new ArrayList<>();

    public ShapeBuilder() {

    }

    public ShapeBuilder withColor(Color color) {
        this.color = color;
        return this;
    }

    public ShapeBuilder withCx(int cx) {
        this.cx = cx;
        return this;
    }

    public ShapeBuilder withCy(int cy) {
        this.cy = cy;
        return this;
    }

    public ShapeBuilder withCz(int cz) {
        this.cz = cz;
        return this;
    }

    public ShapeBuilder withRoundMatrix(Matrix roundMatrix) {
        this.roundMatrix = roundMatrix;
        return this;
    }

    public ShapeBuilder addPoint(float x, float y) {
        points.add(new Point<>(x, y));
        return this;
    }

    public Shape build() throws ShapeBuildingException {
        if (null == color || null == cz || null == cy || null == cx
                || null == roundMatrix) {
            throw new ShapeBuildingException("Can not build object: not all necessary parameters are set");
        }

        return new Shape(color, cx, cy, cz, roundMatrix, points);
    }

}
