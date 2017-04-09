package ru.nsu.fit.g14201.chirikhin.isolines.view;

public class SettingParams {
    private final int gridWidthDivs;
    private final int gridHeightDivs;
    private final int redColor;
    private final int greenColor;
    private final int blueColor;
    private final int x0;
    private final int y0;
    private final int x1;
    private final int y1;

    public SettingParams(int gridWidthDivs, int gridHeightDivs, int redColor, int greenColor, int blueColor, int x0, int y0, int x1, int y1) {
        this.gridWidthDivs = gridWidthDivs;
        this.gridHeightDivs = gridHeightDivs;
        this.redColor = redColor;
        this.greenColor = greenColor;
        this.blueColor = blueColor;
        this.x0 = x0;
        this.y0 = y0;
        this.x1 = x1;
        this.y1 = y1;
    }

    public int getGridWidthDivs() {
        return gridWidthDivs;
    }

    public int getGridHeightDivs() {
        return gridHeightDivs;
    }

    public int getRedColor() {
        return redColor;
    }

    public int getGreenColor() {
        return greenColor;
    }

    public int getBlueColor() {
        return blueColor;
    }

    public int getX0() {
        return x0;
    }

    public int getY0() {
        return y0;
    }

    public int getX1() {
        return x1;
    }

    public int getY1() {
        return y1;
    }
}
