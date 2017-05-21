package ru.nsu.fit.g14201.chirikhin.model;

import chirikhin.support.Point3D;

import java.awt.*;

public class RenderSettings {
    private Color backgroundColor;
    private float gamma;
    private float depth;
    private String quality;
    private Point3D<Float, Float, Float> cameraPoint;
    private Point3D<Float, Float, Float> viewPoint;
    private Point3D<Float, Float, Float> upVector;
    private float zn;
    private float zf;
    private float sw;
    private float sh;

    public RenderSettings(Color backgroundColor, float gamma, float depth, String quality,
                          Point3D<Float, Float, Float> cameraPoint,
                          Point3D<Float, Float, Float> viewPoint,
                          Point3D<Float, Float, Float> upVector,
                          float zn, float zf, float sw, float sh) {
        this.backgroundColor = backgroundColor;
        this.gamma = gamma;
        this.depth = depth;
        this.quality = quality;
        this.cameraPoint = cameraPoint;
        this.viewPoint = viewPoint;
        this.upVector = upVector;
        this.zn = zn;
        this.zf = zf;
        this.sw = sw;
        this.sh = sh;
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public float getGamma() {
        return gamma;
    }

    public float getDepth() {
        return depth;
    }

    public String getQuality() {
        return quality;
    }

    public Point3D<Float, Float, Float> getCameraPoint() {
        return cameraPoint;
    }

    public Point3D<Float, Float, Float> getViewPoint() {
        return viewPoint;
    }

    public Point3D<Float, Float, Float> getUpVector() {
        return upVector;
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

    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public void setGamma(float gamma) {
        this.gamma = gamma;
    }

    public void setDepth(float depth) {
        this.depth = depth;
    }

    public void setQuality(String quality) {
        this.quality = quality;
    }

    public void setCameraPoint(Point3D<Float, Float, Float> cameraPoint) {
        this.cameraPoint = cameraPoint;
    }

    public void setViewPoint(Point3D<Float, Float, Float> viewPoint) {
        this.viewPoint = viewPoint;
    }

    public void setUpVector(Point3D<Float, Float, Float> upVector) {
        this.upVector = upVector;
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
}
