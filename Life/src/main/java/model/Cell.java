package model;

import java.util.logging.Logger;

public class Cell {

    private double impact;
    private boolean isAlive;

    Cell(double impact) {
        this.impact = impact;
    }

    public double getImpact() {
        return impact;
    }

    void changeState(boolean isAlive) {
        this.isAlive = isAlive;
    }

    void addImpact(double impact) {
        this.impact+=impact;
    }

    void subtractImpact(double impact) {
        this.impact-=impact;
    }

    public boolean isAlive() {
        return isAlive;
    }

    void setImpact(double newImpact) {
        this.impact = newImpact;
    }

}
