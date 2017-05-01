package ru.fit.g14201.chirikhin.wireframe.model_loader;

import chirikhin.matrix.Matrix;
import ru.fit.g14201.chirikhin.wireframe.model.BSpline;
import ru.fit.g14201.chirikhin.wireframe.model.Model;

import java.awt.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ModelSaver {
    private ModelSaver() {

    }
    public static void saveModel(File file, Model model) throws IOException {
        try(BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(
                file))) {
            bufferedWriter.write(model.getN() + " " + model.getM() + " " + model.getK() + " " +
                    model.getA() + " " + model.getB() + " " + model.getC() + " " + model.getD());
            bufferedWriter.newLine();
            bufferedWriter.write(model.getZn() + " " + model.getZf() + " " + model.getSw() + " " +
                    model.getSh());
            bufferedWriter.newLine();
            Matrix roundMatrix = model.getRoundMatrix();
            for (int i = 0; i < 3; ++i) {
                bufferedWriter.write(roundMatrix.get(i, 0) + " " + roundMatrix.get(i, 1) + " " + roundMatrix.get(i, 2));
                bufferedWriter.newLine();
            }

            Color backgroundColor = model.getBackgroundColor();
            bufferedWriter.write(backgroundColor.getRed() + " " + backgroundColor.getGreen() + " "
                    + backgroundColor.getBlue());

            bufferedWriter.newLine();
            bufferedWriter.write(model.getbSplines().size() + "");
            bufferedWriter.newLine();

            for (BSpline BSpline : model.getbSplines()) {
                Color color = BSpline.getColor();
                bufferedWriter.write(color.getRed() + " " + color.getGreen() + " " + color.getBlue());
                bufferedWriter.newLine();
                bufferedWriter.write(BSpline.getCx() + " " + BSpline.getCy() + " " + BSpline.getCz());
                bufferedWriter.newLine();

                Matrix shapeMatrix = BSpline.getRoundMatrix();
                for (int i = 0; i < 3; ++i) {
                    bufferedWriter.write(shapeMatrix.get(i, 0) + " " + shapeMatrix.get(i, 1) + " " + shapeMatrix.get(i, 2));
                    bufferedWriter.newLine();
                }

                bufferedWriter.write(BSpline.getPoints().size() + "");
                bufferedWriter.newLine();

                for (chirikhin.support.Point point : BSpline.getPoints()) {
                    bufferedWriter.write(point.getX() + " " + point.getY());
                    bufferedWriter.newLine();
                }
            }
        }
    }
}
