package ru.nsu.fit.g14201.chirikhin.view;

import ru.nsu.fit.g14201.chirikhin.model.*;
import ru.nsu.fit.g14201.chirikhin.model.Shape;
import sun.security.provider.SHA;

import java.awt.*;

public class DrawerFactory {
    private DrawerFactory() {

    }

    public static Drawer createDrawer(Shape shape, Color color) {
        if (shape instanceof Box) {
            return new BoxDrawer((Box) shape, color);
        }

        if (shape instanceof Sphere) {
            return new SphereDrawer((Sphere) shape, color, 10);
        }

        if (shape instanceof Triangle) {
            return new TriangleDrawer((Triangle) shape, color);
        }

        if (shape instanceof Quadrangle) {
            return new QuadrangleDrawer((Quadrangle) shape, color);
        }

        throw new IllegalArgumentException("Unknown shape!");
    }
}
