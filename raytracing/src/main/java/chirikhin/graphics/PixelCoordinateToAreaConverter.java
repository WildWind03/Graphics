package chirikhin.graphics;

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
        return (((float) u) / pixelFieldWidth) * (endX - startX) + startX;
    }

    public float toRealY(int v) {
        return ((float) v / pixelFieldHeight) * (endY - startY) + startY;
    }

    public int toPixelX(float imX) {
        return pixelFieldWidth / 2 + (int) ((imX / (endX - startX)) * pixelFieldWidth);
    }

    public int toPixelY(float imY) {
        return pixelFieldHeight / 2 - (int) ((imY / (endY - startY)) * pixelFieldHeight);
    }

    public int getPixelFieldWidth() {
        return pixelFieldWidth;
    }

    public int getPixelFieldHeight() {
        return pixelFieldHeight;
    }

    public float getStartX() {
        return startX;
    }

    public float getStartY() {
        return startY;
    }

    public float getEndX() {
        return endX;
    }

    public float getEndY() {
        return endY;
    }
}
