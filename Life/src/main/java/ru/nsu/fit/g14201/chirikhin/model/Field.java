package ru.nsu.fit.g14201.chirikhin.model;

import ru.nsu.fit.g14201.chirikhin.support.Point;

import java.util.LinkedList;
import java.util.List;

public class Field {
    private int width;
    private int height;

    private static final float DEFAULT_IMPACT = 0;

    private Cell cells[][];

    public Field(int width, int height) {
        this.width = width;
        this.height = height;

        cells = new Cell[height][width];

        for (int k = 0; k < height; ++k) {
            for (int i = 0; i < width; ++i) {
                cells[k][i] = new Cell(DEFAULT_IMPACT);
            }
        }
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void clear() {
        makeAllCellsDead();
        resetCellsImpacts();
    }

    public void makeAllCellsDead() {
        for (int k = 0; k < height; ++k) {
            for (int i = 0; i < width; ++i) {
                cells[k][i].changeState(false);
            }
        }
    }

    public void resetCellsImpacts() {
        for (int k = 0; k < height; ++k) {
            for (int i = 0; i < width; ++i) {
                cells[k][i].setImpact(0);
            }
        }
    }

    public void changeState(int x, int y, boolean newState) {
        cells[y][x].changeState(newState);
    }

    public void setImpact(int x, int y, float newImpact) {
        cells[y][x].setImpact(newImpact);
    }

    public List<Cell> getFirstNeighbours(int x, int y) {
        LinkedList<Cell> neighbours = new LinkedList<Cell>();

        addCellToNeighbourList(x + 1, y, neighbours);
        addCellToNeighbourList(x - 1, y, neighbours);
        addCellToNeighbourList(x, y - 1, neighbours);
        addCellToNeighbourList(x, y + 1, neighbours);

        if (0 == y % 2) {
            addCellToNeighbourList(x - 1, y - 1, neighbours);
            addCellToNeighbourList(x - 1, y + 1, neighbours);
        } else {
            addCellToNeighbourList(x + 1, y - 1, neighbours);
            addCellToNeighbourList(x + 1, y + 1, neighbours);
        }

        return neighbours;
    }

    public List<Cell> getSecondNeighbours(int x, int y) {
        LinkedList<Cell> neighbours = new LinkedList<Cell>();

        addCellToNeighbourList(x, y - 2, neighbours);
        addCellToNeighbourList(x, y + 2, neighbours);

        if (0 == y % 2) {
            addCellToNeighbourList(x + 1, y - 1, neighbours);
            addCellToNeighbourList(x - 2, y - 1, neighbours);
            addCellToNeighbourList(x + 1, y + 1, neighbours);
            addCellToNeighbourList(x - 2, y + 1, neighbours);
        } else {
            addCellToNeighbourList(x - 1, y - 1, neighbours);
            addCellToNeighbourList(x + 2, y - 1, neighbours);
            addCellToNeighbourList(x - 1, y + 1, neighbours);
            addCellToNeighbourList(x + 2, y + 1, neighbours);
        }

        return neighbours;
    }

    private void addCellToNeighbourList(int x, int y, List<Cell> neighbours) {
        int width = (0 == y % 2) ? getWidth() : getWidth() - 1;

        if (x >= 0 && y >= 0 && x < width && y < getHeight()) {
            neighbours.add(cells[y][x]);
        }
    }

    public boolean isAlive(int x, int y) {
        return cells[y][x].isAlive();
    }

    public boolean isDead(int x, int y) {
        return !isAlive(x, y);
    }

    public float getImpact(int x, int y) {
        return cells[y][x].getImpact();
    }

    public LinkedList<Point<Integer>> getLifeCells() {
        LinkedList<Point<Integer>> lifePoints = new LinkedList<>();
        for (int i = 0; i < height; ++i) {
            int width = (0 == i % 2) ? getWidth() : getWidth() - 1;
            for (int k = 0; k < width; ++k) {
                if (cells[i][k].isAlive()) {
                    lifePoints.add(new Point<>(k, i));
                }
            }
        }

        return lifePoints;
    }

    public void changeSize(int newWidth, int newHeight) {
        Cell[][] newCells = new Cell[newHeight][newWidth];

        for (int k = 0; k < newHeight; ++k) {
            for (int i = 0; i < newWidth; ++i) {
                if (k < getHeight() && i < getWidth()) {
                    newCells[k][i] = cells[k][i];
                } else {
                    newCells[k][i] = new Cell(DEFAULT_IMPACT);
                }
            }
        }

        cells = newCells;

        this.width = newWidth;
        this.height = newHeight;
    }
}
