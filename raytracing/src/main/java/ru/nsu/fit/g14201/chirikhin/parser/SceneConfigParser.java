package ru.nsu.fit.g14201.chirikhin.parser;

import chirikhin.support.Point3D;
import chirikhin.universal_parser.MyFactory;
import chirikhin.universal_parser.ParserConfig;
import chirikhin.universal_parser.ParserConfigBuilder;
import chirikhin.universal_parser.TypeCheckRunnable;
import ru.nsu.fit.g14201.chirikhin.model.*;

import java.awt.*;
import java.io.File;

public class SceneConfigParser {
    private static final String COUNT_OF_LIGHT_SOURCES_TAG = "COUNT_OF_LIGHT_SOURCES";
    private static final String BUILDER_TAG = "SPHERE_BUILDER";

    private static final String SPHERE_NAME_IN_CONFIG = "SPHERE";
    private static final String BOX_NAME_IN_CONFIG = "BOX";
    private static final String TRIANGLE_NAME_IN_CONFIG = "TRIANGLE";
    private static final String QUADRANGLE_NAME_IN_CONFIG = "QUADRANGLE";
    private static final String BEFORE_SHAPES_RUNNABLE_INDEX_TAG = "BEFORE_SHAPES_RUNNABLE_INDEX";
    private static final String OPTICAL_CHARACTERISTICS_RUNNABLE_INDEX_TAG = "OPTICAL_CHARACTERISTICS_RUNNABLE_INDEX";
    private static final String POINT_NUM = "POINT_NUM";

    private SceneConfigParser() {

    }

    public static SceneConfig getSceneConfig(File file) throws ParserException {
        try {
            SceneConfigBuilder sceneConfigBuilder = new SceneConfigBuilder();

            new ParserConfigBuilder()
                    .with(new MyFactory()
                            .addTypeMaker(Integer.class, Integer::parseInt)
                            .addTypeMaker(String.class, Object::toString)
                            .addTypeMaker(Float.class, Float::parseFloat)
                            .addTypeMaker(Comment.class, string -> null))
                    .add(new TypeCheckRunnable(Integer.class, Integer.class, Integer.class, Comment.class) {
                        @Override
                        public void run(Object[] objects, ParserConfig parserConfig) {
                            sceneConfigBuilder.withSceneColor(new Color((int) objects[0], (int) objects[1], (int) objects[2]));
                            parserConfig.nextIndex();
                        }
                    }).add(new TypeCheckRunnable(Integer.class, Comment.class) {
                        @Override
                        public void run(Object[] objects, ParserConfig parserConfig) {
                            parserConfig.saveObject(COUNT_OF_LIGHT_SOURCES_TAG, objects[0]);
                            parserConfig.nextIndex();
                        }
                    }).add(new TypeCheckRunnable(Float.class, Float.class, Float.class, Integer.class,
                            Integer.class, Integer.class, Comment.class) {
                        @Override
                        public void run(Object[] objects, ParserConfig parserConfig) {
                            sceneConfigBuilder.withLightSource(new LightSourceBuilder()
                                .withColor(new Color((int) objects[3], (int) objects[4], (int) objects[5]))
                                .withPosition(new Point3D<>((float) objects[0], (float) objects[1], (float) objects[2]))
                                .build());

                            if (parserConfig.getAlreadyExecuted() >= (int) parserConfig.getObject(COUNT_OF_LIGHT_SOURCES_TAG)) {
                                parserConfig.nextIndex();
                                parserConfig.saveObject(OPTICAL_CHARACTERISTICS_RUNNABLE_INDEX_TAG, parserConfig.getCurrentRunnableIndex() + 3);
                            }
                        }
                    }).add(new TypeCheckRunnable(String.class, Comment.class) {
                        @Override
                        public void run(Object[] objects, ParserConfig parserConfig) {
                            parserConfig.saveObject(BEFORE_SHAPES_RUNNABLE_INDEX_TAG, parserConfig.getCurrentRunnableIndex());
                            parserConfig.addEndPoint(parserConfig.getCurrentRunnableIndex());

                            String shapeType = (String) objects[0];
                            if (shapeType.equals(SPHERE_NAME_IN_CONFIG)) {
                                parserConfig.nextIndex();
                                parserConfig.saveObject(BUILDER_TAG, new SphereBuilder());
                                return;
                            }

                            if (shapeType.equals(BOX_NAME_IN_CONFIG)) {
                                parserConfig.addRunnableIndex(4);
                                parserConfig.saveObject(BUILDER_TAG, new BoxBuilder());
                                return;
                            }

                            if (shapeType.equals(TRIANGLE_NAME_IN_CONFIG)) {
                                parserConfig.addRunnableIndex(6);
                                parserConfig.saveObject(BUILDER_TAG, new TriangleBuilder());
                                parserConfig.saveObject(POINT_NUM, 1);
                                return;
                            }

                            if (shapeType.equals(QUADRANGLE_NAME_IN_CONFIG)) {
                                parserConfig.addRunnableIndex(7);
                                parserConfig.saveObject(BUILDER_TAG, new QuadrangleBuilder());
                                parserConfig.saveObject(POINT_NUM, 1);
                            }
                        }
                    }).add(new TypeCheckRunnable(Float.class, Float.class, Float.class, Comment.class) {
                        @Override
                        public void run(Object[] objects, ParserConfig parserConfig) {
                            SphereBuilder sphereBuilder = (SphereBuilder) parserConfig.getObject(BUILDER_TAG);
                            sphereBuilder.setCenter(new Point3D<>((float) objects[0], (float) objects[1], (float) objects[2]));
                            parserConfig.nextIndex();
                        }
                    }).add(new TypeCheckRunnable(Float.class, Comment.class) {
                        @Override
                        public void run(Object[] objects, ParserConfig parserConfig) {
                            SphereBuilder sphereBuilder = (SphereBuilder) parserConfig.getObject(BUILDER_TAG);
                            sphereBuilder.setRadius((float) objects[0]);
                            parserConfig.nextIndex();
                        }
                    }).add(new TypeCheckRunnable(Float.class, Float.class, Float.class,
                            Float.class, Float.class, Float.class, Float.class, Comment.class) {
                        @Override
                        public void run(Object[] objects, ParserConfig parserConfig) {
                            Point3D<Float, Float, Float> diffuseReflectRate = new Point3D<>((float) objects[0], (float) objects[1],
                                    (float) objects[2]);

                            Point3D<Float, Float, Float> mirrorReflectRate = new Point3D<>((float) objects[3], (float) objects[4],
                                    (float) objects[5]);

                            OpticalCharacteristics opticalCharacteristics = new OpticalCharacteristics(diffuseReflectRate,
                                    mirrorReflectRate, (float) objects[6]);

                            ShapeBuilder shapeBuilder = (ShapeBuilder) parserConfig.getObject(BUILDER_TAG);
                            shapeBuilder.setOpticalCharacteristics(opticalCharacteristics);
                            parserConfig.setCurrentRunnableIndex((int) parserConfig.getObject(BEFORE_SHAPES_RUNNABLE_INDEX_TAG));
                            sceneConfigBuilder.withShape(shapeBuilder.build());
                        }
                    }).add(new TypeCheckRunnable(Float.class, Float.class, Float.class, Comment.class) {
                        @Override
                        public void run(Object[] objects, ParserConfig parserConfig) {
                            BoxBuilder boxBuilder = (BoxBuilder) parserConfig.getObject(BUILDER_TAG);
                            boxBuilder.setMinPoint(new Point3D<>((float) objects[0], (float) objects[1], (float) objects[2]));
                            parserConfig.nextIndex();
                        }
                    }).add(new TypeCheckRunnable(Float.class, Float.class, Float.class, Comment.class) {
                        @Override
                        public void run(Object[] objects, ParserConfig parserConfig) {
                            BoxBuilder boxBuilder = (BoxBuilder) parserConfig.getObject(BUILDER_TAG);
                            boxBuilder.setMaxPoint(new Point3D<>((float) objects[0], (float) objects[1],
                                    (float) objects[2]));
                            parserConfig.setCurrentRunnableIndex(
                                    (int) parserConfig.getObject(OPTICAL_CHARACTERISTICS_RUNNABLE_INDEX_TAG));
                        }
                    }).add(new TypeCheckRunnable(Float.class, Float.class, Float.class, Comment.class) {
                        @Override
                        public void run(Object[] objects, ParserConfig parserConfig) {
                            TriangleBuilder triangleBuilder = (TriangleBuilder) parserConfig.getObject(BUILDER_TAG);
                            int pointNum = (int) parserConfig.getObject(POINT_NUM);
                            switch (pointNum) {
                                case 1:
                                    triangleBuilder.setPoint1(new Point3D<>((float) objects[0], (float) objects[1],
                                            (float) objects[2]));
                                    parserConfig.saveObject(POINT_NUM, 2);
                                    break;
                                case 2:
                                    triangleBuilder.setPoint2(new Point3D<>((float) objects[0], (float) objects[1],
                                            (float) objects[2]));
                                    parserConfig.saveObject(POINT_NUM, 3);
                                    break;
                                case 3:
                                    triangleBuilder.setPoint3(new Point3D<>((float) objects[0], (float) objects[1],
                                            (float) objects[2]));
                                    parserConfig.setCurrentRunnableIndex(
                                            (int) parserConfig.getObject(OPTICAL_CHARACTERISTICS_RUNNABLE_INDEX_TAG));
                                    break;
                            }

                        }
                    }).add(new TypeCheckRunnable(Float.class, Float.class, Float.class,
                            Comment.class) {
                        @Override
                        public void run(Object[] objects, ParserConfig parserConfig) {
                            QuadrangleBuilder quadrangleBuilder = (QuadrangleBuilder) parserConfig.getObject(BUILDER_TAG);
                            int pointNum = (int) parserConfig.getObject(POINT_NUM);

                            switch (pointNum) {
                                case 1:
                                    quadrangleBuilder.setPoint1(new Point3D<>((float) objects[0], (float) objects[1],
                                            (float) objects[2]));
                                    parserConfig.saveObject(POINT_NUM, 2);
                                    break;
                                case 2:
                                    quadrangleBuilder.setPoint2(new Point3D<>((float) objects[0], (float) objects[1],
                                            (float) objects[2]));
                                    parserConfig.saveObject(POINT_NUM, 3);
                                    break;
                                case 3:
                                    quadrangleBuilder.setPoint3(new Point3D<>((float) objects[0], (float) objects[1],
                                            (float) objects[2]));
                                    parserConfig.saveObject(POINT_NUM, 4);
                                    break;
                                case 4:
                                    quadrangleBuilder.setPoint4(new Point3D<>((float) objects[0], (float) objects[1],
                                            (float) objects[2]));
                                    parserConfig.setCurrentRunnableIndex(
                                            (int) parserConfig.getObject(OPTICAL_CHARACTERISTICS_RUNNABLE_INDEX_TAG));
                            }
                        }
                    }).build()
                    .execute(file, s -> {
                        if (s.isEmpty()) {
                            return false;
                        }

                        String trimmedString = s.trim();

                        if (trimmedString.length() >= 2) {
                            if (trimmedString.substring(0, 2).equals("//")) {
                                return false;
                            }
                        }

                        return true;
                    });

            return sceneConfigBuilder.build();
        } catch(Exception e) {
            throw new ParserException(e.getMessage(), e);
        }
    }


}
