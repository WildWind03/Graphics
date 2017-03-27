package ru.nsu.fit.g14201.chirikhin.filter.model;

import ru.nsu.fit.g14201.chirikhin.util.ImageUtil;

import java.awt.image.BufferedImage;

public class FloydDitheringFilter implements BaseFilter {

    private final int redDivision;
    private final int greenDivision;
    private final int blueDivision;

    public FloydDitheringFilter(int redDivision, int greenDivision, int blueDivision) {
        this.redDivision = redDivision;
        this.greenDivision = greenDivision;
        this.blueDivision = blueDivision;
    }

    private int getClosestColor(int currentColor, int divisionCount) {
        int divisionLength;

        if (2 == divisionCount) {
            divisionLength = 128;
        } else {
            divisionLength = 255 / (divisionCount - 1);
        }

        int closestColor = (currentColor + divisionLength / 2) / divisionLength * divisionLength;
        if (closestColor > 255) {
            closestColor = 255;
        }

        if (closestColor < 0) {
            closestColor = 0;
        }

        return closestColor;
    }

    private void updatePixel(BufferedImage bufferedImage, int i, int k, double ratio, int[] errors) {
        if (i < 0 || k < 0 || i >= bufferedImage.getWidth() || k >= bufferedImage.getHeight()) {
            return;
        }

        int[] oldValue = new int[3];
        bufferedImage.getRaster().getPixel(i, k, oldValue);

        int[] newValue = new int[3];
        newValue[0] = (int) (oldValue[0] + ratio * errors[0]);
        newValue[1] = (int) (oldValue[1] + ratio * errors[1]);
        newValue[2] = (int) (oldValue[2] + ratio * errors[2]);

        if (newValue[0] < 0) {
            newValue[0] = 0;
        }

        if (newValue[1] < 0) {
            newValue[1] = 0;
        }

        if (newValue[2] < 0) {
            newValue[2] = 0;
        }

        if (newValue[0] > 255) {
            newValue[0] = 255;
        }

        if (newValue[1] > 255) {
            newValue[1] = 255;
        }

        if (newValue[2] > 255) {
            newValue[2] = 255;
        }

        bufferedImage.getRaster().setPixel(i, k, newValue);
    }

    @Override
    public BufferedImage apply(BufferedImage bufferedImage) {
        BufferedImage imageCopy = ImageUtil.deepCopy(bufferedImage);
        BufferedImage filteredImage = new BufferedImage(imageCopy.getWidth(), imageCopy.getHeight(), BufferedImage.TYPE_INT_RGB);

        for (int k = 0; k < imageCopy.getHeight(); ++k) {
            for (int i = 0; i < imageCopy.getWidth(); ++i) {
                int[] newPixel = new int[3];
                int[] currentPixel = new int[3];

                imageCopy.getRaster().getPixel(i, k, currentPixel);

                newPixel[0] = getClosestColor(currentPixel[0], redDivision);
                newPixel[1] = getClosestColor(currentPixel[1], greenDivision);
                newPixel[2] = getClosestColor(currentPixel[2], blueDivision);

                filteredImage.getRaster().setPixel(i, k, newPixel);

                int errors[] = new int[3];
                errors[0] = currentPixel[0] - newPixel[0];
                errors[1] = currentPixel[1] - newPixel[1];
                errors[2] = currentPixel[2] - newPixel[2];

                updatePixel(imageCopy, i + 1, k, 7d/16d, errors);
                updatePixel(imageCopy, i + 1, k + 1, 1d/16d, errors);
                updatePixel(imageCopy, i, k + 1, 5d/16d, errors);
                updatePixel(imageCopy, i - 1, k + 1, 3d/16d, errors);
            }
        }
        return filteredImage;
    }
}
