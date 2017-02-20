package model;

import java.util.logging.Logger;

public class Field implements IField {
    private static final Logger logger = Logger.getLogger(Field.class.getName());
    private final int width;
    private final int height;

    private final ICell field[][];

    public Field(int width, int height) {
        this.width = width;
        this.height = height;

        field = new ICell[width][height];
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public ICell[][] getCells() {
        return field;
    }
}
