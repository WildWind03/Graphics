package ru.nsu.fit.g14201.chirikhin.filter.model;

import java.awt.image.BufferedImage;

public class SobelFilter implements BaseFilter {

    private final int threshold;
    private static final int MATRIX_HEIGHT = 3;
    private static final int MATRIX_WIDTH = 3;

    public SobelFilter(int threshold) {
        this.threshold = threshold;
    }

    @Override
    public BufferedImage apply(BufferedImage bufferedImage) {
        MatrixFilter firstMatrixFilter = new MatrixFilter(new BaseMatrix(MATRIX_WIDTH, MATRIX_HEIGHT) {
            {
                matrix[0][0] = -1;
                matrix[0][1] = -2;
                matrix[0][2] = -1;
                matrix[1][0] = 0;
                matrix[1][1] = 0;
                matrix[1][2] = 0;
                matrix[2][0] = 1;
                matrix[2][1] = 2;
                matrix[2][2] = 1;
            }
        });

        BufferedImage fisrtFilteredBufferedImage = firstMatrixFilter.apply(bufferedImage);

        MatrixFilter secondMatrixFilter = new MatrixFilter(new BaseMatrix(MATRIX_WIDTH, MATRIX_HEIGHT) {
            {
                matrix[0][0] = -1;
                matrix[0][1] = 0;
                matrix[0][2] = 1;
                matrix[1][0] = -1;
                matrix[1][1] = 0;
                matrix[1][2] = 1;
                matrix[2][0] = -1;
                matrix[2][1] = 0;
                matrix[2][2] = 1;
            }
        });

        BufferedImage secondFilteredImage = secondMatrixFilter.apply(bufferedImage);

        final int COUNT_OF_COLOR_PARTS = 3;
        BufferedImage outputImage = new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(), BufferedImage.TYPE_INT_RGB);

        for (int k = 0; k < bufferedImage.getHeight(); ++k) {
            for (int i = 0; i < bufferedImage.getWidth(); ++i) {
                int[] newPixel = new int[COUNT_OF_COLOR_PARTS];

                int[] pixelFromFirstImage = new int[COUNT_OF_COLOR_PARTS];
                int[] pixelFromSecondImage = new int[COUNT_OF_COLOR_PARTS];

                fisrtFilteredBufferedImage.getRaster().getPixel(i, k, pixelFromFirstImage);
                secondFilteredImage.getRaster().getPixel(i, k, pixelFromSecondImage);

                for (int z = 0; z < COUNT_OF_COLOR_PARTS; ++z) {
                    newPixel[z] = (int) Math.sqrt(pixelFromFirstImage[z] * pixelFromFirstImage[z] + pixelFromSecondImage[z] * pixelFromSecondImage[z]);
                }

                GrayscaleFunctor grayscaleFunctor = new GrayscaleFunctor();
                int pixelValue = grayscaleFunctor.apply(newPixel)[0];

                if (pixelValue > threshold) {
                    outputImage.getRaster().setPixel(i, k, new int[] {255, 255, 255});
                } else {
                    outputImage.getRaster().setPixel(i, k, new int[] {0, 0, 0});
                }
            }
        }

        return outputImage;
    }
}
