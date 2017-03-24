package ru.nsu.fit.g14201.chirikhin.filter.model;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ZoomFilter implements BaseFilter {

    @Override
    public BufferedImage apply(BufferedImage bufferedImage) {
        BufferedImage cutImage = bufferedImage.getSubimage(bufferedImage.getWidth() / 4, bufferedImage.getHeight() / 4, bufferedImage.getWidth() / 2, bufferedImage.getHeight() / 2);
        Image filteredImage = cutImage.getScaledInstance(bufferedImage.getWidth(), bufferedImage.getHeight(), 0);

        BufferedImage filteredBufferedImage = new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(), BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = filteredBufferedImage.createGraphics();
        g2d.drawImage(filteredImage, 0, 0, null);
        g2d.dispose();

        return filteredBufferedImage;
    }
}
