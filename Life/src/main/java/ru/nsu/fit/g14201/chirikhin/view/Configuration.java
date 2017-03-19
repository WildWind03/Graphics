package ru.nsu.fit.g14201.chirikhin.view;

import ru.nsu.fit.g14201.chirikhin.model.ModelConfiguration;

public class Configuration {

    private final int width;
    private final int height;

    private final int lineWidth;
    private final int lineLength;

    private final float liveBegin;
    private final float liveEnd;
    private final float birthBegin;
    private final float birthEnd;
    private final float firstImpact;
    private final float secondImpact;

    private final boolean isReplaceMode;

    public Configuration(int width, int height, int lineWidth, int lineLength, float liveBegin, float liveEnd, float birthBegin, float birthEnd, float firstImpact, float secondImpact, boolean isReplaceMode) {
        this.width = width;
        this.height = height;
        this.lineWidth = lineWidth;
        this.lineLength = lineLength;
        this.liveBegin = liveBegin;
        this.liveEnd = liveEnd;
        this.birthBegin = birthBegin;
        this.birthEnd = birthEnd;
        this.firstImpact = firstImpact;
        this.secondImpact = secondImpact;
        this.isReplaceMode = isReplaceMode;
    }

    public Configuration(ModelConfiguration modelConfiguration, int lineLength, int lineWidth, boolean replaceMode) {
        this.birthBegin = modelConfiguration.getBirthBegin();
        this.birthEnd = modelConfiguration.getBirthEnd();
        this.firstImpact = modelConfiguration.getFirstImpact();
        this.secondImpact = modelConfiguration.getSecondImpact();
        this.liveBegin = modelConfiguration.getLiveBegin();
        this.liveEnd = modelConfiguration.getLiveEnd();
        this.width = modelConfiguration.getWidth();
        this.height = modelConfiguration.getHeight();

        this.isReplaceMode = replaceMode;
        this.lineLength = lineLength;
        this.lineWidth = lineWidth;
    }

    public ModelConfiguration getModelConfiguration() {
        return new ModelConfiguration(liveBegin, liveEnd, birthBegin, birthEnd, firstImpact, secondImpact, width, height);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
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

    public int getLineWidth() {
        return lineWidth;
    }

    public int getLineLength() {
        return lineLength;
    }

    public boolean isReplaceMode() {
        return isReplaceMode;
    }
}
