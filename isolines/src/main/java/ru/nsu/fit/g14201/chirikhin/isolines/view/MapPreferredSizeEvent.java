package ru.nsu.fit.g14201.chirikhin.isolines.view;

public class MapPreferredSizeEvent {
    private final int width;
    private final int height;

    public MapPreferredSizeEvent(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
