package ru.nsu.fit.g14201.chirikhin.isolines.model;

public class Function {
    public double get(double x, double y) {
        return (Math.exp(-x*x-y*y/2) * Math.cos(4*x) + Math.exp(-3*((x+0.5)*(x+0.5)+y*y/2)));
    }
}
