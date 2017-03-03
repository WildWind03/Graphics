package controller;

import java.util.logging.Logger;

public class InvalidGameFile extends Exception {
    private static final Logger logger = Logger.getLogger(InvalidGameFile.class.getName());

    public InvalidGameFile(String message) {
        super(message);
    }
}
