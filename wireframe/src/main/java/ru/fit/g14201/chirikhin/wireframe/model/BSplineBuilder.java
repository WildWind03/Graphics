package ru.fit.g14201.chirikhin.wireframe.model;

import chirikhin.matrix.Matrix;
import chirikhin.support.Point;

import java.awt.*;
import java.util.ArrayList;

public class BSplineBuilder {
    private Color color = null;
    private Float cx = null;
    private Float cy = null;
    private Float cz = null;
    private Matrix roundMatrix = null;
    private final ArrayList<Point<Float, Float>> points = new ArrayList<>();

    public BSplineBuilder() {

    }

    public BSplineBuilder withColor(Color color) {
        this.color = color;
        return this;
    }

    public BSplineBuilder withCx(Float cx) {
        this.cx = cx;
        return this;
    }

    public BSplineBuilder withCy(Float cy) {
        this.cy = cy;
        return this;
    }

    public BSplineBuilder withCz(Float cz) {
        this.cz = cz;
        return this;
    }

    public BSplineBuilder withRoundMatrix(Matrix roundMatrix) {
        this.roundMatrix = roundMatrix;
        return this;
    }

    public BSplineBuilder addPoint(float x, float y) {
        points.add(new Point<>(x, y));
        return this;
    }

    public BSpline build() throws ShapeBuildingException {
        if (null == color || null == cz || null == cy || null == cx
                || null == roundMatrix) {
            throw new ShapeBuildingException("Can not build object: not all necessary parameters are set");
        }

        return new BSpline(color, cx, cy, cz, roundMatrix, points);
    }

}
