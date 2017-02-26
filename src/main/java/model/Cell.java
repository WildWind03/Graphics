package model;

import java.util.logging.Logger;

public class Cell {
    private static final Logger logger = Logger.getLogger(Cell.class.getName());

    private int impact;

    public Cell(int impact) {
        this.impact = impact;
    }

    public int getImpact() {
        return impact;
    }

    public void setImpact() {
        this.impact = impact;
    }
}
