package ru.fit.g14201.chirikhin.wireframe.view;

import chirikhin.matrix.Matrix;
import chirikhin.matrix.MatrixUtil;
import chirikhin.support.Line;
import chirikhin.support.Point;
import chirikhin.support.Point3D;
import ru.fit.g14201.chirikhin.wireframe.bspline.BSplineFunction;
import ru.fit.g14201.chirikhin.wireframe.model.Model;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class ShapeView extends JPanel {
    private final BufferedImage bufferedImage;
    private final int height;
    private final int width;
    private ArrayList<Line<Point3D<Float, Float, Float>>> shapeLines = new ArrayList<>();
    private final int DEFAULT_SCALE_RATIO = 100;
    private Model model;

    private Point<Integer, Integer> prevPoint = new Point<>(0, 0);

    private Matrix M = new Matrix(new float[][] {
            {1, 0, 0, 0},
            {0, 1, 0, 0},
            {0, 0, 1, 0},
            {0, 0, 0, 1}
        });

    public ShapeView(int width, int height) {
        super(true);

        setPreferredSize(new Dimension(width, height));
        this.height = height;
        this.width = width;
        bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

//        shapeLines.add(new Line<>(new Point3D<>(0f, 0f, 0f), new Point3D<>(0f, 1f, 0f)));
//        shapeLines.add(new Line<>(new Point3D<>(0f, 0f, 0f), new Point3D<>(1f, 0f, 0f)));
//        shapeLines.add(new Line<>(new Point3D<>(0f, 0f, 0f), new Point3D<>(0f, 0f, 1f)));
//        shapeLines.add(new Line<>(new Point3D<>(0f, 1f, 0f), new Point3D<>(0f, 0f, 1f)));

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);
                onMouseDragged(e, prevPoint);
                prevPoint = new Point<>(e.getX(), e.getY());
            }
        });

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                prevPoint = new Point<>(e.getX(), e.getY());
            }
        });
    }

    private void onMouseDragged(MouseEvent e, Point<Integer, Integer> point) {
        int dy = e.getX() - point.getX();
        int dx = e.getY() - point.getY();

        Matrix qxMatrix = calculateQxMatrix((float) (((float) dx / (float) width) * 2 * Math.PI));
        Matrix qyMatrix = calculateQyMatrix((float) (((float) dy / (float) height) * 2 * Math.PI));

        M = MatrixUtil.multiply(qyMatrix,
                                MatrixUtil.multiply(qxMatrix, M));

        repaint();
    }

    public void setModel(Model newModel) {
        this.model = newModel;
        BSplineFunction bSplineFunction = new BSplineFunction(model.getShapes().get(0).getPoints());
        shapeLines = ShapeToLinesConverter.toLines(bSplineFunction, model.getN(), model.getM(), model.getK(),
                model.getA(), model.getB(), model.getD(), model.getC());
    }

    public void update() {
        BSplineFunction bSplineFunction = new BSplineFunction(model.getShapes().get(0).getPoints());
        shapeLines = ShapeToLinesConverter.toLines(bSplineFunction, model.getN(), model.getM(), model.getK(),
                model.getA(), model.getB(), model.getC(), model.getD());
    }

    private Matrix calculateZoomMatrix(int scaleX, int scaleY, int scaleZ) {
        return new Matrix(new float[][]{
                {scaleX, 0, 0, 0},
                {0, scaleY, 0, 0},
                {0, 0, scaleZ, 0},
                {0, 0, 0, 1}
        });
    }

    private Matrix calculateShiftMatrix(int shiftX, int shiftY, int shiftZ) {
        return new Matrix(new float[][] {
                {1, 0, 0, shiftX},
                {0, 1, 0, shiftY},
                {0, 0, 1, shiftZ},
                {0, 0, 0, 1}
        });
    }

    private Matrix calculateQzMatrix(float angleZ) {
        return new Matrix(new float[][]{
                {(float) cos(angleZ), (float) -sin(angleZ), 0, 0},
                {(float) sin(angleZ), (float) cos(angleZ), 0, 0},
                {0, 0, 1, 0},
                {0, 0, 0, 1}
        });
    }

    private Matrix calculateQxMatrix(float angleX) {
        return new Matrix(new float[][] {
                {1, 0, 0, 0},
                {0, (float) cos(angleX), (float) -sin(angleX), 0},
                {0, (float) sin(angleX), (float) cos(angleX), 0},
                {0, 0, 0, 1}
        });
    }

    private Matrix calculateQyMatrix(float angleY) {
        return new Matrix(new float[][]{
                {(float) cos(angleY), 0, (float) sin(angleY), 0},
                {0, 1, 0, 0},
                {(float) -sin(angleY), 0, (float) cos(angleY), 0},
                {0, 0, 0, 1},
        });
    }

    private void drawLine(Line<Point3D<Float, Float, Float>> line) {
        Matrix start = new Matrix(new float[][] {{line.getStart().getX()},
                {line.getStart().getY()},{line.getStart().getZ()}, {1}});

        Matrix end = new Matrix(new float[][] {{line.getEnd().getX()},
                {line.getEnd().getY()}, {line.getEnd().getZ()}, {1}});

        Matrix realStart = MatrixUtil.multiply(M, start);
        Matrix realEnd = MatrixUtil.multiply(M, end);

        Matrix zoomMatrix = calculateZoomMatrix(DEFAULT_SCALE_RATIO, DEFAULT_SCALE_RATIO, DEFAULT_SCALE_RATIO);
        Matrix shiftMatrix = calculateShiftMatrix(getWidth() / 2, getHeight() / 2, 0);

        realStart = MatrixUtil.multiply(shiftMatrix, MatrixUtil.multiply(zoomMatrix, realStart));
        realEnd = MatrixUtil.multiply(shiftMatrix, MatrixUtil.multiply(zoomMatrix, realEnd));

        float x0 = realStart.get(0, 0);
        float y0 = realStart.get(1, 0);

        float x1 = realEnd.get(0, 0);
        float y1 = realEnd.get(1, 0);

        Graphics2D g = bufferedImage.createGraphics();
        g.setColor(Color.WHITE);
        g.drawLine((int) x0,  (int) y0, (int) x1, (int) y1);
        g.dispose();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = bufferedImage.createGraphics();
        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, width, height);
        g2d.dispose();

        for (Line<Point3D<Float, Float, Float>> line : shapeLines) {
            drawLine(line);
        }

        g.drawImage(bufferedImage, 0, 0, null);
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public int getWidth() {
        return width;
    }
}
