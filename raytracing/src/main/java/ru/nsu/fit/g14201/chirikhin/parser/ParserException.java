package ru.nsu.fit.g14201.chirikhin.parser;

public class ParserException extends Exception {
    public ParserException(String string) {
        super(string);
    }

    public ParserException(String message, Throwable cause) {
        super(message, cause);
    }
}
