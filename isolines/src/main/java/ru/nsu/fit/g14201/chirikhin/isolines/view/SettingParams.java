package ru.nsu.fit.g14201.chirikhin.isolines.view;

public class SettingParams {
    private final int gridWidthDivs;
    private final int gridHeightDivs;
    private final int redColor;
    private final int greenColor;
    private final int blueColor;

    public SettingParams(int gridWidthDivs, int gridHeightDivs, int redColor, int greenColor, int blueColor) {
        this.gridWidthDivs = gridWidthDivs;
        this.gridHeightDivs = gridHeightDivs;
        this.redColor = redColor;
        this.greenColor = greenColor;
        this.blueColor = blueColor;
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
}
