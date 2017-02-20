package controller;

import model.Game;
import view.MyJFrame;

import java.io.IOException;
import java.util.logging.Logger;

public class GameController {
    private static final Logger logger = Logger.getLogger(GameController.class.getName());

    public GameController() throws IOException {
        Game game = new Game(10, 10);
        MyJFrame myJFrame = new MyJFrame(600, 800);
        game.addObserver(myJFrame);
    }
}
