package ru.nsu.fit.g14201.chirikhin.isolines.view;

public class FieldCoordinatesFunctionValue {
    private final double x;
    private final double y;
    private final double funcValue;

    public FieldCoordinatesFunctionValue(double x, double y, double funcValue) {
        this.x = x;
        this.y = y;
        this.funcValue = funcValue;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getFuncValue() {
        return funcValue;
    }
}
