package ru.nsu.fit.g14201.chirikhin.model;

public class ModelConfiguration {

    private final float liveBegin;
    private final float liveEnd;
    private final float birthBegin;
    private final float birthEnd;
    private final float firstImpact;
    private final float secondImpact;

    private final int width;
    private final int height;

    public ModelConfiguration(float liveBegin, float liveEnd, float birthBegin, float birthEnd, float firstImpact, float secondImpact, int width, int height) {
        this.liveBegin = liveBegin;
        this.liveEnd = liveEnd;
        this.birthBegin = birthBegin;
        this.birthEnd = birthEnd;
        this.firstImpact = firstImpact;
        this.secondImpact = secondImpact;
        this.width = width;
        this.height = height;
    }

    public float getLiveBegin() {
        return liveBegin;
    }

    public float getLiveEnd() {
        return liveEnd;
    }

    public float getBirthBegin() {
        return birthBegin;
    }

    public float getBirthEnd() {
        return birthEnd;
    }

    public float getFirstImpact() {
        return firstImpact;
    }

    public float getSecondImpact() {
        return secondImpact;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
