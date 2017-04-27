package ru.fit.g14201.chirikhin.wireframe.view;

import chirikhin.swing.util.ListUtil;
import ru.fit.g14201.chirikhin.wireframe.bspline.BSplineFunction;
import chirikhin.support.Point;
import ru.fit.g14201.chirikhin.wireframe.model.Shape;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SplineGraphic extends JPanel {
    private static final int ovalRadius1 = 4;
    private static final int ovalRadius2 = 7;
    private static final int MAX_WIDTH = 50;

    private final int width;
    private final int height;

    private final BufferedImage bufferedImage;
    private PixelCoordinateToAreaConverter pixelCoordinateToAreaConverter;
    private Shape shape;

    private Point<Float, Float> selectedPoint = null;

    public SplineGraphic(int width, int height) {
        super(true);

        this.height = height;
        this.width = width;
        setMaxWidth(MAX_WIDTH);
        this.bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        setPreferredSize(new Dimension(width, height));

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);
                onMouseDragged(e);
            }
        });

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                if (SwingUtilities.isRightMouseButton(e)) {
                    if (onMouseRightButtonClick(e)) return;
                }

                onMouseDoubleClick(e);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                super.mouseClicked(e);
                if (SwingUtilities.isRightMouseButton(e)) {
                    return;
                }

                onMousePressed(e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                onMouseReleased();
            }
        });
    }

    private void onMouseReleased() {
        if (null != shape && !shape.isEmpty()) {
            selectedPoint = null;
            drawSpline();
        }
    }

    private void onMousePressed(MouseEvent e) {
        if (null != shape && !shape.isEmpty()) {
            int counter = 0;
            for (Point<Float, Float> point : shape.getPoints()) {
                int realX = pixelCoordinateToAreaConverter.toPixelX(point.getX());
                int realY = pixelCoordinateToAreaConverter.toPixelY(point.getY());
                int radius = (0 == counter % 2) ? ovalRadius1 : ovalRadius2;

                if (e.getX() >= realX - radius && e.getX() <= realX + radius) {
                    if (e.getY() >= realY - radius && e.getY() <= realY + radius) {
                        selectedPoint = point;
                        drawSpline();
                        break;
                    }
                }
                counter++;
            }
        }
    }

    private void onMouseDoubleClick(MouseEvent e) {
        if (e.getClickCount() == 2 && !e.isConsumed()) {
            e.consume();
            if (null != shape) {
                int counter = 0;
                for (Point<Float, Float> point : shape.getPoints()) {
                    int realX = pixelCoordinateToAreaConverter.toPixelX(point.getX());
                    int realY = pixelCoordinateToAreaConverter.toPixelY(point.getY());
                    int radius = (0 == counter % 2) ? ovalRadius1 : ovalRadius2;

                    if (e.getX() >= realX - radius && e.getX() <= realX + radius) {
                        if (e.getY() >= realY - radius && e.getY() <= realY + radius) {
                            return;
                        }
                    }
                    counter++;
                }

                float fieldX = pixelCoordinateToAreaConverter.toRealX(e.getX());
                float fieldY = pixelCoordinateToAreaConverter.toRealY(height - e.getY());

                shape.addPoint(new Point<>(fieldX, fieldY));

                drawSpline();
            }
        }
    }

    private boolean onMouseRightButtonClick(MouseEvent e) {
        if (null != shape) {
            int counter = 0;

            for (Point<Float, Float> point : shape.getPoints()) {
                int realX = pixelCoordinateToAreaConverter.toPixelX(point.getX());
                int realY = pixelCoordinateToAreaConverter.toPixelY(point.getY());
                int radius = (0 == counter % 2) ? ovalRadius1 : ovalRadius2;

                if (e.getX() >= realX - radius && e.getX() <= realX + radius) {
                    if (e.getY() >= realY - radius && e.getY() <= realY + radius) {
                        JPopupMenu jPopupMenu = new JPopupMenu();
                        JMenuItem itemRemove = new JMenuItem("Delete");
                        itemRemove.addActionListener(e1 -> {
                            shape.getPoints().remove(point);
                            drawSpline();
                        });

                        jPopupMenu.add(itemRemove);
                        jPopupMenu.show(SplineGraphic.this, e.getX(), e.getY());
                        return true;
                    }
                }
                counter++;
            }
        }
        return false;
    }

    private void drawPoints(ArrayList<Point<Float, Float>> points) {
        Graphics2D graphics2D = bufferedImage.createGraphics();

        graphics2D.setColor(Color.RED);

        int counter = 0;
        int prevRealX = 0;
        int prevRealY = 0;
        for (Point<Float, Float> p : points) {
            int realX = pixelCoordinateToAreaConverter.toPixelX(p.getX());
            int realY = pixelCoordinateToAreaConverter.toPixelY(p.getY());
            int ovarRadius = (0 == counter % 2) ? ovalRadius1 : ovalRadius2;

            if (selectedPoint == p) {
                graphics2D.setColor(Color.green);
            }

            graphics2D.drawOval(realX - ovarRadius, realY - ovarRadius, ovarRadius * 2,
                    ovarRadius * 2);

            if (selectedPoint == p) {
                graphics2D.setColor(Color.RED);
            }
            if (counter++ > 0) {
                graphics2D.drawLine(realX, realY, prevRealX, prevRealY);
            }

            prevRealX = realX;
            prevRealY = realY;

        }

        graphics2D.dispose();
    }

    public void setShape(Shape shape) {
        this.shape = shape;
        drawSpline();
    }

    private void drawSpline() {
        clearImage();
        drawCoordinateSystem();

        if (null == shape || shape.getPoints().isEmpty()) {
            repaint();
            return;
        }

        drawPoints(shape.getPoints());

        Graphics2D graphics2D = bufferedImage.createGraphics();
        graphics2D.setColor(Color.WHITE);

        BSplineFunction bSplineFunction = new BSplineFunction(shape.getPoints());

        for (int i = 1; i < shape.getPoints().size() - 2; ++i) {
            for (float t = 0; t < 1; t += 0.01) {
                Point<Float, Float> point = bSplineFunction.getValue(i, t);

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
        graphics2D.fillRect(0, 0, width, height);

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

    public void setMaxWidth(float maxWidth) {
        this.pixelCoordinateToAreaConverter = new PixelCoordinateToAreaConverter(-maxWidth, -maxWidth,
                maxWidth, maxWidth, width, height);
    }

    public float getMaxWidth() {
        return pixelCoordinateToAreaConverter.getEndX();
    }

    public void onMouseDragged(MouseEvent e) {
        if (null != shape && null != selectedPoint) {
            float oldMax = pixelCoordinateToAreaConverter.getEndX();
            float newX;
            float newY;

            if (e.getX() > width) {
                float gapX = e.getX() - width;
                float addPartX = (gapX / (float) width) * 2 * oldMax;
                newX = oldMax + addPartX;
                selectedPoint.setX(newX);
            } else {
                newX = pixelCoordinateToAreaConverter.toRealX(e.getX());
                selectedPoint.setX(newX);
            }

            if (e.getY() > height) {
                int gapY = e.getY() - height;
                float addPartY = (gapY / (float) height) * 2 * oldMax;
                newY = -oldMax - addPartY;
                selectedPoint.setY(newY);
            } else {
                newY = pixelCoordinateToAreaConverter.toRealY(height - e.getY());
                selectedPoint.setY(newY);
            }

            float newMax = Math.max(Math.abs(newX), Math.abs(newY));
            if (newMax > getMaxWidth()) {
                setMaxWidth(newMax);
            }

            drawSpline();
        }
    }

    private float getMax(List<Point<Float, Float>> list) {
        if (null == list) {
            throw new IllegalArgumentException("List must be not null");
        }

        Optional<Float> maxValue = list
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
                });

        if (maxValue.isPresent()) {
            return Math.abs(maxValue.get());
        }

        throw new IllegalArgumentException("Optional doesn't contain value");
    }

    public void scaleField(float scaleRate) {
        float newMaxField = getMaxWidth() * scaleRate;
        setMaxWidth(newMaxField);
        drawSpline();
    }

    public void autosizeField() {
        if (null != shape) {
            setMaxWidth(getMax(shape.getPoints()));
        }

        drawSpline();
    }
}
