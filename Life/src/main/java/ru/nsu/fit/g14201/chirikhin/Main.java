package ru.nsu.fit.g14201.chirikhin;

import ru.nsu.fit.g14201.chirikhin.controller.GameController;

import java.io.IOException;

public class Main {
    private static final int lineLength = 30;
    private static final int lineWidth = 1;
    private static final int FIELD_SIZE = 5;

    public static void main(String[] args) throws IOException {
        new GameController(FIELD_SIZE, FIELD_SIZE, lineLength, lineWidth);
    }
}
