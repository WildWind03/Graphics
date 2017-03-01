package model;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

public class Field {
    private static final Logger logger = Logger.getLogger(Field.class.getName());

    private final int width;
    private final int height;

    private final double DEFAULT_IMPACT = 0;

    private final Cell cells[][];

    public Field(int width, int height) {
        this.width = width;
        this.height = height;

        cells = new Cell[width][height];

        for (int k = 0; k < width; ++k) {
            for (int i = 0; i < height; ++i) {
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

    public void addImpact(int x, int y, double count) {
        if (x >= 0 && x < getWidth() && y >= 0 && y < getHeight()) {
            cells[x][y].addImpact(count);
        }
    }

    public void clear() {
        makeAllCellsDead();
        resetCellsImpacts();
    }

    public void makeAllCellsDead() {
        for (int k = 0; k < width; ++k) {
            for (int i = 0; i < height; ++i) {
                cells[k][i].changeState(false);
            }
        }
    }

    public void resetCellsImpacts() {
        for (int k = 0; k < width; ++k) {
            for (int i = 0; i < height; ++i) {
                cells[k][i].setImpact(0);
            }
        }
    }

    public void changeState(int x, int y, boolean newState) {
        cells[x][y].changeState(newState);
    }

    public void setImpact(int x, int y, double newImpact) {
        cells[x][y].setImpact(newImpact);
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
        if (x >= 0 && y >= 0 && x < getWidth() && y < getHeight()) {
            neighbours.add(cells[x][y]);
        }
    }

    public boolean isAlive(int x, int y) {
        return cells[x][y].isAlive();
    }

    public double getImpact(int x, int y) {
        return cells[x][y].getImpact();
    }
}
