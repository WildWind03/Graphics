package ru.nsu.fit.g14201.chirikhin.filter.model;

import java.awt.image.BufferedImage;

public interface BaseFilter {
    BufferedImage apply(BufferedImage bufferedImage);
}
