package ru.fit.g14201.chirikhin.wireframe.model;

import chirikhin.matrix.Matrix;

import java.awt.*;
import java.util.ArrayList;

public class Model {
    private int n;
    private int m;
    private int k;
    private float a;
    private float b;
    private int c;
    private int d;
    private float zn;
    private float zf;
    private float sw;
    private float sh;
    private Matrix roundMatrix;
    private Color backgroundColor;
    private final ArrayList<BSpline> bSplines = new ArrayList<>();

    public Model() {

    }

    public Model(int n, int m, int k, int a, int b, int c, int d,
                 int zn, int zf, int sw, int sh,
                 Matrix roundMatrix, Color backgroundColor) {
        this.n = n;
        this.m = m;
        this.k = k;
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        this.zn = zn;
        this.zf = zf;
        this.sw = sw;
        this.sh = sh;
        this.roundMatrix = roundMatrix;
        this.backgroundColor = backgroundColor;
    }

    public void setN(int n) {
        this.n = n;
    }

    public void setM(int m) {
        this.m = m;
    }

    public void setK(int k) {
        this.k = k;
    }

    public void setA(float a) {
        this.a = a;
    }

    public void setB(float b) {
        this.b = b;
    }

    public void setC(int c) {
        this.c = c;
    }

    public void setD(int d) {
        this.d = d;
    }

    public void setZn(float zn) {
        this.zn = zn;
    }

    public void setZf(float zf) {
        this.zf = zf;
    }

    public void setSw(float sw) {
        this.sw = sw;
    }

    public void setSh(float sh) {
        this.sh = sh;
    }

    public void setRoundMatrix(Matrix roundMatrix) {
        this.roundMatrix = roundMatrix;
    }

    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public void addShape(BSpline BSpline) {
        bSplines.add(BSpline);
    }

    public int getN() {
        return n;
    }

    public int getM() {
        return m;
    }

    public int getK() {
        return k;
    }

    public float getA() {
        return a;
    }

    public float getB() {
        return b;
    }

    public int getC() {
        return c;
    }

    public int getD() {
        return d;
    }

    public float getZn() {
        return zn;
    }

    public float getZf() {
        return zf;
    }

    public float getSw() {
        return sw;
    }

    public float getSh() {
        return sh;
    }

    public Matrix getRoundMatrix() {
        return roundMatrix;
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public ArrayList<BSpline> getbSplines() {
        return bSplines;
    }

    public boolean isEmpty() {
        return getbSplines().isEmpty();
    }
}
