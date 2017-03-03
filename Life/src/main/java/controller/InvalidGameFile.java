package controller;

import java.util.logging.Logger;

public class InvalidGameFile extends FileException {
    private static final Logger logger = Logger.getLogger(InvalidGameFile.class.getName());

    public InvalidGameFile(String message) {
        super(message);
    }

    public InvalidGameFile(String message, Throwable cause) {
        super(message, cause);
    }
}
