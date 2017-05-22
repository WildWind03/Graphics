package ru.nsu.fit.g14201.chirikhin.view;

import ru.nsu.fit.g14201.chirikhin.model.Quadrangle;

import java.awt.*;

public class QuadrangleDrawer extends Drawer {
    private final Quadrangle quadrangle;
    private final Color color;

    public QuadrangleDrawer(Quadrangle quadrangle, Color color) {
        this.quadrangle = quadrangle;
        this.color = color;
    }

    @Override
    public void draw(ShapeView shapeView) {
        shapeView.drawLine(quadrangle.getPoint1().getX() / getRate(),
                quadrangle.getPoint1().getY() / getRate(),
                quadrangle.getPoint1().getZ() / getRate(),
                quadrangle.getPoint2().getX() / getRate(),
                quadrangle.getPoint2().getY() / getRate(),
                quadrangle.getPoint2().getZ() / getRate(),
                color);

        shapeView.drawLine(quadrangle.getPoint2().getX() / getRate(),
                quadrangle.getPoint2().getY() / getRate(),
                quadrangle.getPoint2().getZ() / getRate(),
                quadrangle.getPoint3().getX() / getRate(),
                quadrangle.getPoint3().getY() / getRate(),
                quadrangle.getPoint3().getZ() / getRate(),
                color);

        shapeView.drawLine(quadrangle.getPoint3().getX() / getRate(),
                quadrangle.getPoint3().getY() / getRate(),
                quadrangle.getPoint3().getZ() / getRate(),
                quadrangle.getPoint4().getX() / getRate(),
                quadrangle.getPoint4().getY() / getRate(),
                quadrangle.getPoint4().getZ() / getRate(),
                color);

        shapeView.drawLine(quadrangle.getPoint4().getX() / getRate(),
                quadrangle.getPoint4().getY() / getRate(),
                quadrangle.getPoint4().getZ() / getRate(),
                quadrangle.getPoint1().getX() / getRate(),
                quadrangle.getPoint1().getY() / getRate(),
                quadrangle.getPoint1().getZ() / getRate(),
                color);
    }
}
