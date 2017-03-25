package ru.nsu.fit.g14201.chirikhin.filter.model;

import java.awt.image.BufferedImage;

public class RotationFilter implements BaseFilter {
    private final double cosX;
    private final double sinX;

    public RotationFilter(int degree) {
        cosX = Math.cos(degree);
        sinX = Math.sin(degree);
    }

    @Override
    public BufferedImage apply(BufferedImage bufferedImage) {
        BufferedImage bufferedImage1 = new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(), BufferedImage.TYPE_INT_RGB);

        int centerX = bufferedImage.getWidth() / 2;
        int centerY = bufferedImage.getHeight() / 2;

        for (int i = 0; i < bufferedImage.getHeight(); ++i) {
            for (int k = 0; k < bufferedImage.getWidth(); ++k) {
                int[] pixel = new int[3];

//                int oldX = (int) (centerX + (k - centerX) * cosX - (i - centerY) * sinX);
//                int oldY = (int) (centerY + (-i - centerY) * sinX + (k - centerX) * cosX);

                int oldX = (int) (centerX * cosX * cosX + centerX * sinX * sinX - centerX * sinX + centerY * cosX - cosX * i + sinX * k);
                int oldY = (int) (-centerX * cosX + centerY * cosX * cosX + centerY * sinX * sinX - centerY * sinX + cosX * k + sinX * i);

//                int oldX = (int) (centerX + (k - centerX) * cosX - (i - centerY) * sinX);
//                int oldY = (int) (-centerY + (-k - centerX) * sinX + (i - centerY) * cosX);

//                int newX = (int) (centerX + (k - centerX) * cosX + (i - centerY) * sinX);
//                int newY = (int) (centerY + (i - centerY) * cosX - (k - centerX) * sinX);

                if (oldX >= 0 && oldX < bufferedImage.getWidth() && oldY >= 0 && oldY < bufferedImage.getHeight()) {
                    bufferedImage.getRaster().getPixel(oldX, oldY, pixel);
                    bufferedImage1.getRaster().setPixel(k, i, pixel);
                }
            }
        }
        return bufferedImage1;
    }
}
