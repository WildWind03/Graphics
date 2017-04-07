package ru.nsu.fit.g14201.chirikhin.isolines.view;

import java.awt.image.BufferedImage;

public class ColorMapDrawer implements Drawer {

    private final ColorFunction colorFunction;

    public ColorMapDrawer(ColorFunction colorFunction) {
        this.colorFunction = colorFunction;
    }

    @Override
    public void draw(BufferedImage bufferedImage) {
        for (int i = 0; i < bufferedImage.getHeight(); ++i) {
            for (int k = 0; k < bufferedImage.getWidth(); ++k) {
                bufferedImage.setRGB(k, i, colorFunction.apply(k, i));
            }
        }
    }
}
