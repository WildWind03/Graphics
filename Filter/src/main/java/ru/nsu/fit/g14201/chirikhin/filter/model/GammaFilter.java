package ru.nsu.fit.g14201.chirikhin.filter.model;

import java.awt.image.BufferedImage;

public class GammaFilter implements BaseFilter {

    private final double gamma;

    public GammaFilter(double gamma) {
        this.gamma = gamma;
    }

    @Override
    public BufferedImage apply(BufferedImage bufferedImage) {
        BufferedImage filteredImage = new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(), BufferedImage.TYPE_INT_RGB);
        for (int k = 0; k < bufferedImage.getHeight(); ++k) {
            for (int i = 0; i < bufferedImage.getWidth(); ++i) {
                int[] pixel = new int[3];

                bufferedImage.getRaster().getPixel(i, k, pixel);
                double red  = (double) pixel[0] / 255d;
                double green = (double) pixel[1] / 255d;
                double blue = (double) pixel[2] / 255d;

                double inverseGamma = 1d / gamma;
                int newRed = (int) (Math.pow(red, inverseGamma) * 255);
                int newGreen = (int) (Math.pow(green, inverseGamma) * 255);
                int newBlue = (int) (Math.pow(blue, inverseGamma) * 255);

                filteredImage.getRaster().setPixel(i, k, new int[] {newRed, newGreen, newBlue});
            }
        }
        return filteredImage;
    }
}
