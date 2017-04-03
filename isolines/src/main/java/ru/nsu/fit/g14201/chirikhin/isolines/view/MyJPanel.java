package ru.nsu.fit.g14201.chirikhin.isolines.view;

import ru.nsu.fit.g14201.chirikhin.isolines.model.Function;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;

public class MyJPanel extends JPanel {
    private static final double PART_OF_MAP_HEIGHT = 0.75;
    private static final double GAP_BETWEEN_LEGEND_AND_MAP = 0.05;
    private static final double PART_OF_LEGEND_HEIGHT = 0.2;
    private static final int MIN_HEIGHT = 10;
    private static final int MIN_WIDTH = 10;

    private BufferedImage map;
    private BufferedImage legend;

    private int width;
    private int height;

    public MyJPanel(int width, int height) {
        update(width, height);
    }

    public void update(int width, int height) {
        if (width < MIN_WIDTH || height < MIN_HEIGHT) {
            return;
        }
        
        this.width = width;
        this.height = height;
        setPreferredSize(new Dimension(this.width, this.height));

        int mapHeight = (int) (PART_OF_MAP_HEIGHT * this.height);

        map = new BufferedImage(this.width, mapHeight, BufferedImage.TYPE_INT_RGB);
        legend = new BufferedImage(this.width, (int) (this.height * PART_OF_LEGEND_HEIGHT), BufferedImage.TYPE_INT_RGB);

        new Legend(Arrays.asList(Color.BLACK, Color.CYAN, Color.green)).draw(legend);
        try {
            new ColorMap(Arrays.asList(Color.BLACK, Color.CYAN, Color.green)).draw(new Function(), map);
        } catch (Exception e) {
            e.printStackTrace();
        }
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(map, 0, 0, null);
        g.drawImage(legend, 0, (int) (height * (GAP_BETWEEN_LEGEND_AND_MAP + PART_OF_MAP_HEIGHT)), null);
    }
}
