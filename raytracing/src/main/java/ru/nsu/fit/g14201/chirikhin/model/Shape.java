package ru.nsu.fit.g14201.chirikhin.model;

public abstract class Shape {
    private final OpticalCharacteristics opticalCharacteristics;

    public Shape(OpticalCharacteristics opticalCharacteristics) {
        this.opticalCharacteristics = opticalCharacteristics;
    }

    public OpticalCharacteristics getOpticalCharacteristics() {
        return opticalCharacteristics;
    }
}
