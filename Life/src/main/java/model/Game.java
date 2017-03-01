package model;

import java.util.Observable;
import java.util.Observer;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

public class Game extends Observable {
    private static final Logger logger = Logger.getLogger(Game.class.getName());

    private static double birthBegin = 2.3;
    private static double birthEnd = 2.9;
    private static double lifeBegin = 2.0;
    private static double lifeEnd = 3.3;

    private final double FIRST_IMPACT = 1;
    private final double SECOND_IMPACT = 0.3;

    private final Field firstField;
    private final Field secondField;

    private int turn = 0;

    public Game(int width, int height) {
        firstField = new Field(width, height);
        secondField = new Field(width, height);
    }

    public void onClickOnField(int x, int y) {
        Field field = getField();

        int maxWidth = (0 == y % 2) ? field.getWidth() : field.getWidth() - 1;

        if (x >= 0 && y >= 0 && x < maxWidth && y < field.getHeight()) {
            makeCellAlive(x, y);
            setChanged();
            notifyObservers(field);
        }
    }

    public void nextTurn() {
        Field currentField = getField();
        Field nextField = (0 != turn % 2) ? firstField : secondField;

        turn++;
        nextField.makeAllCellsDead();

        for (int k = 0; k < currentField.getHeight(); ++k) {
            int width = (0 == k % 2) ? currentField.getWidth() : currentField.getWidth() - 1;

            for (int i = 0; i < width; ++i) {
                int countOfAliveFirstNeighbours = (int) currentField
                        .getFirstNeighbours(i, k)
                        .stream()
                        .filter(Cell::isAlive)
                        .count();

                int countOfAliveSecondNeighbours = (int) currentField
                        .getSecondNeighbours(i, k)
                        .stream()
                        .filter(Cell::isAlive)
                        .count();

                double newImpact = countOfAliveFirstNeighbours * FIRST_IMPACT + countOfAliveSecondNeighbours * SECOND_IMPACT;

                nextField.setImpact(i, k, newImpact);
                if (currentField.isAlive(i, k)) {
                    if (newImpact >= lifeBegin && newImpact <= lifeEnd) {
                        nextField.changeState(i, k, true);
                    } else {
                        nextField.changeState(i, k, false);
                    }
                } else {
                    if (newImpact >= birthBegin && newImpact <= birthEnd) {
                        nextField.changeState(i, k, true);
                    } else {
                        nextField.changeState(i, k, false);
                    }
                }
            }
        }

        setChanged();
        notifyObservers(getField());
    }

    public Field getField() {
        return (0 == turn % 2) ? firstField : secondField;
    }

    private void makeCellAlive(int x, int y) {
        if (getField().isAlive(x, y)) {
            return;
        }

        getField().changeState(x, y, true);

        getField()
                .getFirstNeighbours(x, y)
                .forEach(cell -> cell.addImpact(FIRST_IMPACT));

        getField()
                .getSecondNeighbours(x, y)
                .forEach(cell -> cell.addImpact(SECOND_IMPACT));
    }

    public void restart() {
        turn = 0;
        firstField.clear();
        secondField.clear();

        setChanged();
        notifyObservers(getField());
    }
}
