package ru.nsu.fit.g14201.chirikhin.view;

import chirikhin.clipper.Clipper3D;
import chirikhin.graphics.PixelCoordinateToAreaConverter;
import chirikhin.matrix.Matrix;
import chirikhin.matrix.MatrixUtil;
import chirikhin.support.Line;
import chirikhin.support.MathSupport;
import chirikhin.support.Point;
import chirikhin.support.Point3D;
import ru.nsu.fit.g14201.chirikhin.geometry.GPlane;
import ru.nsu.fit.g14201.chirikhin.model.Quadrangle;
import ru.nsu.fit.g14201.chirikhin.model.RenderSettings;
import ru.nsu.fit.g14201.chirikhin.model.SceneConfig;
import ru.nsu.fit.g14201.chirikhin.model.Triangle;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class ShapeView extends JPanel {
    private static final float ZOOM_PLUS_RATIO = 1.1f;
    private static final float ZOOM_MINUS_RATIO = 0.9f;

    private BufferedImage bufferedImage;
    private SceneConfig sceneConfig;
    private RenderSettings renderSettings;
    private int height;
    private int width;

    private boolean isRendered = false;

    private List<Drawer> shapeDrawers = new ArrayList<>();

    private Matrix sceneRotationMatrix = new Matrix(new float[][]{
            {1, 0, 0, 0},
            {0, 1, 0, 0},
            {0, 0, 1, 0},
            {0, 0, 0, 1}
    });

    private Point<Integer, Integer> prevPointScene = new chirikhin.support.Point<>(0, 0);
    private PixelCoordinateToAreaConverter pixelCoordinateToAreaConverter;

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

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                if (SwingUtilities.isLeftMouseButton(e)) {
                    prevPointScene = new Point<>(e.getX(), e.getY());
                }
            }
        });

        addMouseWheelListener(e -> {
            int notches = e.getWheelRotation();

            if (null != renderSettings) {
                if (notches < 0) {
                    if (renderSettings.getZn() == 0) {
                        renderSettings.setZn(0.1f);
                    }

                    if (renderSettings.getZf() == 0) {
                        renderSettings.setZf(0.1f);
                    }

                    renderSettings.setZn(renderSettings.getZn() * ZOOM_PLUS_RATIO);
                    renderSettings.setZf(renderSettings.getZf() * ZOOM_PLUS_RATIO);
                    repaint();
                } else {
                    renderSettings.setZn(renderSettings.getZn() * ZOOM_MINUS_RATIO);
                    renderSettings.setZf(renderSettings.getZf() * ZOOM_MINUS_RATIO);
                    repaint();
                }
            }
        });
    }

    public void setSceneConfig(SceneConfig sceneConfig) {
        this.sceneConfig = sceneConfig;
        sceneConfig.getShapes().forEach(shape -> {
                    Drawer drawer = DrawerFactory.createDrawer(shape, Color.BLACK);
                    shapeDrawers.add(drawer);
                });

        repaint();
    }


    public void setRenderSettings(RenderSettings renderSettings) {
        this.renderSettings = renderSettings;

        Point3D<Float, Float, Float> cameraPoint = renderSettings.getCameraPoint();
        Point3D<Float, Float, Float> viewPoint = renderSettings.getViewPoint();
        Point3D<Float, Float, Float> upVector = renderSettings.getUpVector();

        Point3D<Float, Float, Float> z = MathSupport.minus(cameraPoint, viewPoint);
        Point3D<Float, Float, Float> right = MathSupport.crossProduct(z, upVector);
        Point3D<Float, Float, Float> newUpVector = MathSupport.crossProduct(right, z);
        this.renderSettings.setUpVector(newUpVector);
        updateSwShSettings();
        repaint();
    }

    public void updateSwShSettings() {
        if (null != renderSettings) {
            float sw = renderSettings.getSw();
            float sh = renderSettings.getSh();

            float necessaryK = sw / sh;
            float currentK = (float) bufferedImage.getWidth() / (float) bufferedImage.getHeight();

            if (currentK < necessaryK) {
                height = (int) (bufferedImage.getWidth() * sh / sw);
                width = bufferedImage.getWidth();
            }

            if (necessaryK < currentK) {
                width = (int) (bufferedImage.getHeight() * sw / sh);
                height = bufferedImage.getHeight();
            }

            Graphics2D graphics2D = bufferedImage.createGraphics();
            graphics2D.setColor(Color.LIGHT_GRAY);
            graphics2D.fillRect(0, 0, bufferedImage.getWidth(), bufferedImage.getHeight());
            graphics2D.dispose();
        }
    }

    private void onMouseDragged(MouseEvent e) {
        if (SwingUtilities.isLeftMouseButton(e)) {
            int dx = (e.getY() - prevPointScene.getY());
            int dy = - (e.getX() - prevPointScene.getX());

            Matrix qzMatrix = calculateQzMatrix((float) (((float) dy / (float) height) * 2 * Math.PI));
            Matrix qyMatrix = calculateQxMatrix((float) (((float) dx / (float) width) * 2 * Math.PI));

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

    private Matrix calculateCameraMatrix(Point3D<Float, Float, Float> cameraPosition,
                                         Point3D<Float, Float, Float> viewPoint,
                                         Point3D<Float, Float, Float> upVector) {
        float normalizeForK = (float) Math.sqrt((viewPoint.getX() - cameraPosition.getX()) * (viewPoint.getX() - cameraPosition.getX())
                + (cameraPosition.getY() - viewPoint.getY()) * (cameraPosition.getY() - viewPoint.getY()) +
                (cameraPosition.getZ() - viewPoint.getZ()) * (cameraPosition.getZ() - viewPoint.getZ()));

        float kx = (cameraPosition.getX() - viewPoint.getX()) /
                normalizeForK;
        float ky = (cameraPosition.getY() - viewPoint.getY()) /
                normalizeForK;
        float kz = (cameraPosition.getZ() - viewPoint.getZ()) /
                normalizeForK;

        Point3D<Float, Float, Float> iVector = MathSupport.crossProduct(upVector, new Point3D<>(kx, ky, kz));

        float normalizeForI = (float) Math.sqrt(iVector.getX() * iVector.getX() +
                iVector.getY() * iVector.getY() +
                iVector.getZ() * iVector.getZ());

        float ix = iVector.getX() / normalizeForI;
        float iy = iVector.getY() / normalizeForI;
        float iz = iVector.getZ() / normalizeForI;

        Point3D<Float, Float, Float> jVector = MathSupport.crossProduct(new Point3D<>(kx, ky, kz),
                new Point3D<>(ix, iy, iz));

        return MatrixUtil.multiply(new Matrix(new float[][]{
                {ix, iy, iz, 0},
                {jVector.getX(), jVector.getY(), jVector.getZ(), 0},
                {kx, ky, kz, 0},
                {0, 0, 0, 1}
        }), new Matrix(new float[][]{
                {1, 0, 0, -cameraPosition.getX()},
                {0, 1, 0, -cameraPosition.getY()},
                {0, 0, 1, -cameraPosition.getZ()},
                {0, 0, 0, 1}
        }));
    }

    private Matrix calculateToModelMatrix(Point3D<Float, Float, Float> cameraPosition,
                                          Point3D<Float, Float, Float> viewPoint,
                                          Point3D<Float, Float, Float> upVector) {
        float normalizeForK = (float) Math.sqrt((viewPoint.getX() - cameraPosition.getX()) * (viewPoint.getX() - cameraPosition.getX())
                + (cameraPosition.getY() - viewPoint.getY()) * (cameraPosition.getY() - viewPoint.getY()) +
                (cameraPosition.getZ() - viewPoint.getZ()) * (cameraPosition.getZ() - viewPoint.getZ()));

        float kx = (viewPoint.getX() - cameraPosition.getX()) /
                normalizeForK;
        float ky = (viewPoint.getY() - cameraPosition.getY()) /
                normalizeForK;
        float kz = (viewPoint.getZ() - cameraPosition.getZ()) /
                normalizeForK;

        Point3D<Float, Float, Float> iVector = MathSupport.crossProduct(upVector, new Point3D<>(kx, ky, kz));

        float normalizeForI = (float) Math.sqrt(iVector.getX() * iVector.getX() +
                iVector.getY() * iVector.getY() +
                iVector.getZ() * iVector.getZ());

        float ix = iVector.getX() / normalizeForI;
        float iy = iVector.getY() / normalizeForI;
        float iz = iVector.getZ() / normalizeForI;

        Point3D<Float, Float, Float> jVector = MathSupport.crossProduct(new Point3D<>(kx, ky, kz),
                new Point3D<>(ix, iy, iz));

        Matrix leftMatrix = new Matrix(new float[][]{
                {1, 0, 0, cameraPosition.getX()},
                {0, 1, 0, cameraPosition.getY()},
                {0, 0, 1, cameraPosition.getZ()},
                {0, 0, 0, 1}}
        );

        Matrix rightMatrix = new Matrix(new float[][] {
                {ix, jVector.getX(), kx, 0},
                {iy, jVector.getY(), ky, 0},
                {iz, jVector.getZ(), kz, 0},
                {0, 0, 0, 1}
        });

        return MatrixUtil.multiply(leftMatrix, rightMatrix);
    }

    private Matrix calculateProjMatrix(float sw, float sh, float zf, float zn) {
        return new Matrix(new float[][]{
                {2 * zn / sw, 0, 0, 0},
                {0, 2 * zn / sh, 0, 0},
                {0, 0, zn / (zf - zn), zf * zn / (zf - zn)},
                {0, 0, -1, 0}
        });
    }

    protected void drawLine(float startX, float startY, float startZ, float endX, float endY, float endZ, Color color) {
        Matrix start = new Matrix(new float[][]{{startX},
                {startY}, {startZ}, {1}});

        Matrix end = new Matrix(new float[][]{{endX},
                {endY}, {endZ}, {1}});

        Matrix realStart = MatrixUtil.multiply(sceneRotationMatrix, start);
        Matrix realEnd = MatrixUtil.multiply(sceneRotationMatrix, end);

        Matrix cameraMatrix = calculateCameraMatrix(new Point3D<>(renderSettings.getCameraPoint().getX(),
                        renderSettings.getCameraPoint().getY(), renderSettings.getCameraPoint().getZ()),
                new Point3D<>(renderSettings.getViewPoint().getX(), renderSettings.getViewPoint().getY(),
                        renderSettings.getViewPoint().getZ()),
                new Point3D<>(renderSettings.getUpVector().getX(), renderSettings.getUpVector().getY(),
                        renderSettings.getUpVector().getZ()));

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

        pixelCoordinateToAreaConverter = new PixelCoordinateToAreaConverter(-1, -1, 1, 1, width, height);

        Graphics2D g = bufferedImage.createGraphics();
        g.setColor(color);
        g.drawLine(pixelCoordinateToAreaConverter.toPixelX(clippedLine.getStart().getX()),
                pixelCoordinateToAreaConverter.toPixelY(clippedLine.getStart().getY()),
                pixelCoordinateToAreaConverter.toPixelX(clippedLine.getEnd().getX()),
                pixelCoordinateToAreaConverter.toPixelY(clippedLine.getEnd().getY()));

        g.dispose();
    }

    protected void drawLine(Point3D<Float, Float, Float> startPoint,
                          Point3D<Float, Float, Float> endPoint,
                          Color color) {
        drawLine(startPoint.getX(), startPoint.getY(), startPoint.getZ(),
                endPoint.getX(), endPoint.getY(), endPoint.getZ(), color);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (isRendered) {
            g.drawImage(bufferedImage, 0, 0, null);
            return;
        }

        Graphics2D g2d = bufferedImage.createGraphics();
        g2d.setColor(Color.WHITE);

        g2d.fillRect(0, 0, width + 1, height + 1);
        g2d.dispose();

        if (null != renderSettings) {
            drawCube();
            drawCoordinateSystem(0, 0, 0, 1);
        }

        shapeDrawers.forEach(drawer -> {
            drawer.draw(this);
        });

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

    public void render() {
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Matrix toModelMatrix = calculateToModelMatrix(renderSettings.getCameraPoint(),
                renderSettings.getViewPoint(),
                renderSettings.getUpVector());

        float clippingPlaneZ = renderSettings.getZn();
        float cameraX = 0;
        float cameraY = 0;
        float cameraZ = 0;

        float stepX = renderSettings.getSw() / width;
        float stepY = renderSettings.getSh() / height;

        float centerClippingPlaneX = cameraX;
        float centerClippingPlaneY = cameraY;

        float leftTopClippingPlaneX = centerClippingPlaneX - renderSettings.getSw() / 2;
        float leftTopClippingPlaneY = centerClippingPlaneY + renderSettings.getSh() / 2;

        for (int i = 0; i < height; ++i) {
            float currentPointClippingPlaneY = leftTopClippingPlaneY - i * stepY;
            float normalQualityY = currentPointClippingPlaneY - stepY / 2;

            for (int k = 0; k < width; ++k) {
                float currentPointClippingPlaneX = leftTopClippingPlaneX + k * stepX;
                float normalQualityX = currentPointClippingPlaneX + stepX / 2;

                Matrix start = new Matrix(new float[][]{{cameraX},
                        {cameraY}, {cameraZ}, {1}});

                Matrix end = new Matrix(new float[][]{{normalQualityX},
                        {normalQualityY}, {clippingPlaneZ}, {1}});

                Matrix firstPointInModel = MatrixUtil.multiply(toModelMatrix, start);
                Matrix endPointInModel = MatrixUtil.multiply(toModelMatrix, end);

                Point3D<Float, Float, Float> start3DInModel = new Point3D<>(firstPointInModel.get(0, 0),
                        firstPointInModel.get(1, 0), firstPointInModel.get(2, 0));

                Point3D<Float, Float, Float> endPoint3DInModel = new Point3D<>(endPointInModel.get(0, 0),
                        endPointInModel.get(1, 0), endPointInModel.get(2, 0));

                int finalK = k;
                int finalI = i;
                shapeDrawers.forEach(it -> {
                    Point3D<Float, Float, Float> crossPoint = null;
                    if (it instanceof QuadrangleDrawer) {
                        Quadrangle quadrangle = ((QuadrangleDrawer) (it)).getQuadrangle();
                        Triangle triangle1 = new Triangle(quadrangle.getPoint1(), quadrangle.getPoint2(), quadrangle.getPoint3(), quadrangle.getOpticalCharacteristics());
                        Triangle triangle2 = new Triangle(quadrangle.getPoint4(), quadrangle.getPoint1(), quadrangle.getPoint3(), quadrangle.getOpticalCharacteristics());

                        Point3D<Float, Float, Float> p1 = handleTriangle(triangle1, start3DInModel, endPoint3DInModel);
                        Point3D<Float, Float, Float> p2 = handleTriangle(triangle2, start3DInModel, endPoint3DInModel);

                        crossPoint = p1 == null ? p2 : p1;
                    }

                    if (it instanceof TriangleDrawer) {
                        Triangle triangle = ((TriangleDrawer) it).getTriangle();
                        crossPoint = handleTriangle(triangle, start3DInModel, endPoint3DInModel);
                    }

                    if (null != crossPoint) {
                        bufferedImage.setRGB(finalI, finalK, Color.RED.getRGB());
                    } else {
                        bufferedImage.setRGB(finalI, finalK, renderSettings.getBackgroundColor().getRGB());
                    }

                    //System.out.println(gPlane.getA() * gPlane.getA() + gPlane.getB() * gPlane.getB() + gPlane.getC() * gPlane.getC());
                });
            }
        }

        isRendered = true;
        this.bufferedImage = bufferedImage;
        repaint();
    }

    private Point3D<Float, Float, Float> handleTriangle(Triangle triangle, Point3D<Float, Float, Float> firstPointInModel,
                                Point3D<Float, Float, Float> endPointInModel) {
        GPlane gPlane = new GPlane(triangle.getPoint1(), triangle.getPoint2(), triangle.getPoint3()).normalize();
        Point3D<Float, Float, Float> normalToGPlane = MathSupport.crossProduct(
                MathSupport.createVector(triangle.getPoint1(), triangle.getPoint2()),
                MathSupport.createVector(triangle.getPoint1(), triangle.getPoint3()));

        float t = -(gPlane.getA() * firstPointInModel.getX()
                + gPlane.getB() * firstPointInModel.getY()
                + gPlane.getC() * firstPointInModel.getZ() + gPlane.getD()) /
                (gPlane.getA() * endPointInModel.getX()
                        + gPlane.getB() * endPointInModel.getY()
                        + gPlane.getC() * endPointInModel.getZ());

        float scalarMultiplicationResult = MathSupport.scalarMultiply(normalToGPlane, endPointInModel);

        if (scalarMultiplicationResult >= 0 || t < 0) {
            return new Point3D<>(firstPointInModel.getX() + endPointInModel.getX() * t,
                    firstPointInModel.getY() * endPointInModel.getY() * t,
                    firstPointInModel.getZ() * endPointInModel.getZ() * t);
        }

        return null;
    }
}
