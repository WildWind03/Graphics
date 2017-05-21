package ru.nsu.fit.g14201.chirikhin.view;

import chirikhin.clipper.Clipper3D;
import chirikhin.graphics.PixelCoordinateToAreaConverter;
import chirikhin.matrix.Matrix;
import chirikhin.matrix.MatrixUtil;
import chirikhin.support.*;
import chirikhin.support.Point;
import ru.nsu.fit.g14201.chirikhin.model.RenderSettings;
import ru.nsu.fit.g14201.chirikhin.model.SceneConfig;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class ShapeView extends JPanel {
    private static final float ZOOM_PLUS_RATIO = 1.1f;
    private static final float ZOOM_MINUS_RATIO = 0.9f;

    private final BufferedImage bufferedImage;
    private SceneConfig sceneConfig;
    private RenderSettings renderSettings;
    private int height;
    private int width;

    private Matrix sceneRotationMatrix = new Matrix(new float[][]{
            {1, 0, 0, 0},
            {0, 1, 0, 0},
            {0, 0, 1, 0},
            {0, 0, 0, 1}
    });

    private Point<Integer, Integer> prevPointScene = new chirikhin.support.Point<>(0, 0);

    public ShapeView(int defaultShapeViewWidth, int defaultShapeViewHeight) {
        super(true);
        setPreferredSize(new Dimension(defaultShapeViewWidth, defaultShapeViewHeight));
        this.width = defaultShapeViewWidth;
        this.height = defaultShapeViewHeight;


        bufferedImage = new BufferedImage(defaultShapeViewWidth, defaultShapeViewHeight, BufferedImage.TYPE_INT_RGB);

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);
                onMouseDragged(e);
                prevPointScene = new Point<>(e.getX(), e.getY());
            }
        });

        addMouseWheelListener(e -> {
            int notches = e.getWheelRotation();

            if (null != renderSettings) {
                if (notches < 0) {
                    if (renderSettings.getZf() == 0) {
                        renderSettings.setZf(0.1f);
                    }

                    renderSettings.setZf(renderSettings.getZf() * ZOOM_PLUS_RATIO);
                    repaint();
                } else {
                    renderSettings.setZf(renderSettings.getZf() * ZOOM_MINUS_RATIO);
                    repaint();
                }
            }
        });
    }

    public void setSceneConfig(SceneConfig sceneConfig) {
        this.sceneConfig = sceneConfig;

        repaint();
    }

    public void setRenderSettings(RenderSettings renderSettings) {
        this.renderSettings = renderSettings;

        repaint();
    }

    private void onMouseDragged(MouseEvent e) {
        if (SwingUtilities.isLeftMouseButton(e)) {
            int dx = (e.getX() - prevPointScene.getX());
            int dy = - (e.getY() - prevPointScene.getY());

            Matrix qzMatrix = calculateQzMatrix((float) (((float) dy / (float) height) * 2 * Math.PI));
            Matrix qyMatrix = calculateQyMatrix((float) (((float) dx / (float) width) * 2 * Math.PI));

            sceneRotationMatrix = MatrixUtil.multiply(qzMatrix,
                    MatrixUtil.multiply(qyMatrix, sceneRotationMatrix));
        }

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

    private Point3D<Float, Float, Float> crossProduct(Point3D<Float, Float, Float> vector1,
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

        Point3D<Float, Float, Float> iVector = crossProduct(upVector, new Point3D<>(kx, ky, kz));

        float normalizeForI = (float) Math.sqrt(iVector.getX() * iVector.getX() +
                iVector.getY() * iVector.getY() +
                iVector.getZ() * iVector.getZ());

        float ix = iVector.getX() / normalizeForI;
        float iy = iVector.getY() / normalizeForI;
        float iz = iVector.getZ() / normalizeForI;

        Point3D<Float, Float, Float> jVector = crossProduct(new Point3D<>(kx, ky, kz),
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
                {0, 0, zf / (zn - zf), zn * zf / (zn - zf)},
                {0, 0, -1, 0}
        });
    }

    private void drawLine(Point3D<Float, Float, Float> startPoint,
                          Point3D<Float, Float, Float> endPoint,
                          Color color) {
        Matrix start = new Matrix(new float[][]{{startPoint.getX()},
                {startPoint.getY()}, {startPoint.getZ()}, {1}});

        Matrix end = new Matrix(new float[][]{{endPoint.getX()},
                {endPoint.getY()}, {endPoint.getZ()}, {1}});

        Matrix realStart = MatrixUtil.multiply(sceneRotationMatrix, start);
        Matrix realEnd = MatrixUtil.multiply(sceneRotationMatrix, end);

        Matrix cameraMatrix = calculateCameraMatrix(new Point3D<>(-10f, 0f, 0f),
                new Point3D<>(10f, 0f, 0f),
                new Point3D<>(0f, 1f, 0f));

        Matrix projMatrix = calculateProjMatrix(renderSettings.getSw(),
                renderSettings.getSh(), renderSettings.getZf(), renderSettings.getZn());

        realStart = MatrixUtil.multiply(projMatrix,
                MatrixUtil.multiply(cameraMatrix, realStart));
        realEnd = MatrixUtil.multiply(projMatrix,
                MatrixUtil.multiply(cameraMatrix, realEnd));

        float x0 = realStart.get(0, 0) / realStart.get(3, 0);
        float y0 = realStart.get(1, 0) / realStart.get(3, 0);
        float z0 = realStart.get(2, 0) / realStart.get(3, 0);

        float x1 = realEnd.get(0, 0) / realEnd.get(3, 0);
        float y1 = realEnd.get(1, 0) / realEnd.get(3, 0);
        float z1 = realEnd.get(2, 0) / realEnd.get(3, 0);

        Clipper3D clipper3D = new Clipper3D(1, 1, 1, -1, -1, 0);

        Line<Point3D<Float, Float, Float>> clippedLine = clipper3D.getClippedLine(new Line<>
                (new Point3D<>(x0, y0, z0), new Point3D<>(x1, y1, z1)));

        if (null == clippedLine) {
            return;
        }

        PixelCoordinateToAreaConverter pixelCoordinateToAreaConverter =
                new PixelCoordinateToAreaConverter(-1, -1, 1, 1, width, height);

        Graphics2D g = bufferedImage.createGraphics();
        g.setColor(color);
        g.drawLine(pixelCoordinateToAreaConverter.toPixelX(clippedLine.getStart().getX()),
                height
                        - pixelCoordinateToAreaConverter.toPixelY(clippedLine.getStart().getY()),
                pixelCoordinateToAreaConverter.toPixelX(clippedLine.getEnd().getX()),
                height - pixelCoordinateToAreaConverter.toPixelY(clippedLine.getEnd().getY()));

        g.dispose();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = bufferedImage.createGraphics();
        g2d.setColor(Color.WHITE);

        g2d.fillRect(0, 0, width + 1, height + 1);
        g2d.dispose();

        if (null != renderSettings) {
            drawCube();
            drawCoordinateSystem(0, 0, 0, 1);
        }

        g.drawImage(bufferedImage, 0, 0, null);
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
            drawLine(line.getStart(), line.getEnd(), Color.BLACK);
        }
    }
}
