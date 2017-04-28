package ru.fit.g14201.chirikhin.wireframe.model;

import chirikhin.matrix.Matrix;
import chirikhin.support.Point;

import java.awt.*;
import java.util.ArrayList;

public class BSpline {
    private final Color color;
    private final int cx;
    private final int cy;
    private final int cz;
    private final Matrix roundMatrix;
    private final ArrayList<Point<Float, Float>> points;

    BSpline(Color color, int cx, int cy, int cz, Matrix roundMatrix, ArrayList<Point<Float, Float>> points) {
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

    public int getCx() {
        return cx;
    }

    public int getCy() {
        return cy;
    }

    public int getCz() {
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
}
