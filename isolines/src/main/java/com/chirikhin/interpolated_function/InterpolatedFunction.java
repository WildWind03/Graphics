package com.chirikhin.interpolated_function;

import java.util.ArrayList;
import java.util.LinkedList;

public class InterpolatedFunction {
    private final ArrayList<MyPoint<Double, Double>> points;
    private final Interpolator interpolator;

    public InterpolatedFunction(LinkedList<MyPoint<Double, Double>> points, Interpolator interpolator) {
        this.points = new ArrayList<>(points);

        this.interpolator = interpolator;
    }

    public Double getValue(Double arg) {
        int counter = 0;

        for (MyPoint<Double, Double> point : points) {
            if (arg.equals(point.getValue1())) {
                return point.getValue2();
            }
            if (arg.compareTo(point.getValue1()) < 0) {
                break;
            }

            counter++;
        }

        if (0 == counter) {
            return points.get(0).getValue2();
        } else {
            if (points.size() == counter) {
                return points.get(counter - 1).getValue2();
            } else {
                return interpolator.get(points.get(counter - 1).getValue1(),
                        points.get(counter - 1).getValue2(),
                        points.get(counter).getValue1(),
                        points.get(counter).getValue2(),
                        arg);
            }
        }
    }
}
