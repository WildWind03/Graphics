package ru.nsu.fit.g14201.chirikhin.function_visualization;

import ru.nsu.fit.g14201.chirikhin.filter.controller.MyPoint;
import ru.nsu.fit.g14201.chirikhin.util.ImageUtil;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Collections;
import java.util.LinkedList;

public class OneValueFunctionInflater {

    private static final int MAX_SHIFT = 2;

    public void paint(BufferedImage bufferedImage, LinkedList<MyPoint<Integer, Double>> points) {
        paint(bufferedImage, points, Color.BLACK, 100, 1, 0);
    }

    public void paint(BufferedImage bufferedImage, LinkedList<MyPoint<Integer, Double>> points, Color color, double maxWidth, double maxHeight, int shift) {

        Collections.sort(points);

        int prevX = 0;
        int prevY = 0;

        boolean isFirst = true;
        for (MyPoint<Integer, Double> point : points) {
            int x = point.getValue1() * (int) ((double) (bufferedImage.getWidth() - MAX_SHIFT) / maxWidth) + shift;
            int y = (int) ((point.getValue2() / maxHeight) * (bufferedImage.getHeight() - MAX_SHIFT)) + shift;

            if (isFirst && 0 != x) {
                prevX = 0;
                prevY = y;
                isFirst = false;
            }

            if (isFirst) {
                isFirst = false;
                continue;
            }

            ImageUtil.drawLine(bufferedImage,
                    prevX + shift,
                    bufferedImage.getHeight() + MAX_SHIFT - shift - prevY,
                    x + shift,
                    bufferedImage.getHeight() + MAX_SHIFT - shift - y,
                    color);

            prevX = x;
            prevY = y;
        }

        if (prevX < bufferedImage.getWidth() - MAX_SHIFT) {
            ImageUtil.drawLine(bufferedImage,
                    prevX + shift,
                    bufferedImage.getHeight() + MAX_SHIFT - shift - prevY,
                    bufferedImage.getWidth() - MAX_SHIFT - 1 + shift,
                    bufferedImage.getHeight() + MAX_SHIFT - shift - prevY,
                    color);
        }
    }
}
