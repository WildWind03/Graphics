package ru.nsu.fit.g14201.chirikhin.view;

import ru.nsu.fit.g14201.chirikhin.model.Triangle;

import java.awt.*;

public class TriangleDrawer implements Drawer {
    private final Triangle triangle;
    private final Color color;

    public TriangleDrawer(Triangle triangle, Color color) {
        this.triangle = triangle;
        this.color = color;
    }

    @Override
    public void draw(ShapeView shapeView) {
        shapeView.drawLine(triangle.getPoint1(), triangle.getPoint2(), color);
        shapeView.drawLine(triangle.getPoint2(), triangle.getPoint3(), color);
        shapeView.drawLine(triangle.getPoint3(), triangle.getPoint1(), color);
    }
}
