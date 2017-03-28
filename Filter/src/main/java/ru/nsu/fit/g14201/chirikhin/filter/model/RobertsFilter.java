package ru.nsu.fit.g14201.chirikhin.filter.model;

import java.awt.image.BufferedImage;

public class RobertsFilter implements BaseFilter {

    private final int threshold;

    public RobertsFilter(int threshold) {
        this.threshold = threshold;
    }

    @Override
    public BufferedImage apply(BufferedImage bufferedImage) {
        BufferedImage filteredImage = new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(), BufferedImage.TYPE_INT_RGB);

        for (int i = 0; i < bufferedImage.getHeight(); ++i) {
            for (int k = 0; k < bufferedImage.getWidth(); ++k) {
                int[] firstTmpPixel = getPixel(bufferedImage, k, i);
                int[] secondTmpPixel = getPixel(bufferedImage, k + 1, i + 1);

                int tmp[] = new int[3];
                tmp[0] = Math.abs(firstTmpPixel[0] - secondTmpPixel[0]);
                tmp[1] = Math.abs(firstTmpPixel[1] - secondTmpPixel[1]);
                tmp[2] = Math.abs(firstTmpPixel[2] - secondTmpPixel[2]);

                int[] fourthTmpPixel = getPixel(bufferedImage, k + 1, i);
                int[] fifthTmpPixel = getPixel(bufferedImage, k, i + 1);

                int tmp2[] = new int[3];
                tmp2[0] = Math.abs(fourthTmpPixel[0] - fifthTmpPixel[0]);
                tmp2[1] = Math.abs(fourthTmpPixel[1] - fifthTmpPixel[1]);
                tmp2[2] = Math.abs(fourthTmpPixel[2] - fifthTmpPixel[2]);

                int newRed = (int) Math.sqrt(tmp[0] * tmp[0] + tmp2[0] * tmp2[0]);
                int newGreen = (int) Math.sqrt(tmp[1] * tmp[1] + tmp2[1] * tmp2[1]);
                int newBlue = (int) Math.sqrt(tmp[2] * tmp[2] + tmp2[2] * tmp2[2]);
                GrayscaleFunctor grayscaleFunctor = new GrayscaleFunctor();
                int pixelValue = grayscaleFunctor.apply(new int[] {newRed, newGreen, newBlue})[0];

                if (pixelValue > threshold) {
                    filteredImage.getRaster().setPixel(k, i, new int[] {255, 255, 255});
                } else {
                    filteredImage.getRaster().setPixel(k, i, new int[] {0, 0, 0});
                }
            }
        }
        return filteredImage;
    }

    private int[] getPixel(BufferedImage bufferedImage, int x, int y) {
        if (x >= bufferedImage.getWidth() || y >= bufferedImage.getHeight()) {
            return new int[] {0, 0, 0};
        }

        int[] pixel = new int[3];
        bufferedImage.getRaster().getPixel(x, y, pixel);

        return pixel;
    }
}
