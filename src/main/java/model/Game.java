package model;

import java.util.Observable;
import java.util.logging.Logger;

public class Game extends Observable {
    private static final Logger logger = Logger.getLogger(Game.class.getName());

    private final Field field;

    public Game(int width, int height) {
        field = new Field(width, height);
    }
}
