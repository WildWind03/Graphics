package ru.nsu.fit.g14201.chirikhin.view;

import ru.nsu.fit.g14201.chirikhin.model.Box;

import java.awt.*;

public class BoxDrawer implements Drawer {
    private final Box box;
    private final Color color;

    public BoxDrawer(Box box, Color color) {
        this.box = box;
        this.color = color;
    }

    @Override
    public void draw(ShapeView shapeView) {
        shapeView.drawLine(box.getMinPoint(), box.getMaxPoint(), color);
    }
}
