package chirikhin.universal_parser;

public class ParserException extends Exception {
    public ParserException(String message, Throwable cause) {
        super(message, cause);
    }

    public ParserException(String message) {
        super(message);
    }
}
