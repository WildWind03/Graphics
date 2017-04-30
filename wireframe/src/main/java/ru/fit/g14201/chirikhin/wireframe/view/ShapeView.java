package ru.fit.g14201.chirikhin.wireframe.view;

import chirikhin.matrix.Matrix;
import chirikhin.matrix.MatrixUtil;
import chirikhin.support.Line;
import chirikhin.support.Point;
import chirikhin.support.Point3D;
import chirikhin.swing.util.ListUtil;
import ru.fit.g14201.chirikhin.wireframe.bspline.BSplineFunction;
import ru.fit.g14201.chirikhin.wireframe.model.BSpline;
import ru.fit.g14201.chirikhin.wireframe.model.Model;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.util.*;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class ShapeView extends JPanel {
    private static final float ZOOM_PLUS_RATIO = 1.1f;
    private static final float ZOOM_MINUS_RATIO = 0.9f;
    private final BufferedImage bufferedImage;
    private final int height;
    private final int width;
    private Model model;
    private Integer selectedShape = null;

    private Point<Integer, Integer> prevPoint = new Point<>(0, 0);

    private Matrix SCENE_ROTATION_MATRIX = new Matrix(new float[][]{
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

        addMouseWheelListener(e -> {
            int notches = e.getWheelRotation();

            if (notches < 0) {
                model.setZf(model.getZf() * ZOOM_PLUS_RATIO);
                repaint();
            } else {
                model.setZf(model.getZf() * ZOOM_MINUS_RATIO);
                repaint();
            }
        });
    }

    private void onMouseDragged(MouseEvent e, Point<Integer, Integer> point) {
        int dx = -(e.getX() - point.getX());
        int dy = -(e.getY() - point.getY());

        Matrix qzMatrix = calculateQzMatrix((float) (((float) dy / (float) height) * 2 * Math.PI));
        Matrix qyMatrix = calculateQyMatrix((float) (((float) dx / (float) width) * 2 * Math.PI));

        SCENE_ROTATION_MATRIX = MatrixUtil.multiply(qzMatrix,
                MatrixUtil.multiply(qyMatrix, SCENE_ROTATION_MATRIX));
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

    private Matrix calculateZoomMatrix(float scaleX, float scaleY, float scaleZ) {
        return new Matrix(new float[][]{
                {scaleX, 0, 0, 0},
                {0, scaleY, 0, 0},
                {0, 0, scaleZ, 0},
                {0, 0, 0, 1}
        });
    }

    private Matrix calculateShiftMatrix(float shiftX, float shiftY, float shiftZ) {
        return new Matrix(new float[][]{
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
        return new Matrix(new float[][]{
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

        float kx = (viewPoint.getX() - cameraPosition.getX()) /
                normalizeForK;
        float ky = (viewPoint.getY() - cameraPosition.getY()) /
                normalizeForK;
        float kz = (viewPoint.getZ() - cameraPosition.getZ()) /
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

        return MatrixUtil.multiply(new Matrix(new float[][]{
                {ix, iy, iz, 0},
                {jVector.getX(), jVector.getY(), jVector.getZ(), 0},
                {kx, ky, kz, 0},
                {0, 0, 0, 1}
        }), new Matrix(new float[][]{
                {1, 0, 0, -viewPoint.getX()},
                {0, 1, 0, -viewPoint.getY()},
                {0, 0, 1, -viewPoint.getZ()},
                {0, 0, 0, 1}
        }));
    }

    private Matrix calculateProjMatrix(float sw, float sh, float zf, float zn) {
        return new Matrix(new float[][]{
                {2 * zf / sw, 0, 0, 0},
                {0, 2 * zf / sh, 0, 0},
                {0, 0, zn / (zn - zf), 0},
                {0, 0, 1, 0}
        });
    }

    private void drawLine(Point3D<Float, Float, Float> startPoint,
                          Point3D<Float, Float, Float> endPoint,
                          Color color) {
        drawLine(startPoint, endPoint, color, new Matrix(new float[][]{
                {1, 0, 0, 0},
                {0, 1, 0, 0},
                {0, 0, 1, 0},
                {0, 0, 0, 1}
        }));
    }

    private void drawLine(Point3D<Float, Float, Float> startPoint,
                          Point3D<Float, Float, Float> endPoint,
                          Color color, Matrix shapeMatrix) {
        Matrix start = new Matrix(new float[][]{{startPoint.getX()},
                {startPoint.getY()}, {startPoint.getZ()}, {1}});

        Matrix end = new Matrix(new float[][]{{endPoint.getX()},
                {endPoint.getY()}, {endPoint.getZ()}, {1}});

        Matrix realStart = MatrixUtil.multiply(SCENE_ROTATION_MATRIX, MatrixUtil.multiply(shapeMatrix, start));
        Matrix realEnd = MatrixUtil.multiply(SCENE_ROTATION_MATRIX, MatrixUtil.multiply(shapeMatrix, end));

        Matrix cameraMatrix = calculateCameraMatrix(new Point3D<>(-10f, 0f, 0f),
                new Point3D<>(10f, 0f, 0f),
                new Point3D<>(0f, 1f, 0f));

        Matrix projMatrix = calculateProjMatrix(model.getSw(),
                model.getSh(), model.getZf(), model.getZn());

        realStart = MatrixUtil.multiply(projMatrix,
                MatrixUtil.multiply(cameraMatrix, realStart));
        realEnd = MatrixUtil.multiply(projMatrix,
                MatrixUtil.multiply(cameraMatrix, realEnd));

        float x0 = realStart.get(0, 0) / realStart.get(3, 0);
        float y0 = realStart.get(1, 0) / realStart.get(3, 0);

        float x1 = realEnd.get(0, 0) / realEnd.get(3, 0);
        float y1 = realEnd.get(1, 0) / realEnd.get(3, 0);

        PixelCoordinateToAreaConverter pixelCoordinateToAreaConverter =
                new PixelCoordinateToAreaConverter(-1, -1, 1, 1, width, height);

        Graphics2D g = bufferedImage.createGraphics();
        g.setColor(color);
        g.drawLine(pixelCoordinateToAreaConverter.toPixelX(x0),
                pixelCoordinateToAreaConverter.toPixelY(y0), pixelCoordinateToAreaConverter.toPixelX(x1),
                pixelCoordinateToAreaConverter.toPixelY(y1));
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
        drawCoordinateSystem(0, 0, 0, 1);

        if (null != model) {
            ArrayList<ArrayList<Line<Point3D<Float, Float, Float>>>> linesOfSplines = new ArrayList<>();

            float globalMax = Float.MIN_VALUE;

            for (BSpline bSpline : model.getbSplines()) {
                ArrayList<Line<Point3D<Float, Float, Float>>> splineLines = ShapeToLinesConverter.toLines(new BSplineFunction(bSpline.getPoints()),
                        model.getN(), model.getM(), model.getK(), model.getA(), model.getB(), model.getD(), model.getC(), -1);
                linesOfSplines.add(splineLines);

                Matrix roundMatrix = bSpline.getRoundMatrix();
                for (Line<Point3D<Float, Float, Float>> line : splineLines) {

                    Matrix startPoint = MatrixUtil.multiply(roundMatrix, new Matrix(new float[][]{{line.getStart().getX()},
                            {line.getStart().getY()}, {line.getStart().getZ()}}));

                    Matrix endPoint = MatrixUtil.multiply(roundMatrix, new Matrix(new float[][]{{line.getEnd().getX()},
                            {line.getEnd().getY()}, {line.getEnd().getZ()}}));

                    float localMax = ListUtil.asList(startPoint.get(1, 0) + bSpline.getCy(), startPoint.get(0, 0) + bSpline.getCx(),
                            startPoint.get(2, 0) + bSpline.getCz(),
                            endPoint.get(0, 0) + bSpline.getCx(), endPoint.get(1, 0) + bSpline.getCy(),
                            endPoint.get(2, 0) + bSpline.getCz())
                            .stream()
                            .map(Math::abs)
                            .max(Float::compareTo)
                            .orElse(0f);

                    if (localMax > globalMax) {
                        globalMax = localMax;
                    }

//                    line.getStart().setX(startPoint.get(0, 0));
//                    line.getStart().setY(startPoint.get(1, 0));
//                    line.getStart().setZ(startPoint.get(2, 0));
//
//                    line.getEnd().setX(endPoint.get(0, 0));
//                    line.getEnd().setY(endPoint.get(1, 0));
//                    line.getEnd().setZ(endPoint.get(2, 0));
                }
            }

//            int index = 0;
//            for (ArrayList<Line<Point3D<Float, Float, Float>>> splineLines : linesOfSplines) {
//                BSpline bSpline = model.getbSplines().get(index);
//                float localMax = splineLines
//                        .stream()
//                        .flatMap(point3DLine -> ListUtil.asList(point3DLine.getStart().getX() + bSpline.getCx(),
//                                point3DLine.getEnd().getX() + bSpline.getCx(), point3DLine.getStart().getY() + bSpline.getCy(),
//                                point3DLine.getEnd().getY() + bSpline.getCy(), point3DLine.getStart().getZ() + bSpline.getCz(),
//                                point3DLine.getEnd().getZ() + bSpline.getCz()).stream())
//                        .map(Math::abs)
//                        .max(Float::compareTo)
//                        .orElse(Float.MIN_VALUE);
//
//                ++index;
//
//                if (localMax > globalMax) {
//                    globalMax = localMax;
//                }
//            }

            Matrix scaleMatrix = calculateZoomMatrix(1f / globalMax, 1f / globalMax, 1f / globalMax);

            for (int k = 0; k < model.getbSplines().size(); ++k) {
                Matrix moveMatrix = calculateShiftMatrix(model.getbSplines().get(k).getCx(),
                        model.getbSplines().get(k).getCy(), model.getbSplines().get(k).getCz());

                Matrix roundMatrix = model.getbSplines().get(k).getRoundMatrix();

                Matrix extendedRoundMatrix = new Matrix(new float[][] {
                        {roundMatrix.get(0, 0), roundMatrix.get(0, 1), roundMatrix.get(0,2 ), 0},
                        {roundMatrix.get(1, 0), roundMatrix.get(1, 1), roundMatrix.get(1,2 ), 0},
                        {roundMatrix.get(2, 0), roundMatrix.get(2, 1), roundMatrix.get(2,2 ), 0},
                        {0, 0, 0, 1}

                });

                Matrix shapeMatrix = MatrixUtil.multiply(scaleMatrix, MatrixUtil.multiply(extendedRoundMatrix, moveMatrix));

                for (Line<Point3D<Float, Float, Float>> splineLine : linesOfSplines.get(k)) {
                    drawLine(splineLine.getStart(), splineLine.getEnd(), model.getbSplines().get(k).getColor(), shapeMatrix);
                }
            }

            g.drawImage(bufferedImage, 0, 0, null);
        }
    }

    private void drawCoordinateSystem(float x, float y, float z, float length) {
        drawLine(new Point3D<>(x, y, z), new Point3D<>(x + length, y, z), Color.RED);
        drawLine(new Point3D<>(x, y, z), new Point3D<>(x, y + length, z), Color.GREEN);
        drawLine(new Point3D<>(x, y, z), new Point3D<>(x, y, z + length), Color.BLUE);

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
            drawLine(line.getStart(), line.getEnd(), Color.WHITE);
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
