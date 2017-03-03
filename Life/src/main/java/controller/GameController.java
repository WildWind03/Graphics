package controller;

import model.Field;
import model.Game;
import support.Point;
import util.GraphicsUtil;
import view.MyJFrame;

import java.io.*;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.logging.Logger;

public class GameController {
    private static final Logger logger = Logger.getLogger(GameController.class.getName());

    private final int VERTICAL_MARGIN = 1;
    private final int HORIZONTAL_MARGIN = 1;

    private Game game;
    private final MyJFrame myJFrame;
    private final int lineLength;

    public GameController(final int fieldWidth, final int fieldHeight, final int lineLength) throws IOException {
        this.lineLength = lineLength;

        int windowWidth = fieldWidth * GraphicsUtil.getHorizontalLength(lineLength) + HORIZONTAL_MARGIN;
        int windowHeight = fieldHeight * (GraphicsUtil.getVerticalPart(lineLength) + lineLength) + GraphicsUtil.getVerticalPart(lineLength) + VERTICAL_MARGIN;

        game = new Game(fieldWidth, fieldHeight);
        myJFrame = new MyJFrame(windowWidth, windowHeight, lineLength);
        myJFrame.repaintField(game.getField());

        setListeners(myJFrame);

    }

    private void setListeners(MyJFrame myJFrame) {
        game.addObserver((o, arg) -> {
            if (arg instanceof Field) {
                myJFrame.repaintField((Field) arg);
            }
        });

        myJFrame.setOnClickListener((x, y) -> {
            GraphicsUtil.Point point = GraphicsUtil.fromCoordinatesToPositionInField(x, y, lineLength);
            game.onClickOnField(point.getX(), point.getY());
        });

        myJFrame.setOnMoveListener(new TwoIntegerRunnable() {
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

        myJFrame.setOnClearButtonListener(game::restart);
        myJFrame.setOnNextButtonListener(game::nextTurn);

        myJFrame.setOnNewGameListener((width, height) -> {
            game = new Game(width, height);

            int windowWidth = width * GraphicsUtil.getHorizontalLength(lineLength) + HORIZONTAL_MARGIN;
            int windowHeight = height * (GraphicsUtil.getVerticalPart(lineLength) + lineLength) + GraphicsUtil.getVerticalPart(lineLength) + VERTICAL_MARGIN;

            myJFrame.updateSize(windowWidth, windowHeight);
            myJFrame.repaintField(game.getField());
            setListeners(myJFrame);
        });

        myJFrame.setOnOpenGameListener((fileName) -> {
            try (Scanner scanner = new Scanner(Paths.get(fileName))) {
                int width = scanner.nextInt();
                int height = scanner.nextInt();

                int lineWidth = scanner.nextInt();

                int lineLength = scanner.nextInt();

                int countOfLifeCells = scanner.nextInt();

                LinkedList<Point<Integer>> lifeCells = new LinkedList<>();

                for (int i = 0; i < countOfLifeCells; ++i) {
                    lifeCells.add(new Point<>(scanner.nextInt(), scanner.nextInt()));
                }

                game = new Game(width, height);

                int windowWidth = width * GraphicsUtil.getHorizontalLength(lineLength)
                        + HORIZONTAL_MARGIN
                        + lineWidth;

                int windowHeight = height * (GraphicsUtil.getVerticalPart(lineLength) + lineLength)
                        + GraphicsUtil.getVerticalPart(lineLength)
                        + VERTICAL_MARGIN
                        + lineWidth;

                myJFrame.updateSize(windowWidth, windowHeight);
                myJFrame.updateLineWidth(lineWidth);
                myJFrame.updateLineLength(lineLength);

                game.makeCellAlive(lifeCells);

                myJFrame.repaintField(game.getField());
                setListeners(myJFrame);

            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        myJFrame.setOnSaveGameListener(() -> {

        });
    }
}
