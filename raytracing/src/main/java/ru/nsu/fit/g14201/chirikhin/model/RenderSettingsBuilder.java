package ru.nsu.fit.g14201.chirikhin.model;

import chirikhin.support.Point3D;

import java.awt.*;

public class RenderSettingsBuilder {
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

    public RenderSettingsBuilder setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
        return this;
    }

    public RenderSettingsBuilder setGamma(float gamma) {
        this.gamma = gamma;
        return this;
    }

    public RenderSettingsBuilder setDepth(float depth) {
        this.depth = depth;
        return this;
    }

    public RenderSettingsBuilder setQuality(String quality) {
        this.quality = quality;
        return this;
    }

    public RenderSettingsBuilder setCameraPoint(Point3D<Float, Float, Float> cameraPoint) {
        this.cameraPoint = cameraPoint;
        return this;
    }

    public RenderSettingsBuilder setViewPoint(Point3D<Float, Float, Float> viewPoint) {
        this.viewPoint = viewPoint;
        return this;
    }

    public RenderSettingsBuilder setUpVector(Point3D<Float, Float, Float> upVector) {
        this.upVector = upVector;
        return this;
    }

    public RenderSettingsBuilder setZn(float zn) {
        this.zn = zn;
        return this;
    }

    public RenderSettingsBuilder setZf(float zf) {
        this.zf = zf;
        return this;
    }

    public RenderSettingsBuilder setSw(float sw) {
        this.sw = sw;
        return this;
    }

    public RenderSettingsBuilder setSh(float sh) {
        this.sh = sh;
        return this;
    }

    public RenderSettings createRenderSettings() {
        return new RenderSettings(backgroundColor, gamma, depth, quality, cameraPoint, viewPoint, upVector, zn, zf, sw, sh);
    }
}