package ru.fit.g14201.chirikhin.wireframe.view;

import ru.fit.g14201.chirikhin.wireframe.bspline.*;
import ru.fit.g14201.chirikhin.wireframe.bspline.Point;
import ru.fit.g14201.chirikhin.wireframe.model.Shape;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class SplineGraphic extends JPanel {
    private final int width;
    private final int height;
    private final PixelCoordinateToAreaConverter pixelCoordinateToAreaConverter;

    private final BufferedImage bufferedImage;

    public SplineGraphic(int width, int height, int startX, int startY, int endX, int endY) {
        super(true);
        this.pixelCoordinateToAreaConverter = new PixelCoordinateToAreaConverter(startX,
                startY, endX, endY, width, height);

        this.height = height;
        this.width = width;
        this.bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        setPreferredSize(new Dimension(width, height));
    }

    public void drawPoints(ArrayList<Point> points) {
        Graphics2D graphics2D = bufferedImage.createGraphics();

        for (Point p : points) {
            int realX = pixelCoordinateToAreaConverter.toPixelX(p.getX());
            int realY = pixelCoordinateToAreaConverter.toPixelY(p.getY());
            graphics2D.drawLine(realX, realY, realX, realY);
        }

        graphics2D.dispose();
        repaint();
    }

    public void drawSpline(Shape shape) {
        BSplineFunction bSplineFunction = new BSplineFunction(shape.getPoints());

        for (int i = 1; i < shape.getPoints().size() - 2; ++i) {
            for (float t = 0; t < 1; t += 0.01) {
                Point point = bSplineFunction.getValue(i, t);

                int realX = pixelCoordinateToAreaConverter.toPixelX(point.getX());
                int realY = pixelCoordinateToAreaConverter.toPixelY(point.getY());
                bufferedImage.createGraphics().drawLine(realX, realY, realX, realY);
            }
        }

        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(bufferedImage, 0, 0, null);
    }
}
