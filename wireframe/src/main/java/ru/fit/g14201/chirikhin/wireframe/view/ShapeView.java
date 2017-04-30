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
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.function.Function;
import java.util.stream.Stream;

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

    ;

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
            //drawShapes(model.getbSplines());
            float max = model.getbSplines()
                    .stream()
                    .flatMap(bSpline -> ShapeToLinesConverter.toLines(new BSplineFunction(bSpline.getPoints()),
                            model.getN(), model.getM(), model.getK(), model.getA(), model.getB(), model.getD(), model.getC(), -1).stream())
                    .flatMap(point3DLine -> ListUtil.asList(point3DLine.getStart().getX(),
                            point3DLine.getEnd().getX()).stream())
                    .map(Math::abs)
                    .max(Float::compareTo)
                    .orElse(Float.MIN_VALUE);

            for (BSpline BSpline : model.getbSplines()) {
                drawShape(BSpline, max);
            }
        }

        g.drawImage(bufferedImage, 0, 0, null);
    }

    /*private void drawShapes(ArrayList<BSpline> bSplines) {
        float max = bSplines
                .stream()
                .flatMap(bSpline -> ShapeToLinesConverter.toLines(new BSplineFunction(bSpline.getPoints()),
                        model.getN(), model.getM(), model.getK(), model.getA(), model.getB(), model.getD(), model.getC()).stream())
                .flatMap(point3DLine -> ListUtil.asList(point3DLine.getStart().getX(),
                point3DLine.getEnd().getX()).stream())
                .map(Math::abs)
                .max(Float::compareTo)
                .orElse(Float.MIN_VALUE);

        for (BSpline bSpline : bSplines) {
            drawShape(bSpline);
        }
        /*float globalMaxX = Float.MIN_VALUE;
        float globalMaxY = Float.MIN_VALUE;
        float globalMaxZ = Float.MIN_VALUE;
        float globalMinX = Float.MAX_VALUE;
        float globalMinY = Float.MAX_VALUE;
        float globalMinZ = Float.MAX_VALUE;

        ArrayList<ArrayList<Line<Point3D<Float, Float, Float>>>> splinesLines = new ArrayList<>();

        for (BSpline bSpline : bSplines) {
            BSplineFunction bSplineFunction = new BSplineFunction(bSpline.getPoints());
            ArrayList<Line<Point3D<Float, Float, Float>>> shapeLines =
                    ShapeToLinesConverter.toLines(bSplineFunction, model.getN(), model.getM(), model.getK(),
                            model.getA(), model.getB(), model.getD(), model.getC());

            float maxXValue = shapeLines
                    .stream()
                    .flatMap(point3DLine -> ListUtil.asList(point3DLine.getStart().getX(),
                            point3DLine.getEnd().getX()).stream())
                    .max(Float::compareTo)
                    .orElse(Float.MIN_VALUE) + bSpline.getCx();

            float maxYValue = shapeLines
                    .stream()
                    .flatMap(point3DLine -> ListUtil.asList(point3DLine.getStart().getY(),
                            point3DLine.getEnd().getY()).stream())
                    .max(Float::compareTo)
                    .orElse(Float.MIN_VALUE) + bSpline.getCy();

            float maxZValue = shapeLines
                    .stream()
                    .flatMap(point3DLine -> ListUtil.asList(point3DLine.getStart().getZ(),
                            point3DLine.getEnd().getZ()).stream())
                    .max(Float::compareTo)
                    .orElse(Float.MIN_VALUE) + bSpline.getCz();

            float minXValue = shapeLines
                    .stream()
                    .flatMap(point3DLine -> ListUtil.asList(point3DLine.getStart().getX(),
                            point3DLine.getEnd().getX()).stream())
                    .min(Float::compareTo)
                    .orElse(Float.MIN_VALUE) + bSpline.getCx();

            float minYValue = shapeLines
                    .stream()
                    .flatMap(point3DLine -> ListUtil.asList(point3DLine.getStart().getY(),
                            point3DLine.getEnd().getY()).stream())
                    .min(Float::compareTo)
                    .orElse(Float.MIN_VALUE) + bSpline.getCy();

            float minZValue = shapeLines
                    .stream()
                    .flatMap(point3DLine -> ListUtil.asList(point3DLine.getStart().getZ(),
                            point3DLine.getEnd().getZ()).stream())
                    .min(Float::compareTo)
                    .orElse(Float.MIN_VALUE) + bSpline.getCz();

            splinesLines.add(shapeLines);
        }

    }*/

    private void drawCoordinateSystem(float x, float y, float z, float length) {
        drawLine(new Point3D<>(x, y, z), new Point3D<>(x + length, y, z), Color.RED);
        drawLine(new Point3D<>(x, y, z), new Point3D<>(x, y + length, z), Color.GREEN);
        drawLine(new Point3D<>(x, y, z), new Point3D<>(x, y, z + length), Color.BLUE);

    }

    private void drawShape(BSpline BSpline, float max) {
        BSplineFunction bSplineFunction = new BSplineFunction(BSpline.getPoints());
        ArrayList<Line<Point3D<Float, Float, Float>>> shapeLines =
                ShapeToLinesConverter.toLines(bSplineFunction, model.getN(), model.getM(), model.getK(),
                        model.getA(), model.getB(), model.getD(), model.getC(), max);

        /*float maxXValue = shapeLines
                .stream()
                .flatMap(point3DLine -> ListUtil.asList(point3DLine.getStart().getX(),
                        point3DLine.getEnd().getX()).stream())
                .max(Float::compareTo)
                .orElse(Float.MIN_VALUE);

        float maxYValue = shapeLines
                .stream()
                .flatMap(point3DLine -> ListUtil.asList(point3DLine.getStart().getY(),
                        point3DLine.getEnd().getY()).stream())
                .max(Float::compareTo)
                .orElse(Float.MIN_VALUE);

        float maxZValue = shapeLines
                .stream()
                .flatMap(point3DLine -> ListUtil.asList(point3DLine.getStart().getZ(),
                        point3DLine.getEnd().getZ()).stream())
                .max(Float::compareTo)
                .orElse(Float.MIN_VALUE);

        float minXValue = shapeLines
                .stream()
                .flatMap(point3DLine -> ListUtil.asList(point3DLine.getStart().getX(),
                        point3DLine.getEnd().getX()).stream())
                .min(Float::compareTo)
                .orElse(Float.MIN_VALUE);

        float minYValue = shapeLines
                .stream()
                .flatMap(point3DLine -> ListUtil.asList(point3DLine.getStart().getY(),
                        point3DLine.getEnd().getY()).stream())
                .min(Float::compareTo)
                .orElse(Float.MIN_VALUE);

        float minZValue = shapeLines
                .stream()
                .flatMap(point3DLine -> ListUtil.asList(point3DLine.getStart().getZ(),
                        point3DLine.getEnd().getZ()).stream())
                .min(Float::compareTo)
                .orElse(Float.MIN_VALUE);

        float xDistance = Math.abs(maxXValue - minXValue);
        float yDistance = Math.abs(maxYValue - minYValue);
        float zDistance = Math.abs(maxZValue - minZValue);

        Matrix moveMatrix = calculateShiftMatrix(- (xDistance / 2 + minXValue), - (yDistance / 2 + minYValue), - (zDistance / 2 + minZValue));
        Matrix zoomMatrix = calculateZoomMatrix(2f / xDistance, 2f / yDistance, 2f / zDistance);

*/
        for (Line<Point3D<Float, Float, Float>> line : shapeLines) {
            //drawLine(line.getStart(), line.getEnd(), BSpline.getColor(), MatrixUtil.multiply(zoomMatrix, moveMatrix));
            drawLine(line.getStart(), line.getEnd(), BSpline.getColor());
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
