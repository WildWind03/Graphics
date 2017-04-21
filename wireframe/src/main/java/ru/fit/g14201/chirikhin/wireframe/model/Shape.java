package ru.fit.g14201.chirikhin.wireframe.model;

import chirikhin.matrix.Matrix;
import ru.fit.g14201.chirikhin.wireframe.bspline.Point;

import java.awt.*;
import java.util.ArrayList;

public class Shape {
    private final Color color;
    private final int cx;
    private final int cy;
    private final int cz;
    private final Matrix roundMatrix;
    private final ArrayList<Point> points;

    Shape(Color color, int cx, int cy, int cz, Matrix roundMatrix, ArrayList<Point> points) {
        this.color = color;
        this.cx = cx;
        this.cy = cy;
        this.cz = cz;
        this.roundMatrix = roundMatrix;
        this.points = points;
    }

    public void addPoint(Point point) {
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

    public ArrayList<Point> getPoints() {
        return points;
    }
}
