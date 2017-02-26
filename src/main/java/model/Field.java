package model;

import java.util.Observable;
import java.util.logging.Logger;

public class Field extends Observable {
    private static final Logger logger = Logger.getLogger(Field.class.getName());

    private final int width;
    private final int height;

    private final double FIRST_IMPACT = 1;
    private final double SECOND_IMPACT = 0.3;

    private final double DEFAULT_IMPACT = 0;

    private final Cell field[][];

    public Field(int width, int height) {
        this.width = width;
        this.height = height;

        field = new Cell[width][height];

        for (int k = 0; k < width; ++k) {
            for (int i = 0; i < height; ++i) {
                field[k][i] = new Cell(DEFAULT_IMPACT);
            }
        }

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

    public void addImpact(int x, int y, double count) {
        if (x >= 0 && x < getWidth() && y >= 0 && y < getHeight()) {
            field[x][y].addImpact(count);
        }
    }

    public void markCellAlive(int x, int y) {
        field[x][y].changeState(true);

        addImpact(x + 1, y, FIRST_IMPACT);
        addImpact(x - 1, y, FIRST_IMPACT);

        addImpact(x, y - 1, FIRST_IMPACT);
        addImpact(x, y + 1, FIRST_IMPACT);

        addImpact(x, y - 2, SECOND_IMPACT);
        addImpact(x, y + 2, SECOND_IMPACT);

        if (0 == y % 2) {
            addImpact(x - 1, y - 1, FIRST_IMPACT);
            addImpact(x - 1, y + 1, FIRST_IMPACT);

            addImpact(x + 1, y - 1, SECOND_IMPACT);
            addImpact(x - 2, y - 1, SECOND_IMPACT);

            addImpact(x + 1, y + 1, SECOND_IMPACT);
            addImpact(x - 2, y + 1, SECOND_IMPACT);
        } else {
            addImpact(x + 1, y - 1, FIRST_IMPACT);
            addImpact(x + 1, y + 1, FIRST_IMPACT);

            addImpact(x - 1, y - 1, SECOND_IMPACT);
            addImpact(x + 2, y - 1, SECOND_IMPACT);

            addImpact(x - 1, y + 1, SECOND_IMPACT);
            addImpact(x + 2, y + 1, SECOND_IMPACT);
        }
    }
}
