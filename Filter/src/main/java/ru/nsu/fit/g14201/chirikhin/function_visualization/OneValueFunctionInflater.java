package ru.nsu.fit.g14201.chirikhin.function_visualization;

import ru.nsu.fit.g14201.chirikhin.filter.controller.MyPoint;
import ru.nsu.fit.g14201.chirikhin.util.ImageUtil;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Collections;
import java.util.LinkedList;

public class OneValueFunctionInflater {
    //public void paint(BufferedImage bufferedImage, LinkedList<MyPoint<Integer, Double>> points) {
    //    paint(bufferedImage, points, Color.BLACK, 100, 1, 0, 5, 5, 440, 190);
    //}

    public void paint(BufferedImage bufferedImage,
                      LinkedList<MyPoint<Integer, Double>> points,
                      Color color,
                      double maxWidth,
                      double maxHeight,
                      int shift,
                      int startX,
                      int startY,
                      int width,
                      int height) {

        Collections.sort(points);

        int prevX = startX;
        int prevY = startY;

        boolean isFirst = true;
        for (MyPoint<Integer, Double> point : points) {
            int x = startX + point.getValue1() * (int) ((double) (width) / maxWidth);
            int y = (int) ((point.getValue2() / maxHeight) * height);

            if (isFirst && 0 != x) {
                prevX = startX + shift;
                prevY = y + shift;
                isFirst = false;
            }

            if (isFirst) {
                isFirst = false;
                continue;
            }

            ImageUtil.drawLine(bufferedImage,
                    prevX + shift,
                    startY + height - prevY + shift,
                    x + shift,
                    startY + height - y + shift,
                    color);

            prevX = x;
            prevY = y;
        }

        if (prevX < startX + width) {
            ImageUtil.drawLine(bufferedImage,
                    prevX + shift,
                    startY + height - prevY + shift,
                    startX + width - 1 + shift,
                    startY + height - prevY + shift,
                    color);
        }
    }
}
