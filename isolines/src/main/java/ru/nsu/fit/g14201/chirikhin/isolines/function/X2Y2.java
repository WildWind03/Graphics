package ru.nsu.fit.g14201.chirikhin.isolines.function;

public class X2Y2 implements MyFunction{
    @Override
    public Double apply(Double aDouble, Double aDouble2) {
        return aDouble * aDouble - aDouble2 * aDouble2;
    }
}
