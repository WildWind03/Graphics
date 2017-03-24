package ru.nsu.fit.g14201.chirikhin.filter.model;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.function.Predicate;

public class MatrixFilter implements BaseFilter {
    private final BaseMatrix baseMatrix;
    private final int MAX_COLOR = 255;
    private final int MIN_COLOR = 0;

    public MatrixFilter(BaseMatrix baseMatrix) {
        this.baseMatrix = baseMatrix;
    }

    private int[] getNearPixel(BufferedImage bufferedImage, int x, int y) {
        int newX;
        int newY;

        if (x < 0) {
            newX = 0;
        } else {
            if (x >= bufferedImage.getWidth()) {
                newX = bufferedImage.getWidth() - 1;
            } else {
                newX = x;
            }
        }

        if (y < 0) {
            newY = 0;
        } else {
            if (y >= bufferedImage.getHeight()) {
                newY = bufferedImage.getHeight() - 1;
            } else {
                newY = y;
            }
        }

        int[] pixel = new int[3];
        bufferedImage.getRaster().getPixel(newX, newY, pixel);

        return pixel;
    }

    private double fixColorIfWrong(double color) {
        if (color > MAX_COLOR) {
            return MAX_COLOR;
        }

        if (color < MIN_COLOR) {
            return MIN_COLOR;
        }

        return color;
    }

    private int[] getFilteredPixel(BufferedImage bufferedImage, int x, int y) {
        double newRed = 0;
        double newGreen = 0;
        double newBlue = 0;

        int widthShift = baseMatrix.getWidth() / 2;
        int heightShift = baseMatrix.getHeight() / 2;

        for (int i = x - widthShift; i <= x + widthShift; ++i) {
            for (int k = y - heightShift; k <= y + heightShift; ++k) {
                double matrixPixel = baseMatrix.getValue(i - x + widthShift, k - y + heightShift);

                int[] imagePixel = getNearPixel(bufferedImage, i, k);

                newRed += imagePixel[0] * matrixPixel;
                newGreen += imagePixel[1] * matrixPixel;
                newBlue += imagePixel[2] * matrixPixel;
            }
        }

        newRed = fixColorIfWrong(newRed);
        newGreen = fixColorIfWrong(newGreen);
        newBlue = fixColorIfWrong(newBlue);

        return new int[] {(int) newRed, (int) newGreen, (int) newBlue};


    }

    @Override
    public BufferedImage apply(BufferedImage bufferedImage) {
        /*BufferedImage newImage = new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(), BufferedImage.TYPE_INT_RGB);

        for (int i = 0; i < bufferedImage.getHeight(); ++i) {
            for (int k = 0; k < bufferedImage.getWidth(); ++k) {
                int[] pixel = getFilteredPixel(bufferedImage, k, i);
                newImage.getRaster().setPixel(k, i, pixel);
            }
        }

        return newImage;
        */
        return apply(bufferedImage, point -> true);
    }

    public BufferedImage apply(BufferedImage bufferedImage, Predicate<Point> predicate) {
        BufferedImage newImage = new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(), BufferedImage.TYPE_INT_RGB);

        for (int i = 0; i < bufferedImage.getHeight(); ++i) {
            for (int k = 0; k < bufferedImage.getWidth(); ++k) {
                if (predicate.test(new Point(k, i))) {

                    int[] pixel = getFilteredPixel(bufferedImage, k, i);
                    newImage.getRaster().setPixel(k, i, pixel);
                } else {
                    int[] pixel = new int[3];
                    bufferedImage.getRaster().getPixel(k, i, pixel);
                    newImage.getRaster().setPixel(k, i, pixel);
                }
            }
        }

        return newImage;
    }
}
