package ru.nsu.fit.g14201.chirikhin.view;

import ru.nsu.fit.g14201.chirikhin.model.Quadrangle;

import java.awt.*;

public class QuadrangleDrawer implements Drawer {
    private final Quadrangle quadrangle;
    private final Color color;

    public QuadrangleDrawer(Quadrangle quadrangle, Color color) {
        this.quadrangle = quadrangle;
        this.color = color;
    }

    @Override
    public void draw(ShapeView shapeView) {
        shapeView.drawLine(quadrangle.getPoint1(), quadrangle.getPoint2(), color);
        shapeView.drawLine(quadrangle.getPoint2(), quadrangle.getPoint3(), color);
        shapeView.drawLine(quadrangle.getPoint3(), quadrangle.getPoint4(), color);
        shapeView.drawLine(quadrangle.getPoint4(), quadrangle.getPoint1(), color);
    }
}
