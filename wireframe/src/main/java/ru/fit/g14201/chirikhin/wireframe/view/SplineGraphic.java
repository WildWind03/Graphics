package ru.fit.g14201.chirikhin.wireframe.view;

import chirikhin.swing.util.ListUtil;
import ru.fit.g14201.chirikhin.wireframe.bspline.BSplineFunction;
import ru.fit.g14201.chirikhin.wireframe.bspline.Point;
import ru.fit.g14201.chirikhin.wireframe.model.Shape;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class SplineGraphic extends JPanel {
    private final int width;
    private final int height;

    private final BufferedImage bufferedImage;

    public SplineGraphic(int width, int height) {
        super(true);

        this.height = height;
        this.width = width;
        this.bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        setPreferredSize(new Dimension(width, height));
    }

    private void drawPoints(ArrayList<Point> points) {
        Graphics2D graphics2D = bufferedImage.createGraphics();

        float max = getMax(points);

        PixelCoordinateToAreaConverter pixelCoordinateToAreaConverter =
                new PixelCoordinateToAreaConverter(-max, -max, max, max, width, height);

        graphics2D.setColor(Color.WHITE);

        for (Point p : points) {
            int realX = pixelCoordinateToAreaConverter.toPixelX(p.getX());
            int realY = pixelCoordinateToAreaConverter.toPixelY(p.getY());
            graphics2D.drawLine(realX, realY, realX, realY);
        }

        graphics2D.dispose();
        repaint();
    }

    public void drawSpline(Shape shape) {
        clearImage();
        drawCoordinateSystem();

        if (shape.getPoints().isEmpty()) {
            return;
        }

        drawPoints(shape.getPoints());

        Graphics2D graphics2D = bufferedImage.createGraphics();
        graphics2D.setColor(Color.WHITE);

        BSplineFunction bSplineFunction = new BSplineFunction(shape.getPoints());
        float max = getMax(shape.getPoints());

        PixelCoordinateToAreaConverter pixelCoordinateToAreaConverter =
                new PixelCoordinateToAreaConverter(-max, -max, max, max, width, height);

        for (int i = 1; i < shape.getPoints().size() - 2; ++i) {
            for (float t = 0; t < 1; t += 0.01) {
                Point point = bSplineFunction.getValue(i, t);

                int realX = pixelCoordinateToAreaConverter.toPixelX(point.getX());
                int realY = pixelCoordinateToAreaConverter.toPixelY(point.getY());
                graphics2D.drawLine(realX, realY, realX, realY);
            }
        }

        graphics2D.dispose();

        repaint();
    }

    private void clearImage() {
        Graphics2D graphics2D = bufferedImage.createGraphics();
        graphics2D.setColor(Color.BLACK);
        graphics2D.fillRect(0, 0 , width, height);

        graphics2D.dispose();
    }

    private void drawCoordinateSystem() {
        Graphics2D graphics2D = bufferedImage.createGraphics();
        graphics2D.drawLine(0, height / 2, width, height / 2);
        graphics2D.drawLine(width / 2, 0, width / 2, height);

        graphics2D.dispose();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(bufferedImage, 0, 0, null);
    }

    private float getMax(List<Point> list) {
        return Math.abs(list
                .stream()
                .flatMap(point
                        -> ListUtil.asList(point.getX(), point.getY()).stream())
                .max((o1, o2) -> {
                    if (Math.abs(o1) > Math.abs(o2)) {
                        return 1;
                    } else {
                        if (Math.abs(o1) < Math.abs(o2)) {
                            return -1;
                        } else {
                            return 0;
                        }
                    }
                })
                .get());
    }
}
