import controller.GameController;

import java.io.IOException;
import java.util.logging.Logger;

public class Main {
    private static final Logger logger = Logger.getLogger(Main.class.getName());

    private static final int lineLength = 20;

    public static void main(String[] args) throws IOException {
        GameController gameController = new GameController(15, 15, lineLength);
    }
}
