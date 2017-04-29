package ru.fit.g14201.chirikhin.wireframe.view;

import chirikhin.matrix.Matrix;
import chirikhin.matrix.MatrixUtil;
import chirikhin.support.Line;
import chirikhin.support.Point;
import chirikhin.support.Point3D;
import ru.fit.g14201.chirikhin.wireframe.bspline.BSplineFunction;
import ru.fit.g14201.chirikhin.wireframe.model.BSpline;
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
    private final int DEFAULT_SCALE_RATIO = 200;
    private Model model;
    private Integer selectedShape = null;

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

            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
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
        if (!model.isEmpty()) {
            selectedShape = 0;
        } else {
            selectedShape = null;
        }
        update();
    }

    public void update() {
        repaint();
    }

    private Matrix calculateZoomMatrix(int scaleX, int scaleY, int scaleZ) {
        return new Matrix(new float[][]{
                {scaleX, 0, 0, 0},
                {0, scaleY, 0, 0},
                {0, 0, scaleZ, 0},
                {0, 0, 0, 1}
        });
    }

    private Matrix calculateShiftMatrix(float shiftX, float shiftY, float shiftZ) {
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

    private Point3D<Float, Float, Float> multiplyVectors(Point3D<Float, Float, Float> vector1,
                                                         Point3D<Float, Float, Float> vector2) {
        return new Point3D<>(vector1.getY() * vector2.getZ() - vector1.getZ() * vector2.getY(),
                vector1.getX() * vector2.getZ() - vector1.getZ() * vector2.getX(),
                vector1.getX() * vector2.getY() - vector1.getY() * vector2.getX());
    }

    private Matrix calculateCameraMatrix(Point3D<Float, Float, Float> cameraPosition,
                                         Point3D<Float, Float, Float> viewPoint,
                                         Point3D<Float, Float, Float> upVector) {
        float normalizeForK = (float) Math.sqrt((cameraPosition.getX() - viewPoint.getX()) * (cameraPosition.getX() - viewPoint.getX())
                + (cameraPosition.getY() - viewPoint.getY()) * (cameraPosition.getY() - viewPoint.getY()) +
                (cameraPosition.getZ() - viewPoint.getZ()) * (cameraPosition.getZ() - viewPoint.getZ()));

        float kx = (cameraPosition.getX() - viewPoint.getX()) /
                normalizeForK;
        float ky = (cameraPosition.getY() - viewPoint.getY()) /
                normalizeForK;
        float kz = (cameraPosition.getZ() - viewPoint.getZ()) /
                normalizeForK;

        Point3D<Float, Float, Float> iVector = multiplyVectors(upVector, new Point3D<>(kx, ky, kz));

        float normalizeForI = (float) Math.sqrt(iVector.getX() * iVector.getX() +
                iVector.getY() * iVector.getY() +
                iVector.getZ() * iVector.getZ());
        float ix = iVector.getX() / normalizeForI;
        float iy = iVector.getY() / normalizeForI;
        float iz = iVector.getZ() / normalizeForI;

        Point3D<Float, Float, Float> jVector = multiplyVectors(new Point3D<>(kx, ky, kz),
                new Point3D<>(ix, iy, iz));

        return MatrixUtil.multiply(new Matrix(new float[][] {
                {ix, iy, iz, 0},
                {jVector.getX(), jVector.getY(), jVector.getZ(), 0},
                {kx, ky, kz, 0},
                {0, 0, 0, 1}
        }), new Matrix(new float[][] {
                {1, 0, 0, -cameraPosition.getX()},
                {0, 1, 0, -cameraPosition.getY()},
                {0, 0, 1, -cameraPosition.getZ()},
                {0, 0, 0, 1}
        }));
    }

    private Matrix calculateProjMatrix(float sw, float sh, float zf, float zn) {
        return new Matrix(new float[][] {
                {2 * zf / sw, 0, 0, 0},
                {0, 2 * zf / sh, 0, 0},
                {0, 0, zn / (zn - zf), 0},
                {0, 0, 1, 0}
        });
    };

    private void drawLine(Line<Point3D<Float, Float, Float>> line,
                          float cx, float cy, float cz, Color color) {
        Matrix start = new Matrix(new float[][] {{line.getStart().getX()},
                {line.getStart().getY()},{line.getStart().getZ()}, {1}});

        Matrix end = new Matrix(new float[][] {{line.getEnd().getX()},
                {line.getEnd().getY()}, {line.getEnd().getZ()}, {1}});

        Matrix realStart = MatrixUtil.multiply(M, start);
        Matrix realEnd = MatrixUtil.multiply(M, end);

        Matrix zoomMatrix = calculateZoomMatrix(DEFAULT_SCALE_RATIO, DEFAULT_SCALE_RATIO, DEFAULT_SCALE_RATIO);
        Matrix shiftMatrix = calculateShiftMatrix(cx,
                cy , cz);

        Matrix cameraMatrix = calculateCameraMatrix(new Point3D<>(0f, 0f,-10f),
                new Point3D<>(1f, 0f, 0f),
                new Point3D<>(0f, 1f, 0f));

        Matrix projMatrix = calculateProjMatrix(model.getSw(),
                model.getSh(), model.getZf(), model.getZn());

        realStart = MatrixUtil.multiply(projMatrix,
                MatrixUtil.multiply(cameraMatrix, MatrixUtil.multiply(shiftMatrix, realStart)));
        realEnd = MatrixUtil.multiply(projMatrix,
                MatrixUtil.multiply(cameraMatrix, MatrixUtil.multiply(shiftMatrix, realEnd)));

        float x0 = realStart.get(0, 0) + width / 2;
        float y0 = realStart.get(1, 0) + height / 2;

        float x1 = realEnd.get(0, 0) + width / 2;
        float y1 = realEnd.get(1, 0) + height / 2;

        Graphics2D g = bufferedImage.createGraphics();
        g.setColor(color);
        g.drawLine((int) x0,  (int) y0, (int) x1, (int) y1);
        g.dispose();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = bufferedImage.createGraphics();
        g2d.setColor(model.getBackgroundColor());
        g2d.fillRect(0, 0, width, height);
        g2d.dispose();

        drawCube();

        if (null != model) {
            for (BSpline BSpline : model.getbSplines()) {
                drawShape(BSpline);
            }
        }

        g.drawImage(bufferedImage, 0, 0, null);
    }

    private void drawShape(BSpline BSpline) {
        BSplineFunction bSplineFunction = new BSplineFunction(BSpline.getPoints());
        ArrayList<Line<Point3D<Float, Float, Float>>> shapeLines =
                ShapeToLinesConverter.toLines(bSplineFunction, model.getN(), model.getM(), model.getK(),
                    model.getA(), model.getB(), model.getD(), model.getC());

        for (Line<Point3D<Float, Float, Float>> line : shapeLines) {
            drawLine(line, BSpline.getCx(), BSpline.getCy(), BSpline.getCz(), BSpline.getColor());
        }
    }

    public void drawCube() {
        ArrayList<Line<Point3D<Float, Float, Float>>> cubeLines = new ArrayList<>();

        cubeLines.add(new Line<>(new Point3D<>(-1f, -1f, -1f), new Point3D<>(1f, -1f, -1f)));
        cubeLines.add(new Line<>(new Point3D<>(-1f, -1f, -1f), new Point3D<>(-1f, 1f, -1f)));
        cubeLines.add(new Line<>(new Point3D<>(-1f, -1f, -1f), new Point3D<>(-1f, -1f, 1f)));

        cubeLines.add(new Line<>(new Point3D<>(1f, 1f, -1f), new Point3D<>(1f, -1f, -1f)));
        cubeLines.add(new Line<>(new Point3D<>(1f, -1f, 1f), new Point3D<>(1f, -1f, -1f)));
        cubeLines.add(new Line<>(new Point3D<>(-1f, 1f, -1f), new Point3D<>(1f, 1f, -1f)));
        cubeLines.add(new Line<>(new Point3D<>(-1f, -1f, 1f), new Point3D<>(1f, -1f, 1f)));

        cubeLines.add(new Line<>(new Point3D<>(1f, 1f, -1f), new Point3D<>(1f, 1f, 1f)));
        cubeLines.add(new Line<>(new Point3D<>(1f, -1f, 1f), new Point3D<>(1f, 1f, 1f)));
        cubeLines.add(new Line<>(new Point3D<>(-1f, 1f, 1f), new Point3D<>(1f, 1f, 1f)));

        cubeLines.add(new Line<>(new Point3D<>(-1f, 1f, 1f), new Point3D<>(-1f, 1f, -1f)));
        cubeLines.add(new Line<>(new Point3D<>(-1f, 1f, 1f), new Point3D<>(-1f, -1f, 1f)));

        for (Line<Point3D<Float, Float, Float>> line : cubeLines) {
            drawLine(line, 0, 0, 0, Color.WHITE);
        }
    }

    public Integer getSelectedShape() {
        return selectedShape;
    }

    public void setSelectedShape(Integer selectedShape) {
        this.selectedShape = selectedShape;
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
