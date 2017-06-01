package ru.nsu.fit.g14201.chirikhin.model;

public abstract class ShapeBuilder {
    protected OpticalCharacteristics opticalCharacteristics;

    public ShapeBuilder setOpticalCharacteristics(OpticalCharacteristics opticalCharacteristics) {
        this.opticalCharacteristics = opticalCharacteristics;
        return this;
    }

    public abstract Shape build();
}
