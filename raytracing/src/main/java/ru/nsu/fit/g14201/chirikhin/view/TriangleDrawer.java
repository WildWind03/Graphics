package ru.nsu.fit.g14201.chirikhin.view;

import ru.nsu.fit.g14201.chirikhin.model.Triangle;

import java.awt.*;

public class TriangleDrawer extends Drawer {
    private final Triangle triangle;
    private final Color color;

    public TriangleDrawer(Triangle triangle, Color color) {
        this.triangle = triangle;
        this.color = color;
    }

    @Override
    public void draw(ShapeView shapeView) {
        shapeView.drawLine(triangle.getPoint1().getX() / getRate(),
                triangle.getPoint1().getY() / getRate(),
                triangle.getPoint1().getZ() / getRate(),
                triangle.getPoint2().getX() / getRate(),
                triangle.getPoint2().getY() / getRate(),
                triangle.getPoint2().getZ() / getRate(),
                color);

        shapeView.drawLine(triangle.getPoint2(), triangle.getPoint3(), color);
        shapeView.drawLine(triangle.getPoint3(), triangle.getPoint1(), color);
    }

    public Triangle getTriangle() {
        return triangle;
    }
}
