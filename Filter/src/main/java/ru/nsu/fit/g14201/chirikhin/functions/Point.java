package ru.nsu.fit.g14201.chirikhin.functions;

public class Point<V> {
    private final double argument;
    private final V value;

    public Point(double argument, V value) {
        this.argument = argument;
        this.value = value;
    }

    public double getArgument() {
        return argument;
    }

    public V getValue() {
        return value;
    }
}