package ru.nsu.fit.g14201.chirikhin.parser;

import chirikhin.support.Point3D;
import chirikhin.universal_parser.MyFactory;
import chirikhin.universal_parser.ParserConfig;
import chirikhin.universal_parser.ParserConfigBuilder;
import chirikhin.universal_parser.TypeCheckRunnable;
import ru.nsu.fit.g14201.chirikhin.model.RenderSettings;
import ru.nsu.fit.g14201.chirikhin.model.RenderSettingsBuilder;

import java.awt.*;
import java.io.File;

public class RenderConfigParser {
    private RenderConfigParser() {

    }

    public static RenderSettings getRenderSettings(File file) throws ParserException {
        RenderSettingsBuilder renderSettingsBuilder = new RenderSettingsBuilder();

        try {
            new ParserConfigBuilder().with(new MyFactory()
            .addTypeMaker(Integer.class, Integer::parseInt)
            .addTypeMaker(Float.class, Float::parseFloat)
            .addTypeMaker(String.class, string -> string)
            .addTypeMaker(Comment.class, string -> null))
                    .add(new TypeCheckRunnable(Integer.class, Integer.class, Integer.class, Comment.class) {
                        @Override
                        public void run(Object[] objects, ParserConfig parserConfig) {
                            renderSettingsBuilder.setBackgroundColor(new Color((int) objects[0], (int) objects[1], (int) objects[2]));
                            parserConfig.nextIndex();
                        }
                    }).add(new TypeCheckRunnable(Float.class, Comment.class) {
                @Override
                public void run(Object[] objects, ParserConfig parserConfig) {
                    renderSettingsBuilder.setGamma((float) objects[0]);
                    parserConfig.nextIndex();
                }
            }).add(new TypeCheckRunnable(Float.class, Comment.class) {
                @Override
                public void run(Object[] objects, ParserConfig parserConfig) {
                    renderSettingsBuilder.setDepth((float) objects[0]);
                    parserConfig.nextIndex();
                }
            }).add(new TypeCheckRunnable(String.class, Comment.class) {
                @Override
                public void run(Object[] objects, ParserConfig parserConfig) {
                    renderSettingsBuilder.setQuality((String) objects[0]);
                    parserConfig.nextIndex();
                }
            }).add(new TypeCheckRunnable(Float.class, Float.class, Float.class, Comment.class) {
                @Override
                public void run(Object[] objects, ParserConfig parserConfig) {
                    renderSettingsBuilder.setCameraPoint(new Point3D<>((float) objects[0], (float) objects[1], (float) objects[2]));
                    parserConfig.nextIndex();
                }
            }).add(new TypeCheckRunnable(Float.class, Float.class, Float.class, Comment.class) {
                @Override
                public void run(Object[] objects, ParserConfig parserConfig) {
                    renderSettingsBuilder.setViewPoint(new Point3D<>((float) objects[0], (float) objects[1], (float) objects[2]));
                    parserConfig.nextIndex();
                }
            }).add(new TypeCheckRunnable(Float.class, Float.class, Float.class, Comment.class) {
                @Override
                public void run(Object[] objects, ParserConfig parserConfig) {
                    renderSettingsBuilder.setUpVector(new Point3D<>((float) objects[0], (float) objects[1], (float) objects[2]));
                    parserConfig.nextIndex();
                }
            }).add(new TypeCheckRunnable(Float.class, Float.class, Comment.class) {
                @Override
                public void run(Object[] objects, ParserConfig parserConfig) {
                    renderSettingsBuilder.setZn((float) objects[0]);
                    renderSettingsBuilder.setZf((float) objects[1]);
                    parserConfig.nextIndex();
                }
            }).add(new TypeCheckRunnable(Float.class, Float.class, Comment.class) {
                @Override
                public void run(Object[] objects, ParserConfig parserConfig) {
                    renderSettingsBuilder.setSw((float) objects[0]);
                    renderSettingsBuilder.setSh((float) objects[1]);
                    parserConfig.nextIndex();
                }
            }).build().execute(file, s -> {
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

            return renderSettingsBuilder.createRenderSettings();
        } catch (Exception e) {
            throw new ParserException(e.getMessage(), e);
        }

    }
}
