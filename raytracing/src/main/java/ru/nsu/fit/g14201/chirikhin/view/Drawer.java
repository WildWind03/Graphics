package ru.nsu.fit.g14201.chirikhin.view;

abstract class Drawer {
    private float rate = 1;

    abstract void draw(ShapeView shapeView);

    void setRate(float rate){
        this.rate = rate;
    }

    protected float getRate() {
        return rate;
    }
}
