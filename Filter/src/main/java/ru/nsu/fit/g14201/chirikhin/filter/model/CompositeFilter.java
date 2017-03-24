package ru.nsu.fit.g14201.chirikhin.filter.model;

import java.awt.image.BufferedImage;
import java.util.List;

public class CompositeFilter implements BaseFilter {
    private final List<BaseFilter> filters;

    public CompositeFilter(List <BaseFilter> filters) {
        this.filters = filters;
    }

    @Override
    public BufferedImage apply(BufferedImage bufferedImage) {
        BufferedImage newImage = bufferedImage;

        for (BaseFilter baseFilter : filters) {
            newImage = baseFilter.apply(newImage);
        }

        return newImage;
    }
}
