package ru.nsu.fit.g14201.chirikhin.isolines.model;

public class PixelCoordinateToAreaConverter {
    private final double startX;
    private final double startY;
    private final double endX;
    private final double endY;

    private final int pixelFieldWidth;
    private final int pixelFieldHeight;

    public PixelCoordinateToAreaConverter(double startX, double startY, double endX, double endY, int pixelFieldWidth, int pixelFieldHeight) {
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
        this.pixelFieldWidth = pixelFieldWidth;
        this.pixelFieldHeight = pixelFieldHeight;
    }

    public double toRealX(int u) {
        return (endX - startX) * (double) u / (double) pixelFieldWidth + startX;
    }

    public double toRealY(int v) {
        return (endY - startY) * (double) v / (double) pixelFieldHeight + startY;
    }

    public int getPixelFieldWidth() {
        return pixelFieldWidth;
    }

    public int getPixelFieldHeight() {
        return pixelFieldHeight;
    }
}
