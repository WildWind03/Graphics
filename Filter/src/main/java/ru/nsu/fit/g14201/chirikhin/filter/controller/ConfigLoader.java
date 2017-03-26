package ru.nsu.fit.g14201.chirikhin.filter.controller;

import java.awt.*;
import java.io.*;
import java.util.LinkedList;
import java.util.function.Predicate;

public class ConfigLoader {

    private final LinkedList<int[]> emissionPoints;
    private final LinkedList<Point<Integer, Float>> absorptionPoints;
    private final LinkedList<int[]> chargePoints;

    public class Point<T1, T2> {
        private T1 value1;
        private T2 value2;

        public Point(T1 value1, T2 value2) {
            this.value1 = value1;
            this.value2 = value2;
        }

        public T1 getValue1() {
            return value1;
        }

        public T2 getValue2() {
            return value2;
        }
    }

    public ConfigLoader(File file) throws InvalidConfigException, IOException {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
            String pointInAbsorptionString = bufferedReader.readLine();
            absorptionPoints = new LinkedList<>();

            int countOfPointsInAbsorption = getOneValue(pointInAbsorptionString, value -> ((value >= 0) && (value < 1000)));
            for (int k = 0; k < countOfPointsInAbsorption; ++k) {
                String nextString = bufferedReader.readLine();
                Point<Integer, Float> absorptionPoint = getPoint(nextString,
                        value -> ((value >= 0) && (value <= 100)),
                        value -> (value >= 0.0d) && (value <= 1d));

                absorptionPoints.push(absorptionPoint);
            }

            emissionPoints = new LinkedList<>();
            String pointInEmissionString = bufferedReader.readLine();
            int countOfPointsInEmission = getOneValue(pointInEmissionString, value -> ((value >= 0) && (value < 1000)));
            for (int k = 0; k < countOfPointsInEmission; ++k) {
                String nextString = bufferedReader.readLine();
                int[] values = getFourValues(nextString, value -> ((value >= 0) && (value <= 100)),
                        value -> ((value >= 0) && (value <= 255)),
                        value -> ((value >= 0) && (value <= 255)),
                        value -> ((value >= 0) && (value <= 255)));

                emissionPoints.push(values);
            }

            chargePoints = new LinkedList<>();
            String chargeCountString = bufferedReader.readLine();
            int chargeCount = getOneValue(chargeCountString, value -> ((value >= 0) && (value < 1000)));
            for (int k = 0; k < chargeCount; ++k) {
                String nextString = bufferedReader.readLine();

                int[] values = getFourValues(nextString,
                        value -> ((value >= -100) && (value <= 100)),
                        value -> ((value >= -100) && (value <= 100)),
                        value -> ((value >= -100) && (value <= 100)),
                        value -> ((value >= -100) && (value <= 100)));

                chargePoints.push(values);
            }

            String nextLine = bufferedReader.readLine();
            if (null != nextLine) {
                throw new InvalidConfigException("The config file contains extra line in the end");
            }

        } catch (FileNotFoundException e) {
            throw new InvalidConfigException(e.getMessage());
        }
    }

    public LinkedList<int[]> getEmissionPoints() {
        return emissionPoints;
    }

    public LinkedList<Point<Integer, Float>> getAbsorptionPoints() {
        return absorptionPoints;
    }

    public LinkedList<int[]> getChargePoints() {
        return chargePoints;
    }

    private Point<Integer, Float> getPoint(String string, Predicate<Integer> integerPredicate, Predicate<Float> floatPredicate) throws InvalidConfigException {
        String[] params = string.split(" ");

        if (params.length < 2) {
            throw new InvalidConfigException("Invalid count of args in a string: " + params.length + ". Min length is 2");
        }

        int firstValue;
        float secondValue;

        try {
            firstValue = Integer.parseInt(params[0]);
            secondValue = Float.parseFloat(params[1]);
        } catch (NumberFormatException e) {
            throw new InvalidConfigException(e.getMessage());
        }

        if (params.length > 2) {
            String otherSymbols = params[2];
            if (!otherSymbols.substring(0, 2).equals("//")) {
                throw new InvalidConfigException("Invalid config. Extra lines");
            }
        }

        if (!integerPredicate.test(firstValue) || !floatPredicate.test(secondValue)) {
            throw new InvalidConfigException("Invalid config! According to predicate, it's not appropriate!");
        }

        return new Point<>(firstValue, secondValue);
    }

    private int getOneValue(String string, Predicate<Integer> predicate) throws InvalidConfigException {
        String[] params = string.split(" ");

        int countOfPoints;

        try {
            countOfPoints = Integer.parseInt(params[0]);
        } catch (NumberFormatException e) {
            throw new InvalidConfigException(e.getMessage());
        }

        if (params.length > 1) {
            String otherSymbols = params[1];
            if (!otherSymbols.substring(0, 2).equals("//")) {
                throw new InvalidConfigException("Invalid config. Extra lines");
            }
        }

        if (!predicate.test(countOfPoints)) {
            throw new InvalidConfigException("Invalid config! According to predicate, it's not appropriate!");
        }

        return countOfPoints;
    }

    private int[] getFourValues(String string, Predicate<Integer> predicate1,
                                Predicate<Integer> predicate2,
                                Predicate<Integer> predicate3,
                                Predicate<Integer> predicate4) throws InvalidConfigException {
        String[] params = string.split(" ");

        if (params.length < 4) {
            throw new InvalidConfigException("Invalid count of args in a string: " + params.length + ". Min length is 4");
        }

        int value1;
        int value2;
        int value3;
        int value4;

        try {
            value1 = Integer.parseInt(params[0]);
            value2 = Integer.parseInt(params[1]);
            value3 = Integer.parseInt(params[2]);
            value4 = Integer.parseInt(params[3]);
        } catch (NumberFormatException e) {
            throw new InvalidConfigException(e.getMessage());
        }

        if (params.length > 4) {
            String otherSymbols = params[4];
            if (!otherSymbols.substring(0, 2).equals("//")) {
                throw new InvalidConfigException("Invalid config. Extra lines");
            }
        }

        if (!predicate1.test(value1) || !predicate2.test(value2) || !predicate3.test(value3) || !predicate4.test(value4)) {
            throw new InvalidConfigException("Invalid config! According to predicate, it's not appropriate!");
        }

        return new int[] {value1, value2, value3, value4};
    }
}
