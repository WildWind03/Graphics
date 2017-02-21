package model;

import java.util.Observable;
import java.util.logging.Logger;

public class Field extends Observable {
    private static final Logger logger = Logger.getLogger(Field.class.getName());

    private final int width;
    private final int height;

    private final Cell field[][];

    public Field(int width, int height) {
        this.width = width;
        this.height = height;

        field = new Cell[width][height];

        notifyObservers(this);

    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Cell[][] getCells() {
        return field;
    }
}
