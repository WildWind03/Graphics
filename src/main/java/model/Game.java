package model;

import util.GraphicsUtil;

import java.util.Observable;
import java.util.Observer;
import java.util.logging.Logger;

public class Game extends Observable implements Observer {
    private static final Logger logger = Logger.getLogger(Game.class.getName());

    private final Field field;

    public Game(int width, int height) {
        field = new Field(width, height);
    }

    public void update(Observable o, Object arg) {
        if (arg instanceof Field) {
            setChanged();
            notifyObservers(arg);
        }
    }

    public void onClickOnField(int x, int y) {
        if (x >= 0 && y >= 0 && x < field.getWidth() && y < field.getHeight()) {
            field.markCellAlive(x, y);
            setChanged();
            notifyObservers(field);
        }
    }

    public Field getField() {
        return field;
    }
}
