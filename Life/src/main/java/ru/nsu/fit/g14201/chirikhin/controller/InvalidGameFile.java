package ru.nsu.fit.g14201.chirikhin.controller;

class InvalidGameFile extends FileException {
    public InvalidGameFile(String message) {
        super(message);
    }
}
