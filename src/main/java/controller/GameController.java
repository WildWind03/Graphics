package controller;

import model.LifeView;
import view.MyJFrame;

import java.io.IOException;
import java.util.logging.Logger;

public class GameController {
    private static final Logger logger = Logger.getLogger(GameController.class.getName());

    public GameController() throws IOException {
        LifeView lifeView = new MyJFrame(600, 800);
    }
}
