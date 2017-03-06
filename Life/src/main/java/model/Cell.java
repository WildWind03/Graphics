package model;

public class Cell {

    private float impact;
    private boolean isAlive;

    Cell(float impact) {
        this.impact = impact;
    }

    public float getImpact() {
        return impact;
    }

    void changeState(boolean isAlive) {
        this.isAlive = isAlive;
    }

    void addImpact(float impact) {
        this.impact+=impact;
    }

    void subtractImpact(float impact) {
        this.impact-=impact;
    }

    public boolean isAlive() {
        return isAlive;
    }

    void setImpact(float newImpact) {
        this.impact = newImpact;
    }

}
