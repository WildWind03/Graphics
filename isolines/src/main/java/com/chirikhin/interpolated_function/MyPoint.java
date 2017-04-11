package com.chirikhin.interpolated_function;

public class MyPoint<T1 extends Comparable<T1>, T2> implements Comparable<MyPoint<T1, T2>>{
    private T1 value1;
    private T2 value2;

    public MyPoint(T1 value1, T2 value2) {
        this.value1 = value1;
        this.value2 = value2;
    }

    public T1 getValue1() {
        return value1;
    }

    public T2 getValue2() {
        return value2;
    }

    @Override
    public int compareTo(MyPoint<T1, T2> o) {
        return value1.compareTo(o.value1);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MyPoint<?, ?> myPoint = (MyPoint<?, ?>) o;

        return value1 != null ? value1.equals(myPoint.value1) : myPoint.value1 == null;
    }

    @Override
    public int hashCode() {
        return value1 != null ? value1.hashCode() : 0;
    }
}