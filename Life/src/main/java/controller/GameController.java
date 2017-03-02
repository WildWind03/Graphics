package controller;

import model.Field;
import model.Game;
import util.GraphicsUtil;
import view.MyJFrame;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Logger;

public class GameController {
    private static final Logger logger = Logger.getLogger(GameController.class.getName());

    private final int VERTICAL_MARGIN = 1;
    private final int HORIZONTAL_MARGIN = 1;

    private final Game game;
    private final MyJFrame myJFrame;

    public GameController(final int fieldWidth, final int fieldHeight, final int lineLength) throws IOException {
        int windowWidth = fieldWidth * GraphicsUtil.getHorizontalLength(lineLength) + HORIZONTAL_MARGIN;
        int windowHeight = fieldHeight * (GraphicsUtil.getVerticalPart(lineLength) + lineLength) + GraphicsUtil.getVerticalPart(lineLength) + VERTICAL_MARGIN;
        game = new Game(fieldWidth, fieldHeight);
        game.addObserver(new Observer() {
            public void update(Observable o, Object arg) {
                if (arg instanceof Field) {
                    myJFrame.repaintField((Field) arg);
                }
            }
        });

        myJFrame = new MyJFrame(windowWidth, windowHeight, lineLength);
        myJFrame.repaintField(game.getField());

        myJFrame.addOnClickListener((x, y) -> {
            GraphicsUtil.Point point = GraphicsUtil.fromCoordinatesToPositionInField(x, y, lineLength);
            game.onClickOnField(point.getX(), point.getY());
        });

        myJFrame.addOnMoveListener(new MyRunnable() {
            GraphicsUtil.Point previousPoint;

            @Override
            public void run(int x, int y) {
                GraphicsUtil.Point point = GraphicsUtil.fromCoordinatesToPositionInField(x, y, lineLength);

                if (!point.equals(previousPoint)) {
                    previousPoint = point;
                    game.onClickOnField(point.getX(), point.getY());
                }
            }
        });

        myJFrame.addOnClearButtonListener(game::restart);
        myJFrame.addOnNextButtonListener(game::nextTurn);

        myJFrame.addOnNewGameListener((width, height) -> {
            //game = new Game(width, height);
        });

        myJFrame.addOnOpenGameListener(() -> {

        });

        myJFrame.addOnSaveGameListener(() -> {

        });


    }
}
