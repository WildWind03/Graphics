package ru.nsu.fit.g14201.chirikhin.model;

import chirikhin.support.Point3D;

import java.awt.*;

public class RenderSettings {
    private final Color backgroundColor;
    private final float gamma;
    private final float depth;
    private final String quality;
    private final Point3D<Float, Float, Float> cameraPoint;
    private final Point3D<Float, Float, Float> viewPoint;
    private final Point3D<Float, Float, Float> upVector;
    private final float zn;
    private final float zf;
    private final float sw;
    private final float sh;

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


}
