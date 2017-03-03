package controller;

import java.util.logging.Logger;

abstract public class FileException extends Exception {
    private static final Logger logger = Logger.getLogger(FileException.class.getName());

    public FileException(String message, Throwable cause) {
        super(message, cause);
    }

    public FileException(String message) {
        super(message);
    }
}
