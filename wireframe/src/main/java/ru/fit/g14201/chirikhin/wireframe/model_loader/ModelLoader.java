package ru.fit.g14201.chirikhin.wireframe.model_loader;

import chirikhin.matrix.Matrix;
import chirikhin.swing.util.ListUtil;
import chirikhin.universal_parser.*;
import ru.fit.g14201.chirikhin.wireframe.model.Model;
import ru.fit.g14201.chirikhin.wireframe.model.BSplineBuilder;
import ru.fit.g14201.chirikhin.wireframe.model.ShapeBuildingException;

import java.awt.*;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ModelLoader {
    private static class IntegerWrapper {
        private int integer;

        IntegerWrapper(int integer) {
            this.integer = integer;
        }
    }

    private final Model model = new Model();

    public ModelLoader(File file) throws ParserException, TypeConversionException, TypeMatchingException, NoObjectFactoryException {
        Matrix matrix = new Matrix(new float[3][3]);
        model.setRoundMatrix(matrix);
        final IntegerWrapper rowCountForMatrix = new IntegerWrapper(0);
        final IntegerWrapper countOfShapes = new IntegerWrapper(0);

        MyFactory objectFactory = new MyFactory();
        objectFactory.addTypeMaker(Integer.class, Integer::parseInt);
        objectFactory.addTypeMaker(Float.class, Float::parseFloat);
        objectFactory.addTypeMaker(Comment.class, (string) -> {
            if (!string.substring(0, 2).equals("//")) {
                throw new ParserException("Invalid comment");
            }

            return null;
        });

        ParserConfigBuilder parserConfigBuilder = new ParserConfigBuilder()
                .with(objectFactory)
                .add(new TypeCheckRunnable(ListUtil.asList(Integer.class,
                        Integer.class, Integer.class, Float.class, Float.class, Integer.class,
                        Integer.class, Comment.class)) {
                    @Override
                    public void run(Object[] objects, ParserConfig parserConfig) {
                        model.setN((Integer) objects[0]);
                        model.setM((Integer) objects[1]);
                        model.setK((Integer) objects[2]);
                        model.setA((Float) objects[3]);
                        model.setB((Float) objects[4]);
                        model.setC((Integer) objects[5]);
                        model.setD((Integer) objects[6]);
                        parserConfig.nextIndex();
                    }
                })
                .add(new TypeCheckRunnable(ListUtil.asList(Integer.class,
                        Integer.class, Integer.class, Integer.class, Comment.class)) {
                    @Override
                    public void run(Object[] objects, ParserConfig parserConfig) {
                        model.setZn((Integer) objects[0]);
                        model.setZf((Integer) objects[1]);
                        model.setSw((Integer) objects[2]);
                        model.setSh((Integer) objects[3]);
                        parserConfig.nextIndex();
                    }
                })
                .add(new TypeCheckRunnable(ListUtil.asList(Integer.class, Integer.class, Integer.class,
                        Comment.class)) {
                    @Override
                    public void run(Object[] objects, ParserConfig parserConfig) {
                        for (int k = 0; k < 3; ++k) {
                            matrix.set(rowCountForMatrix.integer, k, (Integer) objects[k]);
                        }

                        if (++rowCountForMatrix.integer > 2) {
                            parserConfig.nextIndex();
                        }
                    }
                })
                .add(new TypeCheckRunnable(ListUtil.asList(Integer.class, Integer.class, Integer.class,
                        Comment.class)) {
                    @Override
                    public void run(Object[] objects, ParserConfig parserConfig) {
                        model.setBackgroundColor(
                                new Color((Integer) objects[0],
                                        (Integer) objects[1],
                                        (Integer) objects[2]));
                        parserConfig.nextIndex();
                    }
                })
                .add(new TypeCheckRunnable(ListUtil.asList(Integer.class, Comment.class)) {
                    @Override
                    public void run(Object[] objects, ParserConfig parserConfig) {
                        countOfShapes.integer = (Integer) objects[0];
                        parserConfig.nextIndex();
                    }
                });

        final BSplineBuilder[] BSplineBuilder = {new BSplineBuilder()};
            IntegerWrapper rowCounterForMatrix = new IntegerWrapper(0);
            Matrix roundMatrix = new Matrix(new float[3][3]);
            BSplineBuilder[0].withRoundMatrix(roundMatrix);
            IntegerWrapper countOfPoints = new IntegerWrapper(0);
            IntegerWrapper currentParserRunnableIndex = new IntegerWrapper(0);
            IntegerWrapper currentShape = new IntegerWrapper(0);

            parserConfigBuilder
                    .add(new TypeCheckRunnable(ListUtil.asList(Integer.class, Integer.class, Integer.class,
                            Comment.class)) {
                        @Override
                        public void run(Object[] objects, ParserConfig parserConfig) {
                            BSplineBuilder[0].withColor(new Color((Integer) objects[0],
                                    (Integer) objects[1],
                                    (Integer) objects[2]));
                            currentParserRunnableIndex.integer = parserConfig.getCurrentRunnableIndex();
                            parserConfig.nextIndex();
                        }
                    })
                    .add(new TypeCheckRunnable(ListUtil.asList(Integer.class, Integer.class, Integer.class,
                            Comment.class)) {
                        @Override
                        public void run(Object[] objects, ParserConfig parserConfig) {
                            BSplineBuilder[0]
                                    .withCx((Integer) objects[0])
                                    .withCy((Integer) objects[1])
                                    .withCz((Integer) objects[2]);

                            parserConfig.nextIndex();
                        }
                    })
                    .add(new TypeCheckRunnable(ListUtil.asList(Integer.class, Integer.class, Integer.class,
                            Comment.class)) {
                        @Override
                        public void run(Object[] objects, ParserConfig parserConfig) {
                            for (int k = 0; k < 3; ++k) {
                                roundMatrix.set(rowCounterForMatrix.integer, k, (Integer) objects[k]);
                            }

                            if (++rowCounterForMatrix.integer > 2) {
                                parserConfig.nextIndex();
                            }
                        }
                    })
                    .add(new TypeCheckRunnable(ListUtil.asList(Integer.class, Comment.class)) {
                        @Override
                        public void run(Object[] objects, ParserConfig parserConfig) {
                            countOfPoints.integer = (Integer) objects[0];
                            parserConfig.nextIndex();
                        }
                    });


        final IntegerWrapper[] currentPoint = {new IntegerWrapper(0)};
        parserConfigBuilder.add(new TypeCheckRunnable(ListUtil.asList(Float.class, Float.class,
                Comment.class)) {
            @Override
            public void run(Object[] objects, ParserConfig parserConfig) {
                BSplineBuilder[0].addPoint((Float) objects[0], (Float) objects[1]);
                if (++currentPoint[0].integer >= countOfPoints.integer) {
                    try {
                        model.addShape(BSplineBuilder[0].build());
                    } catch (ShapeBuildingException e) {
                        Logger.getLogger(ModelLoader.class.getName()).log(Level.WARNING,
                                "BSpline was not added. Builder worked not properly");
                    }
                    ++currentShape.integer;
                    rowCounterForMatrix.integer = 0;
                    currentPoint[0].integer = 0;
                    parserConfig.setCurrentRunnableIndex(currentParserRunnableIndex.integer);
                    BSplineBuilder[0] = new BSplineBuilder();
                    BSplineBuilder[0].withRoundMatrix(roundMatrix);
                }
            }
        });

        Parser parser = new Parser(file, parserConfigBuilder.build(), ListUtil.asList(5), s -> {
            if (s.isEmpty()) {
                return false;
            }

            String trimmedString = s.trim();

            if (trimmedString.length() > 2) {
                if (trimmedString.substring(0, 2).equals("//")) {
                    return false;
                }
            }

            return true;
        });

    }

    public Model getModel() {
        return model;
    }
}
