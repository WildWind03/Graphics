package ru.nsu.fit.g14201.chirikhin.model;

import ru.nsu.fit.g14201.chirikhin.support.Point;

import java.util.LinkedList;
import java.util.Observable;

public class Game extends Observable {
    private float birthBegin = 2.3f;
    private float birthEnd = 2.9f;
    private float lifeBegin = 2.0f;
    private float lifeEnd = 3.3f;

    private float firstImpact = 1f;
    private float secondImpact = 0.3f;

    private final Field field;

    public Game(int width, int height) {
        field = new Field(width, height);
    }

    public void onClickOnFieldReplaceMode(int x, int y) {
        markCellAlive(x, y);
    }

    private void markCellAlive(int x, int y) {
        int maxWidth = (0 == y % 2) ? field.getWidth() : field.getWidth() - 1;

        if (x >= 0 && y >= 0 && x < maxWidth && y < field.getHeight()) {
            makeCellAlive(x, y);
            setChanged();
            notifyObservers(field);
        }
    }

    private void markCellDead(int x, int y) {

        int maxWidth = (0 == y % 2) ? field.getWidth() : field.getWidth() - 1;

        if (x >= 0 && y >= 0 && x < maxWidth && y < field.getHeight()) {
            makeCellDead(x, y);
            setChanged();
            notifyObservers(field);
        }
    }

    public void onClickOnFieldXORMode(int x, int y) {

        int maxWidth = (0 == y % 2) ? field.getWidth() : field.getWidth() - 1;

        if (x >= 0 && y >= 0 && x < maxWidth && y < field.getHeight()) {
            if (field.isAlive(x, y)) {
                markCellDead(x, y);
            } else {
                markCellAlive(x, y);
            }
        }
    }

    public void nextTurn() {
        for (int k = 0; k < field.getHeight(); ++k) {
            int width = (0 == k % 2) ? field.getWidth() : field.getWidth() - 1;

            for (int i = 0; i < width; ++i) {
                float currentImpact = field.getImpact(i, k);

                if (field.isAlive(i, k)) {
                    if (currentImpact < lifeBegin || currentImpact  > lifeEnd) {
                        field.changeState(i, k, false);
                    }
                } else {
                    if (currentImpact >= birthBegin && currentImpact <= birthEnd) {
                        field.changeState(i, k, true);

                    }
                }
            }
        }

        for (int k = 0; k < field.getHeight(); ++k) {
            int width = (0 == k % 2) ? field.getWidth() : field.getWidth() - 1;

            for (int i = 0; i < width; ++i) {
                int countOfAliveFirstNeighbours = (int) field
                        .getFirstNeighbours(i, k)
                        .stream()
                        .filter(Cell::isAlive)
                        .count();

                int countOfAliveSecondNeighbours = (int) field
                        .getSecondNeighbours(i, k)
                        .stream()
                        .filter(Cell::isAlive)
                        .count();

                float newImpact = countOfAliveFirstNeighbours * firstImpact + countOfAliveSecondNeighbours * secondImpact;

                field.setImpact(i, k, newImpact);
            }
        }

        setChanged();
        notifyObservers(field);
    }

    private void makeCellAlive(int x, int y) {
        if (field.isAlive(x, y)) {
            return;
        }

        field.changeState(x, y, true);

        field
                .getFirstNeighbours(x, y)
                .forEach(cell -> cell.addImpact(firstImpact));

        field
                .getSecondNeighbours(x, y)
                .forEach(cell -> cell.addImpact(secondImpact));
    }

    private void makeCellDead(int x, int y) {
        if (field.isDead(x, y)) {
            return;
        }

        field.changeState(x, y, false);

        field
                .getFirstNeighbours(x, y)
                .forEach(cell -> cell.subtractImpact(firstImpact));

        field
                .getSecondNeighbours(x, y)
                .forEach(cell -> cell.subtractImpact(secondImpact));
    }

    public void makeCellAlive(LinkedList<Point<Integer>> points) {
        points.forEach(point -> {
            makeCellAlive(point.getX(), point.getY());
        });
    }

    public LinkedList<Point<Integer>> getLifePoints() {
        return field.getLifeCells();
    }

    public void restart() {
        field.clear();

        setChanged();
        notifyObservers(field);
    }

    public ModelConfiguration getConfiguration() {
        return new ModelConfiguration(lifeBegin, lifeEnd, birthBegin, birthEnd, firstImpact, secondImpact, field.getWidth(), field.getHeight());
    }

    public void applyNewConfiguration(ModelConfiguration configuration) {
        this.birthBegin = configuration.getBirthBegin();
        this.birthEnd = configuration.getBirthEnd();
        this.firstImpact = configuration.getFirstImpact();
        this.secondImpact = configuration.getSecondImpact();
        this.lifeBegin = configuration.getLiveBegin();
        this.lifeEnd = configuration.getLiveEnd();

        field.changeSize(configuration.getWidth(), configuration.getHeight());

        setChanged();
        notifyObservers(field);
    }

    public Field getField() {
        return field;
    }
}
