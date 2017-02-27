package model;

import java.util.logging.Logger;

public class Cell {
    private static final Logger logger = Logger.getLogger(Cell.class.getName());

    private double impact;
    private boolean isAlive;

    public Cell(double impact) {
        this.impact = impact;
    }

    public double getImpact() {
        return impact;
    }

    public void changeState(boolean isAlive) {
        this.isAlive = isAlive;
    }

    public void addImpact(double impact) {
        this.impact+=impact;
    }

    public boolean isAlive() {
        return isAlive;
    }

}
