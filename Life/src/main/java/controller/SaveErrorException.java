package controller;

public class SaveErrorException extends FileException {
    public SaveErrorException(String message, Throwable cause) {
        super(message, cause);
    }

    public SaveErrorException(String message) {
        super(message);
    }
}
