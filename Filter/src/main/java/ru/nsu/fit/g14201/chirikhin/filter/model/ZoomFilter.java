package ru.nsu.fit.g14201.chirikhin.filter.model;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.function.Predicate;

public class ZoomFilter implements BaseFilter {

    @Override
    public BufferedImage apply(BufferedImage bufferedImage) {
        BufferedImage cutImage = bufferedImage.getSubimage(bufferedImage.getWidth() / 4, bufferedImage.getHeight() / 4, bufferedImage.getWidth() / 2, bufferedImage.getHeight() / 2);

        BufferedImage filteredBufferedImage = new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(), BufferedImage.TYPE_INT_RGB);

        for (int k = 0; k < cutImage.getHeight(); ++k) {
            for (int i = 0; i < cutImage.getWidth(); ++i) {
                int[] curPixel = new int[3];
                cutImage.getRaster().getPixel(i, k , curPixel);

                filteredBufferedImage.getRaster().setPixel(2 * i, 2 * k, curPixel);
            }
        }

        MatrixFilter matrixFilter = new MatrixFilter(new BaseMatrix(3, 3) {
            {
                matrix[0][0] = 0;
                matrix[0][1] = 0;
                matrix[0][2] = 0;

                matrix[1][0] = 0.5d;
                matrix[1][1] = 0;
                matrix[1][2] = 0.5d;

                matrix[2][0] = 0;
                matrix[2][1] = 0;
                matrix[2][2] = 0;
            }
        });
        BufferedImage bufferedImage1 = matrixFilter.apply(filteredBufferedImage, point -> ((point.getX() % 2 != 0) && (point.getY() % 2 == 0)));

        MatrixFilter matrixFilter1 = new MatrixFilter(new BaseMatrix(3, 3) {
            {
                matrix[0][0] = 0;
                matrix[0][1] = 0.5d;
                matrix[0][2] = 0;

                matrix[1][0] = 0;
                matrix[1][1] = 0;
                matrix[1][2] = 0;

                matrix[2][0] = 0;
                matrix[2][1] = 0.5d;
                matrix[2][2] = 0;
            }
        });

        BufferedImage bufferedImage2 = matrixFilter1.apply(bufferedImage1, point -> (point.getX() % 2 == 0 && point.getY() % 2 != 0));

        MatrixFilter matrixFilter2 = new MatrixFilter(new BaseMatrix(3, 3) {
            {
                matrix[0][0] = 0.25d;
                matrix[0][1] = 0;
                matrix[0][2] = 0.25d;

                matrix[1][0] = 0;
                matrix[1][1] = 0;
                matrix[1][2] = 0;

                matrix[2][0] = 0.25d;
                matrix[2][1] = 0;
                matrix[2][2] = 0.25d;
            }
        });


        return matrixFilter2.apply(bufferedImage2, point -> (point.getX() % 2 != 0 && point.getY() % 2 != 0));
    }
}
