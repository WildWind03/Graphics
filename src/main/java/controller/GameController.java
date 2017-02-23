package controller;

import model.Field;
import model.Game;
import view.MyJFrame;

import java.io.IOException;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Logger;

public class GameController implements Observer {
    private static final Logger logger = Logger.getLogger(GameController.class.getName());

    private final Game game;
    private final MyJFrame myJFrame;

    public GameController(int fieldWidth, int fieldHeight, int windowWidth, int windowHeight) throws IOException {
        game = new Game(fieldWidth, fieldHeight);
        game.addObserver(this);
        myJFrame = new MyJFrame(windowWidth, windowHeight);
        myJFrame.repaintField(game.getField());
    }

    public void update(Observable o, Object arg) {
        if (arg instanceof Field) {
            myJFrame.repaintField((Field) arg);
        }
    }
}
