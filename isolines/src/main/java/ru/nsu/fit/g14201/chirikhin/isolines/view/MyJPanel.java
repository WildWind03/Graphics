package ru.nsu.fit.g14201.chirikhin.isolines.view;

import com.chirikhin.interpolated_function.LinearInterpolator;
import ru.nsu.fit.g14201.chirikhin.isolines.function.DifficultFunctionSingleton;
import ru.nsu.fit.g14201.chirikhin.isolines.model.PixelCoordinateToAreaConverter;
import ru.nsu.fit.g14201.chirikhin.isolines.util.Util;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.util.List;

public class MyJPanel extends JPanel {
    private static final double PART_OF_MAP_HEIGHT = 0.75;
    private static final double GAP_BETWEEN_LEGEND_AND_MAP = 0.05;
    private static final double PART_OF_LEGEND_HEIGHT = 0.2;
    private static final int MIN_HEIGHT = 10;
    private static final int MIN_WIDTH = 10;
    private static final int MIN_WIDTH_TO_PRINT_LEGENDS = 350;
    private static final int MIN_HEIGHT_TO_PRINT_LEGENDS = 400;

    private static final int START_X = -2;
    private static final int END_X = 2;
    private static final int START_Y = -4;
    private static final int END_Y = 4;

    private BufferedImage map;
    private BufferedImage legend;
    private BufferedImage legendRecords;

    private int width;
    private int height;

    private boolean isUpdated = false;

    private List<Integer[]> colors;

    private int m;
    private int k;
    private boolean isGridShown = false;
    private int isolineColor;

    private final Color GRID_COLOR = Color.GRAY;
    private boolean colorMapVisibility;
    private boolean interaciveMode;
    private boolean colorInterpolationModeEnabled;


    public MyJPanel(int width, int height) {
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                super.mouseMoved(e);

                if (null != map && e.getX() < map.getWidth() && e.getY() < map.getHeight()) {
                    PixelCoordinateToAreaConverter pixelCoordinateToAreaConverter =
                            new PixelCoordinateToAreaConverter(START_X, START_Y, END_X, END_Y, map.getWidth(), map.getHeight());

                    double realX = pixelCoordinateToAreaConverter.toRealX(e.getX());
                    double realY = pixelCoordinateToAreaConverter.toRealY(e.getY());
                    EventBusSingleton.getInstance().post(new FieldCoordinatesFunctionValue(realX, realY, DifficultFunctionSingleton.getInstance().apply(realX, realY)));
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

        setPreferredSize(new Dimension(this.width, this.height));
        isUpdated = true;
        repaint();
    }

    public void applyNewConfiguration(int m, int k, List<Integer[]> colors, Integer[] isolineColor) {
        this.colors = colors;
        isUpdated = true;

        this.m = m;
        this.k = k;

        this.isolineColor = new Color(isolineColor[0], isolineColor[1], isolineColor[2]).getRGB();
        repaint();
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

            int mapHeight = (int) (PART_OF_MAP_HEIGHT * this.height);


            map = new BufferedImage(this.width, mapHeight, BufferedImage.TYPE_INT_RGB);
            legend = new BufferedImage(this.width, (int) (this.height * PART_OF_LEGEND_HEIGHT), BufferedImage.TYPE_INT_RGB);
            legendRecords = new BufferedImage(this.width, (int) (this.height * GAP_BETWEEN_LEGEND_AND_MAP), BufferedImage.TYPE_INT_RGB);

            if (null != colors) {
                if (!colorInterpolationModeEnabled) {
                    ColorFunction colorFunction = new ColorPaletteFunction(colors, legend.getWidth());
                    new ColorMapDrawer(colorFunction).draw(legend);
                } else {
                    ColorFunction colorFunction = new InterpolatedColorPaletteFunction(colors, new LinearInterpolator(), legend.getWidth());
                    new ColorMapDrawer(colorFunction).draw(legend);
                }

                PixelCoordinateToAreaConverter pixelCoordinateToAreaConverter =
                        new PixelCoordinateToAreaConverter(START_X, START_Y, END_X, END_Y, map.getWidth(), map.getHeight());
                List<Double> values = Util.getPointsByFunctionColorsAndArea(DifficultFunctionSingleton.getInstance(),
                        pixelCoordinateToAreaConverter,
                        colors.size());

                if (colorMapVisibility) {
                    if (colorInterpolationModeEnabled) {
                        InterpolatedColorMapFunction func = new InterpolatedColorMapFunction(values,
                                colors,
                                DifficultFunctionSingleton.getInstance(),
                                pixelCoordinateToAreaConverter,
                                new LinearInterpolator());

                        new ColorMapDrawer(func).draw(map);
                    } else {
                        ColorFunction colorFunction = new ColorMapFunction(values, colors, DifficultFunctionSingleton.getInstance(), pixelCoordinateToAreaConverter);
                        new ColorMapDrawer(colorFunction).draw(map);
                    }
                }

                new ColorPaletteRecordsDrawer(values).draw(legendRecords);
            }

            if (isGridShown) {
                new GridMapDrawer(m, k, GRID_COLOR).draw(map);
            }

            isUpdated = false;
        }

        g.drawImage(map, 0, 0, null);

        if (width > MIN_WIDTH_TO_PRINT_LEGENDS && height > MIN_HEIGHT_TO_PRINT_LEGENDS) {
            g.drawImage(legendRecords, 0, (int) (height * PART_OF_MAP_HEIGHT), null);
        }

        g.drawImage(legend, 0, (int) (height * (GAP_BETWEEN_LEGEND_AND_MAP + PART_OF_MAP_HEIGHT)), null);
    }

    public void setColorMapVisibility(boolean colorMapVisibility) {
        this.colorMapVisibility = colorMapVisibility;
        isUpdated = true;
        repaint();
    }

    public void setInteractiveModeEnabled(boolean newInteractiveMode) {
        this.interaciveMode = newInteractiveMode;
        isUpdated = true;
    }

    public void setColorInterpolationModeEnabled(boolean colorInterpolationModeEnabled) {
        this.colorInterpolationModeEnabled = colorInterpolationModeEnabled;
        isUpdated = true;
        repaint();
    }

    public void setDynamicIsolineMode(boolean b) {

    }

    public void setEnterPointMode(boolean b) {

    }

    public void updateSettings(int gridWidthDivisions, int gridHeightDivisions, int redColor, int greenColor, int blueColor) {
        this.m = gridWidthDivisions;
        this.k = gridHeightDivisions;

        this.isolineColor = new Color(redColor, greenColor, blueColor).getRGB();
        isUpdated = true;
        repaint();
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
}
