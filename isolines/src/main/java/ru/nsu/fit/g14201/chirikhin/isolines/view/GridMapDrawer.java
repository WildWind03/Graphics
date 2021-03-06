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
        int stepX = (int)(((double) bufferedImage.getWidth() ) / (double) widthDiv);
        int stepY = (int)(((double) bufferedImage.getHeight()) / (double) heightDiv);

        int realWidth = ((bufferedImage.getWidth()) / widthDiv) * widthDiv;
        int realHeight = ((bufferedImage.getHeight()) / heightDiv) * heightDiv;

        int x = stepX;
        for (int counter = 0; counter < widthDiv; x+=stepX, counter++) {
            graphics2D.drawLine(x, 0, x, realHeight);
        }

        int y = stepY;
        for (int counter = 0; counter < heightDiv; y+=stepY, counter++) {
            graphics2D.drawLine(0, y, realWidth, y);
        }

        graphics2D.dispose();
    }
}
