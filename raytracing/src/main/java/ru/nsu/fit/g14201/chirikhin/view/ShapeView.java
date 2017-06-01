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
import ru.nsu.fit.g14201.chirikhin.model.*;

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
    private IntegerRunnable integerRunnable;
    private RenderEventHandler renderEventHandler;

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
        if (isRendered) {
            return;
        }

        if (SwingUtilities.isLeftMouseButton(e)) {
            int dx = -(e.getY() - prevPointScene.getY());
            int dy = -(e.getX() - prevPointScene.getX());

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

        Matrix rightMatrix = new Matrix(new float[][]{
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
        g.drawLine(width - pixelCoordinateToAreaConverter.toPixelX(clippedLine.getStart().getX()),
                pixelCoordinateToAreaConverter.toPixelY(clippedLine.getStart().getY()),
                width - pixelCoordinateToAreaConverter.toPixelX(clippedLine.getEnd().getX()),
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

        if (renderSettings == null) {
            return;
        }

        if (isRendered) {
            g.drawImage(bufferedImage, 0, 0, null);
            return;
        }

        Graphics2D g2d = bufferedImage.createGraphics();
        g2d.setColor(Color.WHITE);

        g2d.fillRect(0, 0, width + 1, height + 1);
        g2d.dispose();

//        if (null != renderSettings) {
//            drawCube();
//            drawCoordinateSystem(0, 0, 0, 1);
//        }

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

    private Point3D<Float, Float, Float> getIntensity(Point3D<Float, Float, Float> start3DInModel, Point3D<Float, Float, Float> endPoint3DInModel, int depth) {
        Point3D<Float, Float, Float> nearestCrossPoint = null;
        GPlane gPlane = null;
        OpticalCharacteristics opticalCharacteristics = null;
        Point3D<Float, Float, Float> normal = null;

        for (int counter = 0; counter < shapeDrawers.size(); ++counter) {
            Point3D<Float, Float, Float> crossPoint = null;
            GPlane currentPlane = null;
            OpticalCharacteristics currentOpticalCharacteristics = null;
            Point3D<Float, Float, Float> currentNormal = null;

            if (shapeDrawers.get(counter) instanceof QuadrangleDrawer) {
                Quadrangle quadrangle = ((QuadrangleDrawer) (shapeDrawers.get(counter))).getQuadrangle();
                Triangle triangle1 = new Triangle(quadrangle.getPoint1(), quadrangle.getPoint2(), quadrangle.getPoint3(), quadrangle.getOpticalCharacteristics());
                Triangle triangle2 = new Triangle(quadrangle.getPoint4(), quadrangle.getPoint1(), quadrangle.getPoint3(), quadrangle.getOpticalCharacteristics());

                Point3D<Float, Float, Float> p1 = handleTriangle(triangle1, start3DInModel, endPoint3DInModel);
                Point3D<Float, Float, Float> p2 = handleTriangle(triangle2, start3DInModel, endPoint3DInModel);
                currentPlane = new GPlane(triangle1.getPoint1(), triangle1.getPoint2(), triangle1.getPoint3());
                currentNormal = MathSupport.crossProduct(MathSupport.createVector(triangle1.getPoint1(), triangle1.getPoint2()),
                        MathSupport.createVector(triangle1.getPoint1(), triangle1.getPoint3()));
                currentNormal = normalizePoint(currentNormal);
                currentOpticalCharacteristics = triangle1.getOpticalCharacteristics();

                crossPoint = p1 == null ? p2 : p1;

                if (null == crossPoint) {
                    continue;
                }

                if (!MathSupport.isInTriangle(triangle1, crossPoint) && !MathSupport.isInTriangle(triangle2, crossPoint)) {
                    continue;
                }
            }

            if (shapeDrawers.get(counter) instanceof TriangleDrawer) {
                Triangle triangle = ((TriangleDrawer) shapeDrawers.get(counter)).getTriangle();
                crossPoint = handleTriangle(triangle, start3DInModel, endPoint3DInModel);
                currentPlane = new GPlane(triangle.getPoint1(), triangle.getPoint2(), triangle.getPoint3());
                currentNormal = MathSupport.crossProduct(MathSupport.createVector(triangle.getPoint1(), triangle.getPoint2()),
                        MathSupport.createVector(triangle.getPoint1(), triangle.getPoint3()));
                currentNormal = normalizePoint(currentNormal);
                currentOpticalCharacteristics = triangle.getOpticalCharacteristics();

                if (null == crossPoint) {
                    continue;
                }

                if (!MathSupport.isInTriangle(triangle, crossPoint)) {
                    continue;
                }
            }

            if (null == nearestCrossPoint) {
                nearestCrossPoint = crossPoint;
                opticalCharacteristics = currentOpticalCharacteristics;
                gPlane = currentPlane;
                normal = currentNormal;
            } else {
                float oldDistance = MathSupport.getDistance(nearestCrossPoint, start3DInModel);
                float currentDistance = MathSupport.getDistance(crossPoint, start3DInModel);

                if (currentDistance < oldDistance) {
                    nearestCrossPoint = crossPoint;
                    gPlane = currentPlane;
                    normal = currentNormal;
                    opticalCharacteristics = currentOpticalCharacteristics;
                }
            }
        }

        if (null == gPlane) {
            return renderSettings.getBackgroundColorAsPoint();
        } else {

            if (depth < renderSettings.getDepth()) {
                Point3D<Float, Float, Float> e = new Point3D<>((nearestCrossPoint.getX() - start3DInModel.getX()),
                        (nearestCrossPoint.getY() - start3DInModel.getY()), (nearestCrossPoint.getZ() - start3DInModel.getZ()));
                e = normalizePoint(e);

                float reflectPoint = MathSupport.scalarMultiply(normal, e);

                Point3D<Float, Float, Float> reflectRay = new Point3D<>((e.getX() - normal.getX() * 2 * reflectPoint),
                         (e.getY() - normal.getY() * 2 * reflectPoint), (e.getZ() - normal.getZ() * 2 * reflectPoint));

                reflectRay = normalizePoint(reflectRay);
                Point3D<Float, Float, Float> newPoint = new Point3D<>(reflectRay.getX() + nearestCrossPoint.getX(),
                        reflectRay.getY() + nearestCrossPoint.getY(), reflectRay.getZ() + nearestCrossPoint.getZ());

                Point3D<Float, Float, Float> intensity = getIntensity(nearestCrossPoint, newPoint, depth + 1);
                Point3D<Float, Float, Float> fixedIntensity = new Point3D<>((intensity.getX() * opticalCharacteristics.getMirrorReflectRate().getX()),
                        (intensity.getY() * opticalCharacteristics.getMirrorReflectRate().getY()),
                        (intensity.getZ() * opticalCharacteristics.getMirrorReflectRate().getZ()));

                Point3D<Float, Float, Float> firstTerm = new Point3D<>(sceneConfig.getSpaceColorAsPoint().getX() * opticalCharacteristics.getDiffuseReflectRate().getX(),
                        sceneConfig.getSpaceColorAsPoint().getY() * opticalCharacteristics.getDiffuseReflectRate().getY(),
                        sceneConfig.getSpaceColorAsPoint().getZ() * opticalCharacteristics.getDiffuseReflectRate().getZ());

                return new Point3D<>(firstTerm.getX() + fixedIntensity.getX(), firstTerm.getY() + fixedIntensity.getY(),
                        firstTerm.getZ() + fixedIntensity.getZ());
            } else {
                float red = sceneConfig.getSpaceColorAsPoint().getX() * opticalCharacteristics.getDiffuseReflectRate().getX();
                float green = sceneConfig.getSpaceColorAsPoint().getY() * opticalCharacteristics.getDiffuseReflectRate().getY();
                float blue = sceneConfig.getSpaceColorAsPoint().getZ() * opticalCharacteristics.getDiffuseReflectRate().getZ();
                return new Point3D<>(red, green, blue);
            }
        }
    }

    public void render() {
        if (null == sceneConfig || null == renderSettings) {
            JOptionPane.showMessageDialog(this, "You should load scene and render settings before rendering!");
            return;
        }

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

        float leftTopClippingPlaneX = cameraX - renderSettings.getSw() / 2;
        float leftTopClippingPlaneY = cameraY + renderSettings.getSh() / 2;

        Matrix start = new Matrix(new float[][]{{cameraX},
                {cameraY}, {cameraZ}, {1}});

        Matrix firstPointInModel = MatrixUtil.multiply(toModelMatrix, start);

        Point3D<Float, Float, Float> start3DInModel = new Point3D<>(firstPointInModel.get(0, 0),
                firstPointInModel.get(1, 0), firstPointInModel.get(2, 0));

        renderEventHandler.run(RenderEvent.START);

        new Thread(() -> {
            for (int i = 0; i < height; ++i) {
                float currentPointClippingPlaneY = leftTopClippingPlaneY - i * stepY;
                float normalQualityY = currentPointClippingPlaneY - stepY / 2;

                for (int k = 0; k < width; ++k) {
                    float currentPointClippingPlaneX = leftTopClippingPlaneX + k * stepX;
                    float normalQualityX = currentPointClippingPlaneX + stepX / 2;

                    Matrix end = new Matrix(new float[][]{{normalQualityX},
                            {normalQualityY}, {clippingPlaneZ}, {1}});

                    Matrix endPointInModel = MatrixUtil.multiply(toModelMatrix, end);


                    Point3D<Float, Float, Float> endPoint3DInModel = new Point3D<>(endPointInModel.get(0, 0),
                            endPointInModel.get(1, 0), endPointInModel.get(2, 0));

                    Point3D<Float, Float, Float> intensity = getIntensity(start3DInModel, endPoint3DInModel, 1);

                    float red  = intensity.getX();
                    float green = intensity.getY();
                    float blue = intensity.getZ();

                    float max = MathSupport.getMax(red, green, blue);

                    if (max > 1) {
                        red /= max;
                        green /= max;
                        blue /= max;
                    }

                    red *= 255;
                    green *= 255;
                    blue *= 255;

                    red = Math.min(red, 255);
                    blue = Math.min(blue, 255);
                    green = Math.min(green, 255);

                    bufferedImage.setRGB(k, i, new Color((int) red, (int) green, (int) blue).getRGB());
                    int finalI = i;
                    int finalK = k;
                    SwingUtilities.invokeLater(() -> {
                        integerRunnable.run((int) (((float) (finalI * width + finalK) / (float) (width * height)) * 100));
                    });
                }
            }

            isRendered = true;
            this.bufferedImage = bufferedImage;
            renderEventHandler.run(RenderEvent.END);
            SwingUtilities.invokeLater(this::repaint);

        }).start();
    }

    public void setOnRenderUpdateListener(IntegerRunnable integerRunnable) {
        this.integerRunnable = integerRunnable;
    }

    public void setRenderEventHandler(RenderEventHandler renderEventHandler) {
        this.renderEventHandler = renderEventHandler;
    }

    private Point3D<Float, Float, Float> handleTriangle(Triangle triangle, Point3D<Float, Float, Float> firstPointInModel,
                                                        Point3D<Float, Float, Float> endPointInModel) {
        GPlane gPlane = new GPlane(triangle.getPoint1(), triangle.getPoint2(), triangle.getPoint3()).normalize();
        endPointInModel = MathSupport.createVector(firstPointInModel, endPointInModel);

        float lengthOfEndPointInModel = (float) Math.sqrt(endPointInModel.getX() * endPointInModel.getX()
                + endPointInModel.getY() * endPointInModel.getY() + endPointInModel.getZ() * endPointInModel.getZ());

        Point3D<Float, Float, Float> normalizedEndPointInModel = new Point3D<>(endPointInModel.getX() / lengthOfEndPointInModel,
                endPointInModel.getY() / lengthOfEndPointInModel, endPointInModel.getZ() / lengthOfEndPointInModel);


        Point3D<Float, Float, Float> normalToGPlane = new Point3D<>(gPlane.getA(), gPlane.getB(), gPlane.getC());

        float t = -(gPlane.getA() * firstPointInModel.getX()
                + gPlane.getB() * firstPointInModel.getY()
                + gPlane.getC() * firstPointInModel.getZ() + gPlane.getD()) /
                (gPlane.getA() * normalizedEndPointInModel.getX()
                        + gPlane.getB() * normalizedEndPointInModel.getY()
                        + gPlane.getC() * normalizedEndPointInModel.getZ());

        float scalarMultiplicationResult = MathSupport.scalarMultiply(normalToGPlane, endPointInModel);

        if (scalarMultiplicationResult < 0 || t >= 0) {
            return new Point3D<>(firstPointInModel.getX() + normalizedEndPointInModel.getX() * t,
                    firstPointInModel.getY() + normalizedEndPointInModel.getY() * t,
                    firstPointInModel.getZ() + normalizedEndPointInModel.getZ() * t);
        }

        return null;
    }

    private Point3D<Float, Float, Float> normalizePoint(Point3D<Float, Float, Float> p) {
        float normalizeRate = (float) Math.sqrt(p.getX() * p.getX() + p.getY() * p.getY() + p.getZ() * p.getZ());
        return new Point3D<>(p.getX() / normalizeRate, p.getY() / normalizeRate, p.getZ() / normalizeRate);
    }

    public void wireMode() {
        isRendered = false;
        repaint();
    }
}
