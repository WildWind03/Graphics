package controller;

import model.Field;
import model.Game;
import util.GraphicsUtil;
import view.MyJFrame;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Logger;

public class GameController implements Observer {
    private static final Logger logger = Logger.getLogger(GameController.class.getName());

    private final int lineLength = 60;

    private final Game game;
    private final MyJFrame myJFrame;

    public GameController(int fieldWidth, int fieldHeight, int windowWidth, int windowHeight) throws IOException {
        game = new Game(fieldWidth, fieldHeight);
        game.addObserver(this);
        myJFrame = new MyJFrame(windowWidth, windowHeight, lineLength);
        myJFrame.repaintField(game.getField());

        myJFrame.addOnClickListener(new MouseListener() {
            public void mouseClicked(MouseEvent e) {
                GraphicsUtil.Point point = GraphicsUtil.fromCoordinatesToPositionInField(e.getX(), e.getY(), lineLength);

                game.onClickOnField(point.getX(), point.getY());
            }

            public void mousePressed(MouseEvent e) {

            }

            public void mouseReleased(MouseEvent e) {

            }

            public void mouseEntered(MouseEvent e) {

            }

            public void mouseExited(MouseEvent e) {

            }
        });

    }

    public void update(Observable o, Object arg) {
        if (arg instanceof Field) {
            myJFrame.repaintField((Field) arg);
        }
    }
}
