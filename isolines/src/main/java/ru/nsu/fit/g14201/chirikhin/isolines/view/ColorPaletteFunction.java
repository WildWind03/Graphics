package ru.nsu.fit.g14201.chirikhin.isolines.view;

import java.awt.*;
import java.util.List;

public class ColorPaletteFunction implements ColorFunction {

    private final List<Integer[]> colors;
    private final int step;

    public ColorPaletteFunction(List<Integer[]> colors, int width){
        this.colors = colors;
        this.step = width / colors.size();
    }

    @Override
    public Integer apply(Integer integer, Integer integer2) {
        int colorPos = integer / step;
        colorPos = Math.min(colorPos, colors.size() - 1);
        Integer[] color = colors.get(colorPos);
        return new Color(color[0], color[1], color[2]).getRGB();
    }
}
