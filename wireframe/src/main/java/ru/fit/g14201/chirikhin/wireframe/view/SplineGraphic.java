package ru.fit.g14201.chirikhin.wireframe.view;

import javax.swing.*;
import java.awt.*;

public class SplineGraphic extends JPanel {
    private final int width;
    private final int height;

    public SplineGraphic(int width, int height) {
        super(true);
        this.height = height;
        this.width = width;
        setPreferredSize(new Dimension(width, height));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawLine(0, height / 2, width, height / 2);
        g.drawLine(width / 2, 0, width / 2, height);
    }
}
