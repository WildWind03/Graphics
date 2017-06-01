package ru.nsu.fit.g14201.chirikhin.model;

import chirikhin.support.Point3D;

public class OpticalCharacteristics {
    private final Point3D<Float, Float, Float> diffuseReflectRate;
    private final Point3D<Float, Float, Float> mirrorReflectRate;
    private final float power;

    public OpticalCharacteristics(Point3D<Float, Float, Float> diffuseReflectRate, Point3D<Float, Float, Float> mirrorReflectRate, float power) {
        this.diffuseReflectRate = diffuseReflectRate;
        this.mirrorReflectRate = mirrorReflectRate;
        this.power = power;
    }

    public Point3D<Float, Float, Float> getDiffuseReflectRate() {
        return diffuseReflectRate;
    }

    public Point3D<Float, Float, Float> getMirrorReflectRate() {
        return mirrorReflectRate;
    }

    public float getPower() {
        return power;
    }
}
