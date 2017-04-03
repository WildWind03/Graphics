package com.chirikhin.interpolated_function;

public interface DataInterpolator<A, V> {
    V apply(Interpolator interpolator, A arg1, V value1, A arg2, V value2, A arg);
}
