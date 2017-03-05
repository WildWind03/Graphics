package view;

import model.ModelConfiguration;

import java.util.logging.Logger;

public class Configuration {
    private static final Logger logger = Logger.getLogger(Configuration.class.getName());

    private final int width;
    private final int height;

    private final double lineWidth;
    private final double lineLength;

    private final double liveBegin;
    private final double liveEnd;
    private final double birthBegin;
    private final double birthEnd;
    private final double firstImpact;
    private final double secondImpact;

    private final boolean isReplaceMode;

    public Configuration(int width, int height, double lineWidth, double lineLength, double liveBegin, double liveEnd, double birthBegin, double birthEnd, double firstImpact, double secondImpact, boolean isReplaceMode) {
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

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
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

    public double getLineWidth() {
        return lineWidth;
    }

    public double getLineLength() {
        return lineLength;
    }

    public boolean isReplaceMode() {
        return isReplaceMode;
    }
}
