package ru.nsu.fit.g14201.chirikhin.controller;

abstract public class FileException extends Exception {
    public FileException(String message, Throwable cause) {
        super(message, cause);
    }

    public FileException(String message) {
        super(message);
    }
}
