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
            notifyObservers(arg);
        }

        if (arg instanceof GraphicsUtil.Point) {
            field.getCells()[((GraphicsUtil.Point) arg).getX()][((GraphicsUtil.Point) arg).getY()].setImpact();
        }
    }

    public Field getField() {
        return field;
    }
}
