package ru.fit.g14201.chirikhin.wireframe.model;

import chirikhin.matrix.Matrix;
import chirikhin.support.Point;

import java.awt.*;
import java.util.ArrayList;

public class BSpline {
    private Color color;
    private final float cx;
    private final float cy;
    private final float cz;
    private final Matrix roundMatrix;
    private final ArrayList<Point<Float, Float>> points;

    BSpline(Color color, float cx, float cy, float cz, Matrix roundMatrix, ArrayList<Point<Float, Float>> points) {
        this.color = color;
        this.cx = cx;
        this.cy = cy;
        this.cz = cz;
        this.roundMatrix = roundMatrix;
        this.points = points;
    }

    public void addPoint(Point<Float, Float> point) {
        points.add(point);
    }

    public Color getColor() {
        return color;
    }

    public float getCx() {
        return cx;
    }

    public float getCy() {
        return cy;
    }

    public float getCz() {
        return cz;
    }

    public Matrix getRoundMatrix() {
        return roundMatrix;
    }

    public ArrayList<Point<Float, Float>> getPoints() {
        return points;
    }

    public boolean isEmpty() {
        return getPoints().isEmpty();
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
