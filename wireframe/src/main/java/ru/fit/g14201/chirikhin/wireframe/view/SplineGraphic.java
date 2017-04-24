package ru.fit.g14201.chirikhin.wireframe.view;

import chirikhin.swing.util.ListUtil;
import ru.fit.g14201.chirikhin.wireframe.bspline.BSplineFunction;
import ru.fit.g14201.chirikhin.wireframe.bspline.Point;
import ru.fit.g14201.chirikhin.wireframe.model.Shape;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class SplineGraphic extends JPanel {
    public static final int ovalRadius1 = 4;
    public static final int ovalRadius2 = 7;

    private final int width;
    private final int height;

    private final BufferedImage bufferedImage;
    private PixelCoordinateToAreaConverter pixelCoordinateToAreaConverter;
    private Shape shape;

    private Point selectedPoint = null;

    public SplineGraphic(int width, int height) {
        super(true);

        this.height = height;
        this.width = width;
        this.bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        setPreferredSize(new Dimension(width, height));

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);
                System.out.println("Drag");
                if (null != selectedPoint) {
                    selectedPoint.setX(pixelCoordinateToAreaConverter.toRealX(e.getX()));
                    selectedPoint.setY(pixelCoordinateToAreaConverter.toRealY(height - e.getY()));
                    drawSpline(shape);
                }
            }
        });

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mouseClicked(e);

                if (null != shape) {
                    int counter = 0;
                    for (Point point : shape.getPoints()) {
                        int realX = pixelCoordinateToAreaConverter.toPixelX(point.getX());
                        int realY = pixelCoordinateToAreaConverter.toPixelY(point.getY());
                        int radius = (0 == counter % 2) ? ovalRadius1 : ovalRadius2;

                        if (e.getX() >= realX - radius && e.getX() <= realX + radius) {
                            if (e.getY() >= realY - radius && e.getY() <= realY + radius) {
                                selectedPoint = point;
                                System.out.println("Press");
                                break;
                            }
                        }
                        counter++;
                    }
                }
            }
        });
    }

    private void drawPoints(ArrayList<Point> points) {
        Graphics2D graphics2D = bufferedImage.createGraphics();

        graphics2D.setColor(Color.RED);

        int counter = 0;
        int prevRealX = 0;
        int prevRealY = 0;
        for (Point p : points) {
            int realX = pixelCoordinateToAreaConverter.toPixelX(p.getX());
            int realY = pixelCoordinateToAreaConverter.toPixelY(p.getY());
            int ovarRadius = (0 == counter % 2) ? ovalRadius1 : ovalRadius2;
            graphics2D.drawOval(realX - ovarRadius, realY - ovarRadius, ovarRadius * 2,
                    ovarRadius * 2);
            if (counter++ > 0) {
                graphics2D.drawLine(realX, realY, prevRealX, prevRealY);
            }

            prevRealX = realX;
            prevRealY = realY;

        }

        graphics2D.dispose();
        repaint();
    }

    public void setShape(Shape shape) {
        this.shape = shape;
        if (!shape.getPoints().isEmpty()) {
            float max = getMax(shape.getPoints());
            pixelCoordinateToAreaConverter =
                    new PixelCoordinateToAreaConverter(-max, -max, max, max, width, height);
        } else {
            pixelCoordinateToAreaConverter = null;
        }
        drawSpline(shape);
    }

    private void drawSpline(Shape shape) {
        clearImage();
        drawCoordinateSystem();

        if (shape.getPoints().isEmpty()) {
            repaint();
            return;
        }

        drawPoints(shape.getPoints());

        Graphics2D graphics2D = bufferedImage.createGraphics();
        graphics2D.setColor(Color.WHITE);

        BSplineFunction bSplineFunction = new BSplineFunction(shape.getPoints());

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
        graphics2D.setColor(Color.WHITE);
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
