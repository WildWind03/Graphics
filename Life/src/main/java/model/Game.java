package model;

import support.Point;

import java.util.LinkedList;
import java.util.Observable;
import java.util.logging.Logger;

public class Game extends Observable {
    private static final Logger logger = Logger.getLogger(Game.class.getName());

    private double birthBegin = 2.3;
    private double birthEnd = 2.9;
    private double lifeBegin = 2.0;
    private double lifeEnd = 3.3;

    private double firstImpact = 1;
    private double secondImpact = 0.3F;

    private final Field firstField;
    private final Field secondField;

    private int turn = 0;

    public Game(int width, int height) {
        firstField = new Field(width, height);
        secondField = new Field(width, height);
    }

    public void onClickOnFieldReplaceMode(int x, int y) {
        markCellAlive(x, y);
    }

    private void markCellAlive(int x, int y) {
        Field field = getField();

        int maxWidth = (0 == y % 2) ? field.getWidth() : field.getWidth() - 1;

        if (x >= 0 && y >= 0 && x < maxWidth && y < field.getHeight()) {
            makeCellAlive(x, y);
            setChanged();
            notifyObservers(field);
        }
    }

    private void markCellDead(int x, int y) {
        Field field = getField();

        int maxWidth = (0 == y % 2) ? field.getWidth() : field.getWidth() - 1;

        if (x >= 0 && y >= 0 && x < maxWidth && y < field.getHeight()) {
            makeCellDead(x, y);
            setChanged();
            notifyObservers(field);
        }
    }

    public void onClickOnFieldXORMode(int x, int y) {
        Field field = getField();

        int maxWidth = (0 == y % 2) ? field.getWidth() : field.getWidth() - 1;

        if (x >= 0 && y >= 0 && x < maxWidth && y < field.getHeight()) {
            if (getField().isAlive(x, y)) {
                markCellDead(x, y);
            } else {
                markCellAlive(x, y);
            }
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

                double newImpact = countOfAliveFirstNeighbours * firstImpact + countOfAliveSecondNeighbours * secondImpact;

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
                .forEach(cell -> cell.addImpact(firstImpact));

        getField()
                .getSecondNeighbours(x, y)
                .forEach(cell -> cell.addImpact(secondImpact));
    }

    private void makeCellDead(int x, int y) {
        if (getField().isDead(x, y)) {
            return;
        }

        getField().changeState(x, y, false);

        getField()
                .getFirstNeighbours(x, y)
                .forEach(cell -> cell.subtractImpact(firstImpact));

        getField()
                .getSecondNeighbours(x, y)
                .forEach(cell -> cell.subtractImpact(secondImpact));
    }

    public void makeCellAlive(LinkedList<Point<Integer>> points) {
        points.forEach(point -> {
            makeCellAlive(point.getX(), point.getY());
        });
    }

    public LinkedList<Point<Integer>> getLifePoints() {
        return getField().getLifeCells();
    }

    public void restart() {
        turn = 0;
        firstField.clear();
        secondField.clear();

        setChanged();
        notifyObservers(getField());
    }

    public ModelConfiguration getConfiguration() {
        return new ModelConfiguration(lifeBegin, lifeEnd, birthBegin, birthEnd, firstImpact, secondImpact, getField().getWidth(), getField().getHeight());
    }

    public void applyNewConfiguration(ModelConfiguration configuration) {
        this.birthBegin = configuration.getBirthBegin();
        this.birthEnd = configuration.getBirthEnd();
        this.firstImpact = configuration.getFirstImpact();
        this.secondImpact = configuration.getSecondImpact();
        this.lifeBegin = configuration.getLiveBegin();
        this.lifeEnd = configuration.getLiveEnd();

        firstField.changeSize(configuration.getWidth(), configuration.getHeight());
        secondField.changeSize(configuration.getWidth(), configuration.getHeight());

        setChanged();
        notifyObservers(getField());
    }
}
