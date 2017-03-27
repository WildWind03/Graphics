package ru.nsu.fit.g14201.chirikhin.filter.view;

import ru.nsu.fit.g14201.chirikhin.filter.controller.MyPoint;
import ru.nsu.fit.g14201.chirikhin.filter.model.*;
import ru.nsu.fit.g14201.chirikhin.function_visualization.OneValueFunctionInflater;
import ru.nsu.fit.g14201.chirikhin.util.ImageUtil;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.Arc2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;

public class MyJPanel extends JPanel {
    private static final int ZONE_SIZE = 350;

    private static final int GAP_BETWEEN_ZONES = 20;
    private static final String INVALID_FILE = "Invalid file";

    private int selectRectSize = ZONE_SIZE;
    private Point point;

    private boolean isSelectMode = false;

    private BufferedImage zoneA;
    private BufferedImage zoneB;
    private BufferedImage zoneC;

    private BufferedImage originalImage;
    private BufferedImage absorptionImage;
    private BufferedImage emissionImage;

    private LinkedList<MyPoint<Integer, Double>> absorptionPoints;
    private LinkedList<MyPoint<Integer, int[]>> emissionPoints;

    public MyJPanel() {
        setPreferredSize(new Dimension(ZONE_SIZE * 3 + GAP_BETWEEN_ZONES * 3, ZONE_SIZE + GAP_BETWEEN_ZONES));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                onClickOnImage(e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                point = null;
                repaint();
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);
                onClickOnImage(e);
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int rectSize = ZONE_SIZE + 1;

        ImageUtil.drawDashedRectangle(g, 0, 0, rectSize, rectSize);
        ImageUtil.drawDashedRectangle(g, rectSize + GAP_BETWEEN_ZONES, 0, rectSize, rectSize);
        ImageUtil.drawDashedRectangle(g, rectSize + rectSize + GAP_BETWEEN_ZONES + GAP_BETWEEN_ZONES, 0, rectSize, rectSize);

        if (null != zoneA) {
            g.drawImage(zoneA, 1, 1, null);
        }

        if (null != point) {
            ImageUtil.drawDashedRectangle(g, point.x, point.y, selectRectSize, selectRectSize);
        }

        if (null != zoneB) {
            g.drawImage(zoneB, 1 + ZONE_SIZE + GAP_BETWEEN_ZONES + 1, 1, null);
        }

        if (null != zoneC) {
            g.drawImage(zoneC, 3 + ZONE_SIZE + ZONE_SIZE + GAP_BETWEEN_ZONES + GAP_BETWEEN_ZONES, 1, null);
        }

        if (null != absorptionImage) {
            g.drawImage(absorptionImage, 1, ZONE_SIZE + GAP_BETWEEN_ZONES, null);
        }

        if (null != emissionImage) {
            g.drawImage(emissionImage, 1, ZONE_SIZE + GAP_BETWEEN_ZONES, null);
            //new OneValueFunctionInflater().paint(emissionImage, emissionPoints);
        }
    }

    public void loadNewImage(File file) throws IOException, LoadImageException {
        originalImage = ImageIO.read(file);

        if (null == originalImage) {
            throw new LoadImageException (INVALID_FILE);
        }

        int height = originalImage.getHeight();
        int width = originalImage.getWidth();

        int maxSize = height > width ? height : width;

        if (maxSize >= ZONE_SIZE) {
            int newWidth;
            int newHeight;

            if (width > height) {
                float k = (float) width / (float) ZONE_SIZE;

                newWidth = ZONE_SIZE;
                newHeight = (int) (height / k);

                selectRectSize = (int) (ZONE_SIZE / k) - 1;
            } else {
                float k = (float) height / (float) ZONE_SIZE;

                newHeight = ZONE_SIZE;
                newWidth = (int) (width / k);

                selectRectSize = (int) (ZONE_SIZE / k) - 1;
            }

            Image image = originalImage.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
            zoneA = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);

            Graphics2D g2d = zoneA.createGraphics();
            g2d.drawImage(image, 0, 0, null);
            g2d.dispose();
        }

        repaint();
    }

    public void setSelectMode(boolean selectMode) {
        isSelectMode = selectMode;
    }

    public boolean isSelectMode() {
        return isSelectMode;
    }

    private void onClickOnImage(MouseEvent e) {
        if (null != zoneA && isSelectMode()) {
            int startX = e.getX() - (int) ((float) selectRectSize / 2f);

            if (e.getX() + (float) selectRectSize / 2f > zoneA.getWidth()) {
                startX -= ((e.getX() + (float) selectRectSize / 2f - zoneA.getWidth()));
            }

            if (startX < 1) {
                startX = 1;
            }

            int startY = e.getY() - (int) ((float) selectRectSize / 2f);

            if (e.getY() + (float) selectRectSize / 2f > zoneA.getHeight()) {
                startY -= ((e.getY() + (float) selectRectSize / 2f - zoneA.getHeight()));
            }

            if (startY < 1) {
                startY = 1;
            }

            point = new Point(startX, startY);

            int xOriginalImage = e.getX() * originalImage.getWidth() / zoneA.getWidth();
            int yOriginalImage = e.getY() * originalImage.getHeight() / zoneA.getHeight();

            int startXOriginal = xOriginalImage - ZONE_SIZE / 2;
            if (startXOriginal < 0) {
                startXOriginal = 0;
            }

            if (xOriginalImage + ZONE_SIZE / 2 > originalImage.getWidth()) {
                startXOriginal -= (xOriginalImage + ZONE_SIZE / 2 - originalImage.getWidth());
            }

            int startYOriginal = yOriginalImage - ZONE_SIZE / 2;
            if (startYOriginal < 0) {
                startYOriginal = 0;
            }

            if (yOriginalImage + ZONE_SIZE / 2 > originalImage.getHeight()) {
                startYOriginal -= (yOriginalImage + ZONE_SIZE / 2 - originalImage.getHeight());
            }

            zoneB = ImageUtil.getSubimage(originalImage, startXOriginal, startYOriginal, ZONE_SIZE, ZONE_SIZE);
            repaint();
        }
    }

    public void clear() {
        zoneA = null;
        zoneB = null;
        zoneC = null;

        originalImage = null;
    }

    public void copyCToB() {
        zoneB = ImageUtil.deepCopy(zoneC);
    }

    public void copyBToC() {
        zoneC = ImageUtil.deepCopy(zoneB);
    }

    public BufferedImage getFilteredImage() {
        return zoneC;
    }

    public void applyGrayscaleFilter() {
        if (null != zoneB) {
            OnePixelFilter grayscaleFilter = new OnePixelFilter(new GrayscaleFunctor());
            zoneC = grayscaleFilter.apply(zoneB);
            repaint();
        }
    }

    public void applyNegativeFilter() {
        if (null != zoneB) {
            zoneC = new OnePixelFilter(new NegativeFunctor()).apply(zoneB);
            repaint();
        }
    }

    public void applyBlurFilter() {
        if (null != zoneB) {
            zoneC = new MatrixFilter(new BlurMatrix(5, 2)).apply(zoneB);
            repaint();
        }
    }

    public void applySharpFilter() {
        if (null != zoneB) {
            zoneC = new MatrixFilter(new SharpMatrix(5)).apply(zoneB);
            repaint();
        }
    }

    public void applyEmbossFilter() {

        if (null != zoneB) {
            MatrixFilter embossFilter = new MatrixFilter(new EmbossMatrix());
            OnePixelFilter brightnessShiftFilter = new OnePixelFilter(new BrightnessShiftFunctor());
            CompositeFilter embossWithShiftFilter = new CompositeFilter(Arrays.asList(embossFilter, brightnessShiftFilter));

            zoneC = embossWithShiftFilter.apply(zoneB);
            repaint();
        }
    }

    public void applyOrderedDitheringFilter() {
        if (null != zoneB) {
            zoneC = new OrderedDitheringFilter().apply(zoneB);
            repaint();
        }
    }

    public void applyFloydDitheringClicked(int redDivision, int greenDivision, int blueDivision) {
        if (null != zoneB) {
            zoneC = new FloydDitheringFilter(redDivision, greenDivision, blueDivision).apply(zoneB);
            repaint();
        }
    }

    public void applyZoomFilter() {
        if (null != zoneB) {
            zoneC = new ZoomFilter().apply(zoneB);
            repaint();
        }
    }

    public void applyRobertsFilter(int threshold) {
        if (null != zoneB) {
            zoneC = new RobertsFilter(threshold).apply(zoneB);
            repaint();
        }
    }

    public void applySobelFilter(int value) {
        if (null != zoneB) {
            zoneC = new SobelFilter(value).apply(zoneB);
            repaint();
        }
    }

    public void applyWatercolorFilter() {
        if (null != zoneB) {
            LinkedList<BaseFilter> filters = new LinkedList<>();
            filters.add(new WatercolorFilter());
            filters.add(new MatrixFilter(new SharpMatrix(3)));
            zoneC = new CompositeFilter(filters).apply(zoneB);
            repaint();
        }
    }

    public void applyRotationFilter(int value) {
        if (null != zoneB) {
            zoneC = new RotationFilter(value).apply(zoneB);
            repaint();
        }
    }

    public void applyGammaFilter(int value) {
        if (null != zoneB) {
            zoneC = new GammaFilter(value).apply(zoneB);
            repaint();
        }
    }

    public void applyVisualizationFilter(LinkedList<MyPoint<Integer, Float>> absorptionPoints, LinkedList<int[]> emissionPoints, LinkedList<int[]> chargePoints) {

    }

    public void applyGraphicBuilding(LinkedList<MyPoint<Integer, Double>> absorptionPoints, LinkedList<int[]> emissionPoints, LinkedList<int[]> chargePoints) {
        if (null != absorptionPoints) {
            absorptionImage = new BufferedImage(500, 200, BufferedImage.TYPE_INT_RGB);

            Graphics2D graphics2D = absorptionImage.createGraphics();
            graphics2D.setColor(Color.WHITE);
            graphics2D.fillRect(0, 0, absorptionImage.getWidth(), absorptionImage.getHeight());
            graphics2D.dispose();

            /*Graphics2D graphics2D1 = absorptionImage.createGraphics();
            graphics2D1.setColor(Color.BLACK);
            graphics2D1.drawLine(0, 0, 200, 200);
            graphics2D1.dispose();*/

            //absorptionImage.getGraphics().setColor(Color.BLACK);
            //((Graphics2D) absorptionImage.getGraphics()).drawLine(100, 100, 0, 200);
            //emissionImage = new BufferedImage(500, 200, BufferedImage.TYPE_INT_RGB);
            OneValueFunctionInflater functionInflater = new OneValueFunctionInflater();
            functionInflater.paint(absorptionImage, absorptionPoints);
        }
    }
}
