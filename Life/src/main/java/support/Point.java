package support;

import java.util.logging.Logger;

public class Point<T> {
    private static final Logger logger = Logger.getLogger(Point.class.getName());

    private final T x;
    private final T y;

    public Point(T x, T y) {
        this.x = x;
        this.y = y;
    }

    public T getX() {
        return x;
    }

    public T getY() {
        return y;
    }
}
