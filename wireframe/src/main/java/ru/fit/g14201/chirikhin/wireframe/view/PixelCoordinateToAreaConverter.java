package ru.fit.g14201.chirikhin.wireframe.view;

public class PixelCoordinateToAreaConverter {
    private final float startX;
    private final float startY;
    private final float endX;
    private final float endY;

    private final int pixelFieldWidth;
    private final int pixelFieldHeight;

    public PixelCoordinateToAreaConverter(float startX, float startY, float endX, float endY, int pixelFieldWidth, int pixelFieldHeight) {
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
        this.pixelFieldWidth = pixelFieldWidth;
        this.pixelFieldHeight = pixelFieldHeight;
    }

    public float toRealX(int u) {
        return (endX - startX) * (float) u / (float) pixelFieldWidth + startX;
    }

    public float toRealY(int v) {
        return (endY - startY) * (float) v / (float) pixelFieldHeight + startY;
    }

    public int toPixelX(float imX) {
        return (int) (imX / (endX - startX)) * pixelFieldWidth;
    }

    public int toPixelY(float imY) {
        return (int) (imY / (endY - startY)) * pixelFieldHeight;
    }

    public int getPixelFieldWidth() {
        return pixelFieldWidth;
    }

    public int getPixelFieldHeight() {
        return pixelFieldHeight;
    }
}
