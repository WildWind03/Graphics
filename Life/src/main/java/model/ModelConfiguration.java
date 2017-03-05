package model;

import java.util.logging.Logger;

public class ModelConfiguration {
    private static final Logger logger = Logger.getLogger(ModelConfiguration.class.getName());

    private final double liveBegin;
    private final double liveEnd;
    private final double birthBegin;
    private final double birthEnd;
    private final double firstImpact;
    private final double secondImpact;

    private final int width;
    private final int height;

    public ModelConfiguration(double liveBegin, double liveEnd, double birthBegin, double birthEnd, double firstImpact, double secondImpact, int width, int height) {
        this.liveBegin = liveBegin;
        this.liveEnd = liveEnd;
        this.birthBegin = birthBegin;
        this.birthEnd = birthEnd;
        this.firstImpact = firstImpact;
        this.secondImpact = secondImpact;
        this.width = width;
        this.height = height;
    }

    public double getLiveBegin() {
        return liveBegin;
    }

    public double getLiveEnd() {
        return liveEnd;
    }

    public double getBirthBegin() {
        return birthBegin;
    }

    public double getBirthEnd() {
        return birthEnd;
    }

    public double getFirstImpact() {
        return firstImpact;
    }

    public double getSecondImpact() {
        return secondImpact;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
