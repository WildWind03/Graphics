package ru.nsu.fit.g14201.chirikhin.controller;

import ru.nsu.fit.g14201.chirikhin.model.Field;
import ru.nsu.fit.g14201.chirikhin.model.Game;
import ru.nsu.fit.g14201.chirikhin.support.Point;
import ru.nsu.fit.g14201.chirikhin.util.GraphicsUtil;
import ru.nsu.fit.g14201.chirikhin.view.MyJFrame;

import java.io.*;
import java.util.LinkedList;

public class GameController {
    private static final String CHARSET_NAME = "utf-8";

    private static final int ADDITIONAL_HEIGHT_FOR_FRAME = 1;
    private static final int ADDITIONAL_WIDTH_FOR_FRAME = 1;

    private Game game;

    private final int defaultLineWidth;
    private final int defaultLineLength;

    public GameController(final int fieldWidth, final int fieldHeight, final int lineLength, final int lineWidth) throws IOException {
        this.defaultLineLength = lineLength;
        this.defaultLineWidth = lineWidth;

        int windowWidth = fieldWidth * GraphicsUtil.getHorizontalLength(lineLength) + ADDITIONAL_WIDTH_FOR_FRAME;
        int windowHeight = fieldHeight * (GraphicsUtil.getVerticalPart(lineLength) + lineLength) + GraphicsUtil.getVerticalPart(lineLength) + ADDITIONAL_HEIGHT_FOR_FRAME;

        game = new Game(fieldWidth, fieldHeight);
        MyJFrame myJFrame = new MyJFrame(windowWidth, windowHeight, lineLength, lineWidth);
        myJFrame.repaintField(game.getField());

        setListeners(myJFrame, lineLength);

    }

    private void setListeners(MyJFrame myJFrame, final int lineLength) {
        game.addObserver((o, arg) -> {
            if (arg instanceof Field) {
                myJFrame.repaintField((Field) arg);
            }
        });

        myJFrame.setOnClickListener((x, y, isReplaceMode) -> {
            Point<Integer> point = GraphicsUtil.fromCoordinatesToPositionInField(x, y, lineLength);

            if (isReplaceMode) {
                game.onClickOnFieldReplaceMode(point.getX(), point.getY());
            } else {
                game.onClickOnFieldXORMode(point.getX(), point.getY());
            }
        });

        myJFrame.setOnMoveListener(new TwoIntegerOneBooleanRunnable() {
            Point previousPoint;

            @Override
            public void run(int x, int y, boolean isReplaceMode) {
                Point<Integer> point = GraphicsUtil.fromCoordinatesToPositionInField(x, y, lineLength);

                if (!point.equals(previousPoint)) {
                    previousPoint = point;

                    if (isReplaceMode) {
                        game.onClickOnFieldReplaceMode(point.getX(), point.getY());
                    } else {
                        game.onClickOnFieldXORMode(point.getX(), point.getY());
                    }
                }
            }
        });

        myJFrame.setOnClearButtonListener(game::restart);
        myJFrame.setOnNextButtonListener(game::nextTurn);

        myJFrame.setOnNewGameListener((width, height) -> {
            game = new Game(width, height);

            int windowWidth = width * GraphicsUtil.getHorizontalLength(defaultLineLength) + ADDITIONAL_WIDTH_FOR_FRAME;
            int windowHeight = height * (GraphicsUtil.getVerticalPart(defaultLineLength) + defaultLineLength) + GraphicsUtil.getVerticalPart(defaultLineLength) + ADDITIONAL_HEIGHT_FOR_FRAME;

            myJFrame.updateLineLength(defaultLineLength);
            myJFrame.updateLineWidth(defaultLineWidth);
            myJFrame.updateSize(windowWidth, windowHeight);
            myJFrame.repaintField(game.getField());
            setListeners(myJFrame, lineLength);
        });

        myJFrame.setOnOpenGameListener((fileName) -> {
            try (BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName))) {
                String sizeString = bufferedReader.readLine();

                Point<Integer> size = getPoint(sizeString);
                int width = size.getX();
                int height = size.getY();

                String lineWidthString = bufferedReader.readLine();

                int lineWidth = getValue(lineWidthString);

                String lineLengthString = bufferedReader.readLine();
                int newLineLength = getValue(lineLengthString);

                String countOfLifeCellsString = bufferedReader.readLine();
                int countOfLifeCells = getValue(countOfLifeCellsString);

                LinkedList<Point<Integer>> lifeCells = new LinkedList<>();

                for (int k = 0; k < countOfLifeCells; ++k) {
                    String pointString = bufferedReader.readLine();
                    Point<Integer> point = getPoint(pointString);
                    lifeCells.add(point);
                }

                game = new Game(width, height);

                int windowWidth = width * GraphicsUtil.getHorizontalLength(newLineLength)
                        + ADDITIONAL_WIDTH_FOR_FRAME
                        + lineWidth;

                int windowHeight = height * (GraphicsUtil.getVerticalPart(newLineLength) + newLineLength)
                        + GraphicsUtil.getVerticalPart(newLineLength)
                        + ADDITIONAL_HEIGHT_FOR_FRAME
                        + lineWidth;

                myJFrame.updateSize(windowWidth, windowHeight);
                myJFrame.updateLineWidth(lineWidth);
                myJFrame.updateLineLength(newLineLength);

                game.makeCellAlive(lifeCells);

                myJFrame.repaintField(game.getField());
                setListeners(myJFrame, newLineLength);

            }
        });

        myJFrame.setOnSaveGameListener((filename) -> {
            try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(filename), CHARSET_NAME))) {
                writer.write(game.getField().getWidth()
                        + " "
                + game.getField().getHeight());
                writer.newLine();
                writer.write(String.valueOf(myJFrame.getLineWidth()));
                writer.newLine();
                writer.write(String.valueOf(myJFrame.getLineLength()));
                writer.newLine();

                LinkedList<Point<Integer>> lifePoints = game.getLifePoints();
                writer.write(String.valueOf(lifePoints.size()));
                for (Point<Integer> point : lifePoints) {
                    writer.newLine();
                    writer.write(String.valueOf(point.getX() + " " + String.valueOf(point.getY())));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        myJFrame.setOnConfigurationChangedListener((configuration) -> {
            int newLineLength = configuration.getLineLength();

            int windowWidth = configuration.getWidth() * GraphicsUtil.getHorizontalLength(newLineLength) + ADDITIONAL_WIDTH_FOR_FRAME;
            int windowHeight = configuration.getHeight() * (GraphicsUtil.getVerticalPart(newLineLength) + newLineLength) + GraphicsUtil.getVerticalPart(newLineLength) + ADDITIONAL_HEIGHT_FOR_FRAME;

            myJFrame.updateLineLength(newLineLength);
            myJFrame.updateLineWidth(configuration.getLineWidth());
            myJFrame.setGameMode(configuration.isReplaceMode());
            myJFrame.updateSize(windowWidth, windowHeight);
            setListeners(myJFrame, newLineLength);

            game.applyNewConfiguration(configuration.getModelConfiguration());
        }, () -> game.getConfiguration());
    }

    private int getValue(String string) throws InvalidGameFile {
        String[] fieldSize = string.split(" ");

        if (fieldSize.length < 1) {
            throw new InvalidGameFile("Invalid width or height");
        }

        int value;

        try {
            value = Integer.parseInt(fieldSize[0]);
        } catch (NumberFormatException e) {
            throw new InvalidGameFile("Incorrect width and height");
        }

        if (fieldSize.length >= 2) {
            String thirdToken = fieldSize[1];
            if (!thirdToken.substring(0, 2).equals("//")) {
                throw new InvalidGameFile("Invalid file. Too many arguments");
            }
        }

        return value;
    }

    private Point<Integer> getPoint(String string) throws InvalidGameFile {
        String[] fieldSize = string.split(" ");

        if (fieldSize.length < 2) {
            throw new InvalidGameFile("Invalid width or height");
        }

        int width, height;
        try {
            width = Integer.parseInt(fieldSize[0]);
            height = Integer.parseInt(fieldSize[1]);
        } catch (NumberFormatException e) {
            throw new InvalidGameFile("Incorrect width and height");
        }

        if (fieldSize.length >= 3) {
            String thirdToken = fieldSize[2];
            if (!thirdToken.substring(0, 2).equals("//")) {
                throw new InvalidGameFile("Invalid file. Too many arguments");
            }
        }

        return new Point<>(width, height);
    }
}
