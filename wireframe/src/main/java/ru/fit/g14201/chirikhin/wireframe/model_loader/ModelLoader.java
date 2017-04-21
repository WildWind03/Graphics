package ru.fit.g14201.chirikhin.wireframe.model_loader;

import chirikhin.matrix.Matrix;
import chirikhin.swing.util.ListUtil;
import chirikhin.universal_parser.*;
import ru.fit.g14201.chirikhin.wireframe.model.Model;
import ru.fit.g14201.chirikhin.wireframe.model.ShapeBuilder;
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
        objectFactory.addTypeMaker(Comment.class, (string) -> null);

        ParserConfigBuilder parserConfigBuilder = new ParserConfigBuilder()
                .with(objectFactory)
                .add(new TypeCheckRunnable(ListUtil.asList(Integer.class,
                        Integer.class, Integer.class, Integer.class, Integer.class,
                        Integer.class, Comment.class)) {
                    @Override
                    public void run(Object[] objects, ParserConfig parserConfig) {
                        model.setN((Integer) objects[0]);
                        model.setM((Integer) objects[1]);
                        model.setA((Integer) objects[2]);
                        model.setB((Integer) objects[3]);
                        model.setC((Integer) objects[4]);
                        model.setD((Integer) objects[5]);
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

            ShapeBuilder shapeBuilder = new ShapeBuilder();
            IntegerWrapper rowCounterForMatrix = new IntegerWrapper(0);
            Matrix roundMatrix = new Matrix(new float[3][3]);
            shapeBuilder.withRoundMatrix(roundMatrix);
            IntegerWrapper countOfPoints = new IntegerWrapper(0);
            IntegerWrapper currentParserRunnableIndex = new IntegerWrapper(0);
            IntegerWrapper currentShape = new IntegerWrapper(0);

            parserConfigBuilder
                    .add(new TypeCheckRunnable(ListUtil.asList(Integer.class, Integer.class, Integer.class,
                            Comment.class)) {
                        @Override
                        public void run(Object[] objects, ParserConfig parserConfig) {
                            shapeBuilder.withColor(new Color((Integer) objects[0],
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
                            shapeBuilder
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


        IntegerWrapper currentPoint = new IntegerWrapper(0);
        parserConfigBuilder.add(new TypeCheckRunnable(ListUtil.asList(Integer.class, Integer.class,
                Comment.class)) {
            @Override
            public void run(Object[] objects, ParserConfig parserConfig) {
                shapeBuilder.addPoint((Integer) objects[0], (Integer) objects[1]);
                if (++currentPoint.integer >= countOfPoints.integer) {
                    try {
                        model.addShape(shapeBuilder.build());
                    } catch (ShapeBuildingException e) {
                        Logger.getLogger(ModelLoader.class.getName()).log(Level.WARNING,
                                "Shape was not added. Builder worked not properly");
                    }
                    ++currentShape.integer;
                    parserConfig.setCurrentRunnableIndex(currentParserRunnableIndex.integer);
                }
            }
        });

        Parser parser = new Parser(file, parserConfigBuilder.build());
        System.out.println(model.getShapes().get(0).getRoundMatrix().get(0, 0));

    }

    public Model getModel() {
        return model;
    }
}
