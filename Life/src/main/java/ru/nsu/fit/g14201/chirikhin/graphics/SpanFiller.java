package ru.nsu.fit.g14201.chirikhin.graphics;

import com.sun.deploy.util.ArrayUtil;

import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Stack;

public class SpanFiller {
    private static class MyPoint {
        private int x, y;

        private MyPoint(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }
    }

    private final Stack<MyPoint> myPoints;
    private final int[] fillColor;
    private final BufferedImage bufferedImage;
    private final int replacedColor[] = new int[3];

    public SpanFiller(BufferedImage bufferedImage, int x, int y, int[] fillColor) {
        myPoints = new Stack<>();
        this.fillColor = Arrays.copyOf(fillColor, fillColor.length);
        this.bufferedImage = bufferedImage;
        myPoints.push(new MyPoint(x, y));

        bufferedImage.getRaster().getPixel(x, y, replacedColor);
    }

    public void applyFiller() {
        MyPoint myPoint = myPoints.pop();
        int x = myPoint.getX();
        int y = myPoint.getY();

        int startX = x - 1;
        int finishX = x + 1;

        int[] currentColor = new int[3];

        while (true) {
            if (startX < 0) {
                break;
            }

            bufferedImage.getRaster().getPixel(startX, y, currentColor);

            if (!Arrays.equals(currentColor, replacedColor)) {
                break;
            } else {
                startX--;
            }
        }

        while(true) {
            if (finishX >= bufferedImage.getWidth()) {
                break;
            }

            bufferedImage.getRaster().getPixel(finishX , y, currentColor);

            if (!Arrays.equals(currentColor, replacedColor)) {
                break;
            } else {
                finishX++;
            }
        }

        boolean isPrevAboveAdded = false;
        boolean isPrevUnderAdded = false;


        for (int k = startX + 1; k < finishX; ++k) {
            bufferedImage.getRaster().setPixel(k, y, fillColor);

            if (y + 1 < bufferedImage.getHeight()) {

                bufferedImage.getRaster().getPixel(k, y + 1, currentColor);

                if (Arrays.equals(currentColor, replacedColor)) {
                    if (!isPrevAboveAdded) {
                        isPrevAboveAdded = true;
                        myPoints.push(new MyPoint(k, y + 1));
                    }
                } else {
                    isPrevAboveAdded = false;
                }
            }

            if (y - 1 >= 0) {
                bufferedImage.getRaster().getPixel(k, y - 1, currentColor);

                if (Arrays.equals(currentColor, replacedColor)) {
                    if (!isPrevUnderAdded) {
                        isPrevUnderAdded = true;
                        myPoints.push(new MyPoint(k, y - 1));
                    }
                } else {
                    isPrevUnderAdded = false;
                }
            }
        }

        if (!myPoints.isEmpty()){
            applyFiller();
        }
    }
}
