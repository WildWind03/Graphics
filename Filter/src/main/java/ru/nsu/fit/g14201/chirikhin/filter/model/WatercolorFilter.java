package ru.nsu.fit.g14201.chirikhin.filter.model;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Collections;
import java.util.LinkedList;

public class WatercolorFilter implements BaseFilter {

    @Override
    public BufferedImage apply(BufferedImage bufferedImage) {
        BufferedImage filteredBufferedImage = new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(), BufferedImage.TYPE_INT_RGB);

        for (int k = 0; k < bufferedImage.getHeight(); ++k) {
            for (int i = 0; i < bufferedImage.getWidth(); ++i) {
                int x = i - 2;
                int y = k - 2;

                if (x < 0) {
                    x = 0;
                }

                if (y < 0) {
                    y = 0;
                }

                if (x + 5 >= bufferedImage.getWidth()) {
                    x = bufferedImage.getWidth() - 5;
                }

                if (y + 5 >= bufferedImage.getHeight()) {
                    y = bufferedImage.getHeight() - 5;
                }

                LinkedList<Integer> redList = new LinkedList<>();
                LinkedList<Integer> greenList = new LinkedList<>();
                LinkedList<Integer> blueList = new LinkedList<>();

                for (int m = y; m < y + 5; ++m) {
                    for (int n = x; n < x + 5; ++n) {
                        int[] color = new int[3];
                        bufferedImage.getRaster().getPixel(n, m, color);

                        redList.add(color[0]);
                        greenList.add(color[1]);
                        blueList.add(color[2]);

                    }
                }

                Collections.sort(redList);
                Collections.sort(greenList);
                Collections.sort(blueList);

                filteredBufferedImage.setRGB(i, k, new Color(redList.get(12), greenList.get(12), blueList.get(12)).getRGB());
            }

        }

        return filteredBufferedImage;
    }
}
