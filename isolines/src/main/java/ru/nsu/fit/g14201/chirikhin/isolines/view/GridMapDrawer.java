package ru.nsu.fit.g14201.chirikhin.isolines.view;

import java.awt.*;
import java.awt.image.BufferedImage;

public class GridMapDrawer implements Drawer {
    private final int widthDiv;
    private final int heightDiv;

    private final Color gridColor;

    public GridMapDrawer(int widthDiv, int heightDiv, Color gridColor) {
        this.widthDiv = widthDiv;
        this.heightDiv = heightDiv;
        this.gridColor = gridColor;
    }

    @Override
    public void draw(BufferedImage bufferedImage) {
        Graphics2D graphics2D = bufferedImage.createGraphics();
        graphics2D.setColor(gridColor);
        int stepX = (int) Math.ceil((double) bufferedImage.getWidth() / (double) widthDiv);
        int stepY = (int) Math.ceil((double) bufferedImage.getHeight() / (double) heightDiv);

        for (int k = stepX; k < bufferedImage.getWidth(); k+=stepX) {
            graphics2D.drawLine(k, 0, k, bufferedImage.getHeight());
        }

        for (int k = stepY; k < bufferedImage.getHeight(); k+=stepY) {
            graphics2D.drawLine(0, k, bufferedImage.getWidth(), k);
        }
    }
}
