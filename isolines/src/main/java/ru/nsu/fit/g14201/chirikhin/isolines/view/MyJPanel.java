package ru.nsu.fit.g14201.chirikhin.isolines.view;

import com.chirikhin.interpolated_function.LinearInterpolator;
import ru.nsu.fit.g14201.chirikhin.isolines.function.DifficultFunctionSingleton;
import ru.nsu.fit.g14201.chirikhin.isolines.function.MyFunction;
import ru.nsu.fit.g14201.chirikhin.isolines.model.PixelCoordinateToAreaConverter;
import ru.nsu.fit.g14201.chirikhin.isolines.util.Util;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class MyJPanel extends JPanel {
    private static final double PART_OF_MAP_HEIGHT = 0.75;
    private static final double GAP_BETWEEN_LEGEND_AND_MAP = 0.05;
    private static final double PART_OF_LEGEND_HEIGHT = 0.2;
    private static final int MIN_HEIGHT = 10;
    private static final int MIN_WIDTH = 10;
    private static final int MIN_WIDTH_TO_PRINT_LEGENDS = 350;
    private static final int MIN_HEIGHT_TO_PRINT_LEGENDS = 400;

    private int startX = -2;
    private int endX = 2;
    private int startY = -4;
    private int endY = 4;

    private transient BufferedImage map;
    private transient BufferedImage legend;
    private transient BufferedImage legendRecords;

    private int width;
    private int height;

    private boolean isUpdated = false;

    private List<Integer[]> colors;

    private int m = 1;
    private int k = 1;
    private boolean isGridShown = false;
    private boolean isIsolinesDrawing = false;
    private int isolineColor;

    private final Color GRID_COLOR = Color.GRAY;
    private boolean isColorMapVisible;
    private boolean isInteractiveMode;
    private boolean isColorInterpolated;
    private int mapWidth;
    private int mapHeight;

    private Double drawnIsoline;

    private final transient MyFunction myFunction;
    private boolean isDynamicIsolineDrawingMode;
    private boolean isEnterPointDrawingMode;



    public MyJPanel(int width, int height) {
        //myFunction = (aDouble, aDouble2) -> Math.abs(aDouble + aDouble2);
        //myFunction = new X2Y2();
//        myFunction = (x, y) -> Math.sin(x * Math.cos(Math.PI / 4) - y * Math.sin(Math.PI / 4)) + Math.cos(x * Math.sin(Math.PI / 4) + y * Math.cos(Math.PI / 4));
        myFunction = DifficultFunctionSingleton.getInstance();
        MouseMotionAdapter mouseMotionAdapter = new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                super.mouseMoved(e);

                if (null != map && e.getX() < map.getWidth() && e.getY() < map.getHeight()) {

                    PixelCoordinateToAreaConverter pixelCoordinateToAreaConverter = new PixelCoordinateToAreaConverter(startX, startY, endX, endY, map.getWidth(), map.getHeight());
                    double realX = pixelCoordinateToAreaConverter.toRealX(e.getX());
                    double realY = pixelCoordinateToAreaConverter.toRealY(e.getY());
                    EventBusSingleton.getInstance().post(new FieldCoordinatesFunctionValue(realX, realY, myFunction.apply(realX, realY)));
                }
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);

                if (isDynamicIsolineDrawingMode && null != map && e.getX() < map.getWidth() && e.getY() < map.getHeight()) {
                    PixelCoordinateToAreaConverter pixelCoordinateToAreaConverter = new PixelCoordinateToAreaConverter(startX, startY, endX, endY, map.getWidth(), map.getHeight());
                    double realX = pixelCoordinateToAreaConverter.toRealX(e.getX());
                    double realY = pixelCoordinateToAreaConverter.toRealY(e.getY());

//                    drawnIsolines.add(myFunction.apply(realX, realY));
                    drawnIsoline = myFunction.apply(realX, realY);
                    isUpdated = true;
                    repaint();
                }
            }
        };
        addMouseMotionListener(mouseMotionAdapter);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (isInteractiveMode && null != map && e.getX() < map.getWidth() && e.getY() < map.getHeight()) {
                    PixelCoordinateToAreaConverter pixelCoordinateToAreaConverter = new PixelCoordinateToAreaConverter(startX, startY, endX, endY, map.getWidth(), map.getHeight());
                    double realX = pixelCoordinateToAreaConverter.toRealX(e.getX());
                    double realY = pixelCoordinateToAreaConverter.toRealY(e.getY());

//                    drawnIsolines.add(myFunction.apply(realX, realY));
                    drawnIsoline = myFunction.apply(realX, realY);
                    isUpdated = true;
                    repaint();
                }
            }
        });
        update(width, height);
    }

    public void update(int width, int height) {
        if (width < MIN_WIDTH || height < MIN_HEIGHT) {
            return;
        }

        this.width = width;
        this.height = height;

        int stepX = width / k;
        int stepY = ((int) (PART_OF_MAP_HEIGHT * this.height)) / m;
        this.mapWidth = stepX * k;
        this.mapHeight = stepY * m;

        setPreferredSize(new Dimension(this.width, this.height));
        isUpdated = true;
        repaint();
    }

    public void applyNewConfiguration(int m, int k, List<Integer[]> colors, Integer[] isolineColor) {
        this.colors = colors;

        this.drawnIsoline = null;

        this.m = m;
        this.k = k;

        this.isolineColor = new Color(isolineColor[0], isolineColor[1], isolineColor[2]).getRGB();
        update(this.width, this.height);
    }

    public void setGridShownMode(boolean isShown) {
        this.isGridShown = isShown;
        isUpdated = true;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (isUpdated) {
            setPreferredSize(new Dimension(this.width, this.height));


            map = new BufferedImage(mapWidth, mapHeight, BufferedImage.TYPE_INT_RGB);
            legend = new BufferedImage(this.width, (int) (this.height * PART_OF_LEGEND_HEIGHT), BufferedImage.TYPE_INT_RGB);
            legendRecords = new BufferedImage(this.width, (int) (this.height * GAP_BETWEEN_LEGEND_AND_MAP), BufferedImage.TYPE_INT_RGB);

            if (null != colors) {
                if (!isColorInterpolated) {
                    ColorFunction colorFunction = new ColorPaletteFunction(colors, legend.getWidth());
                    new ColorMapDrawer(colorFunction).draw(legend);
                } else {
                    ColorFunction colorFunction = new InterpolatedColorPaletteFunction(colors, new LinearInterpolator(), legend.getWidth());
                    new ColorMapDrawer(colorFunction).draw(legend);
                }

                PixelCoordinateToAreaConverter pixelCoordinateToAreaConverter =
                        new PixelCoordinateToAreaConverter(startX, startY, endX, endY, map.getWidth(), map.getHeight());
                List<Double> values = Util.getPointsByFunctionColorsAndArea(myFunction,
                        pixelCoordinateToAreaConverter,
                        colors.size());

                if (isColorMapVisible) {
                    if (isColorInterpolated) {
                        InterpolatedColorMapFunction func = new InterpolatedColorMapFunction(values,
                                colors,
                                myFunction,
                                pixelCoordinateToAreaConverter,
                                new LinearInterpolator());

                        new ColorMapDrawer(func).draw(map);
                    } else {
                        ColorFunction colorFunction = new ColorMapFunction(values, colors, myFunction, pixelCoordinateToAreaConverter);
                        new ColorMapDrawer(colorFunction).draw(map);
                    }
                }

                new ColorPaletteRecordsDrawer(values).draw(legendRecords);

                if (isGridShown) {
                    new GridMapDrawer(m, k, GRID_COLOR).draw(map);
                }

                if (isIsolinesDrawing) {
                    ArrayList<Double> drawnIsolineList = new ArrayList<>();
                    if (null != drawnIsoline) {
                        drawnIsolineList.add(drawnIsoline);
                    }
                    new IsolineDrawer(m, k, isolineColor, pixelCoordinateToAreaConverter, myFunction, values, isEnterPointDrawingMode).draw(map);
                    new IsolineDrawer(m, k, isolineColor, pixelCoordinateToAreaConverter, myFunction, drawnIsolineList, isEnterPointDrawingMode).draw(map);
                }
            }

            isUpdated = false;
        }

        g.drawImage(map, 0, 0, null);

        if (width > MIN_WIDTH_TO_PRINT_LEGENDS && height > MIN_HEIGHT_TO_PRINT_LEGENDS) {
            g.drawImage(legendRecords, 0, (int) (height * PART_OF_MAP_HEIGHT), null);
        }

        g.drawImage(legend, 0, (int) (height * (GAP_BETWEEN_LEGEND_AND_MAP + PART_OF_MAP_HEIGHT)), null);
    }

    public void setColorMapVisible(boolean colorMapVisible) {
        this.isColorMapVisible = colorMapVisible;
        isUpdated = true;
        repaint();
    }

    public void setInteractiveModeEnabled(boolean newInteractiveMode) {
        this.isInteractiveMode = newInteractiveMode;
        isUpdated = true;
    }

    public void setColorInterpolated(boolean colorInterpolated) {
        this.isColorInterpolated = colorInterpolated;
        isUpdated = true;
        repaint();
    }


    public void setEnterPointDrawingMode(boolean b) {
        this.isEnterPointDrawingMode = b;
        isUpdated = true;
        repaint();
    }

    public void updateSettings(int gridWidthDivisions,
                               int gridHeightDivisions,
                               int redColor,
                               int greenColor,
                               int blueColor,
                               int startX,
                               int startY,
                               int endX,
                               int endY) {

        this.startX = startX;
        this.startY = startY;

        this.endX = endX;
        this.endY = endY;
        this.m = gridWidthDivisions;
        this.k = gridHeightDivisions;

        this.isolineColor = new Color(redColor, greenColor, blueColor).getRGB();

        update(this.width, this.height);
    }

    public int getM() {
        return m;
    }

    public int getK() {
        return k;
    }

    public int getIsolineColor() {
        return isolineColor;
    }

    public int getStartX() {
        return startX;
    }

    public int getEndX() {
        return endX;
    }

    public int getStartY() {
        return startY;
    }

    public int getEndY() {
        return endY;
    }

    public void setDrawIsolinesMode(boolean isIsolinesDrawing) {
        this.isIsolinesDrawing = isIsolinesDrawing;
        isUpdated = true;
        repaint();
    }

    public void setDynamicIsolineDrawingMode(boolean b) {
        this.isDynamicIsolineDrawingMode = b;
    }
}
