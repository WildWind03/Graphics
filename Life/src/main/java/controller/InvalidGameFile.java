package controller;

class InvalidGameFile extends FileException {
    public InvalidGameFile(String message) {
        super(message);
    }
}
