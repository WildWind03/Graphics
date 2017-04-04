package ru.nsu.fit.g14201.chirikhin.isolines.function;

public class DifficultFunctionSingleton implements MyFunction {

    private DifficultFunctionSingleton() {

    }

    private static class DifficultFunctionSingletonHolder {
        private static final DifficultFunctionSingleton INSTANCE = new DifficultFunctionSingleton();
    }

    @Override
    public Double apply(Double x, Double y) {
        return (Math.exp(-x*x-y*y/2) * Math.cos(4*x) + Math.exp(-3*((x+0.5)*(x+0.5)+y*y/2)));
    }

    public static DifficultFunctionSingleton getInstance() {
        return DifficultFunctionSingletonHolder.INSTANCE;
    }
}
