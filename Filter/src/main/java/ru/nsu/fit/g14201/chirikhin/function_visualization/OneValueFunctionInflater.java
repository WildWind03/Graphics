package ru.nsu.fit.g14201.chirikhin.function_visualization;

import ru.nsu.fit.g14201.chirikhin.filter.controller.MyPoint;
import ru.nsu.fit.g14201.chirikhin.util.ImageUtil;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

public class OneValueFunctionInflater {
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

        int prevX = startX;
        int prevY = startY;

        int counter = 0;
        boolean isFirst = true;
        for (MyPoint<Integer, Double> point : points) {
            int x = startX + point.getValue1() * (int) ((double) (width) / maxWidth);
            int y = (int) ((point.getValue2() / maxHeight) * height);

            if (isFirst && startX != x) {
                prevX = startX + width - 1 + shift;
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
                    startX,
                    startY + height - prevY + shift,
                    color);
        }
    }
}
