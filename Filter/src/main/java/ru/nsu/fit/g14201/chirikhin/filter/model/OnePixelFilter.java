package ru.nsu.fit.g14201.chirikhin.filter.model;

import java.awt.image.BufferedImage;
import java.util.function.Function;

public class OnePixelFilter implements BaseFilter {
    private final Function<int[], int[]> function;

    public OnePixelFilter(Function<int[], int[]> function) {
        this.function = function;
    }

    @Override
    public BufferedImage apply(BufferedImage bufferedImage) {
        BufferedImage bufferedImage1 = new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(), BufferedImage.TYPE_INT_RGB);
        for (int k = 0; k < bufferedImage.getHeight(); ++k) {
            for (int i = 0; i < bufferedImage.getWidth(); ++i) {
                int[] originalPixel = new int[3];
                bufferedImage.getRaster().getPixel(i, k, originalPixel);
                bufferedImage1.getRaster().setPixel(i, k, function.apply(originalPixel));
            }
        }

        return bufferedImage1;
    }
}
