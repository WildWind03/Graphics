package ru.fit.g14201.chirikhin.wireframe.view;

import chirikhin.swing.dialog.FormattedTextFieldListDialog;
import chirikhin.swing.util.ListUtil;
import javafx.scene.input.KeyCode;
import ru.fit.g14201.chirikhin.wireframe.bspline.BSplineFunction;
import ru.fit.g14201.chirikhin.wireframe.bspline.Point;
import ru.fit.g14201.chirikhin.wireframe.model.Shape;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class SplineGraphic extends JPanel {
    private static final int ovalRadius1 = 4;
    private static final int ovalRadius2 = 7;

    private static final int MIN_COUNT_OF_SHAPE_FOR_AUTOSIZE = 3;

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
                if (null != shape && null != selectedPoint) {
                    float oldMax = pixelCoordinateToAreaConverter.getEndX();

                    if (e.getX() > width) {
                        float gapX = e.getX() - width;
                        float addPartX = (gapX / (float) width) * 2 * oldMax;
                        selectedPoint.setX(oldMax + addPartX);
                    } else {
                        float newX = pixelCoordinateToAreaConverter.toRealX(e.getX());
                        selectedPoint.setX(newX);
                    }

                    if (e.getY() > height) {
                        int gapY = e.getY() - height;
                        float addPartY = (gapY / (float) height) * 2 * oldMax;
                        selectedPoint.setY(-oldMax - addPartY);
                    } else {
                        float newY = pixelCoordinateToAreaConverter.toRealY(height - e.getY());
                        selectedPoint.setY(newY);
                    }

                    if (shape.getPoints().size() >= MIN_COUNT_OF_SHAPE_FOR_AUTOSIZE) {
                        float newMax = getMax(shape.getPoints());
                        pixelCoordinateToAreaConverter = new PixelCoordinateToAreaConverter(
                                -newMax, -newMax, newMax, newMax, width, height);
                    }

                    drawSpline();
                }
            }
        });

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                if (SwingUtilities.isRightMouseButton(e)) {
                    if (null != shape) {
                        int counter = 0;

                        for (Point point : shape.getPoints()) {
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
                                    return;
                                }
                            }
                            counter++;
                        }
                    }
                }

                if (e.getClickCount() == 2 && !e.isConsumed()) {
                    e.consume();
                    if (null != shape) {
                        int counter = 0;
                        for (Point point : shape.getPoints()) {
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

                        shape.addPoint(new Point(fieldX, fieldY));

                        drawSpline();
                    }
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                super.mouseClicked(e);
                if (SwingUtilities.isRightMouseButton(e)) {
                    return;
                }

                if (null != shape && !shape.isEmpty()) {
                    int counter = 0;
                    for (Point point : shape.getPoints()) {
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

            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                if (null != shape && !shape.isEmpty()) {
                    selectedPoint = null;
                    drawSpline();
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

        if (null != shape) {
            if (this.shape.getPoints().size() < 3) {
                FormattedTextFieldListDialog formattedTextFieldListDialog = new FormattedTextFieldListDialog(null,
                        "The spline doesn't contain enough points to draw coordinate system. Please, type necessary" +
                                " information manually", 1) {
                    @Override
                    protected void onDialogCreated(HashMap<String, Object> propertyResourceBundle) {
                        super.onDialogCreated(propertyResourceBundle);
                        addTextField("Max value for field", 0, 1000, "MAX_VALUE", 50);
                        setResizable(false);
                    }
                };
                formattedTextFieldListDialog.apparate();

                if (!formattedTextFieldListDialog.isCancelled()) {
                    int result = Integer.parseInt(formattedTextFieldListDialog.getResult().get("MAX_VALUE"));
                    pixelCoordinateToAreaConverter = new PixelCoordinateToAreaConverter(-result, -result, result, result,
                            width, height);
                } else {
                    final int maxValue = 50;
                    JOptionPane.showMessageDialog(this, "Max value is " + maxValue, "Default settings",
                            JOptionPane.INFORMATION_MESSAGE);

                    pixelCoordinateToAreaConverter = new PixelCoordinateToAreaConverter(-maxValue, -maxValue, maxValue,
                            maxValue, width, height);
                }
            } else {
                float max = getMax(shape.getPoints());
                pixelCoordinateToAreaConverter =
                        new PixelCoordinateToAreaConverter(-max, -max, max, max, width, height);
            }
        }

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

    private float getMax(List<Point> list) {
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
}
