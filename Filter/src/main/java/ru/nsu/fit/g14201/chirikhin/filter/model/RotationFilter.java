package ru.nsu.fit.g14201.chirikhin.filter.model;

import java.awt.image.BufferedImage;

public class RotationFilter implements BaseFilter {
    private final double cosX;
    private final double sinX;

    public RotationFilter(int degree) {
        cosX = Math.cos(Math.toRadians(degree));
        sinX = Math.sin(Math.toRadians(degree));
    }

    @Override
    public BufferedImage apply(BufferedImage bufferedImage) {
        BufferedImage bufferedImage1 = new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(), BufferedImage.TYPE_INT_RGB);

        int centerX = bufferedImage.getWidth() / 2;
        int centerY = bufferedImage.getHeight() / 2;

        for (int i = 0; i < bufferedImage.getHeight(); ++i) {
            for (int k = 0; k < bufferedImage.getWidth(); ++k) {
                int[] pixel = new int[3];

                int oldX = (int) (centerX - centerX * sinX + centerY * cosX - cosX * i + sinX * k);
                int oldY = (int) (-centerX * cosX + centerY - centerY * sinX + cosX * k + sinX * i);

                if (oldX >= 0 && oldX < bufferedImage.getWidth() && oldY >= 0 && oldY < bufferedImage.getHeight()) {
                    bufferedImage.getRaster().getPixel(oldX, oldY, pixel);
                    bufferedImage1.getRaster().setPixel(i, k, pixel);
                }
            }
        }
        return bufferedImage1;
    }
}
