package ru.nsu.fit.g14201.chirikhin.geometry;

public class GLine {
    private final float x1;
    private final float y1;
    private final float z1;
    private final float x2;
    private final float y2;
    private final float z2;

    public GLine(float x1, float y1, float z1, float x2, float y2, float z2) {
        this.x1 = x1;
        this.y1 = y1;
        this.z1 = z1;
        this.x2 = x2;
        this.y2 = y2;
        this.z2 = z2;
    }

    public float getX1() {
        return x1;
    }

    public float getY1() {
        return y1;
    }

    public float getZ1() {
        return z1;
    }

    public float getX2() {
        return x2;
    }

    public float getY2() {
        return y2;
    }

    public float getZ2() {
        return z2;
    }
}
