package ru.nsu.fit.g14201.chirikhin.function_visualization;

import ru.nsu.fit.g14201.chirikhin.filter.controller.MyPoint;
import ru.nsu.fit.g14201.chirikhin.util.ImageUtil;

import java.awt.image.BufferedImage;
import java.util.LinkedList;

public class OneValueFunctionInflater {
    public void paint(BufferedImage bufferedImage, LinkedList<MyPoint<Integer, Double>> points) {

        ImageUtil.drawDashedLine(bufferedImage, 0, 0, 0, bufferedImage.getHeight());
        ImageUtil.drawDashedLine(bufferedImage, 0, bufferedImage.getHeight() - 1, bufferedImage.getWidth(), bufferedImage.getHeight() - 1);

        double maxValue = points.getFirst().getValue2();
        for (MyPoint<Integer, Double> point : points) {
            if (point.getValue2() > maxValue) {
                maxValue = point.getValue2();
            }
        }


        /*List<MyPoint<V>> points = interpolatedFunction.getSortedValues();
        for (int k = 0; k < points.size(); ++k) {

        }
        */
    }
}
