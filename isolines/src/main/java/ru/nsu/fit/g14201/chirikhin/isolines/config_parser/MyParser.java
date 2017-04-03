package ru.nsu.fit.g14201.chirikhin.isolines.config_parser;

import com.chirikhin.universal_parser.*;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;

public class MyParser {
    private final int k;
    private final int m;
    private final int n;
    private final LinkedList<Integer[]> isolinesColor;
    private final LinkedList<Integer[]> legendColor;


    public MyParser(File file) throws TypeConversionException, ParserException, TypeMatchingException {
        ArrayList<Class<?>> firstLinesParams = new ArrayList<>();
        firstLinesParams.add(Integer.class);
        firstLinesParams.add(Integer.class);
        firstLinesParams.add(Comment.class);

        ArrayList<Class<?>> secondLinesParams = new ArrayList<>();
        secondLinesParams.add(Integer.class);
        secondLinesParams.add(Comment.class);

        ArrayList<Class<?>> thirdLinesParams = new ArrayList<>();
        thirdLinesParams.add(Integer.class);
        thirdLinesParams.add(Integer.class);
        thirdLinesParams.add(Integer.class);
        thirdLinesParams.add(Comment.class);

        MyFactory myFactory = new MyFactory();
        myFactory.addTypeMaker(Integer.class, string -> {
            try {
                return Integer.parseInt(string);
            } catch (Exception e) {
                throw new TypeConversionException(e.getMessage());
            }
        });

        myFactory.addTypeMaker(Comment.class, string -> {
            if (!string.substring(0, 2).equals("//")) {
                throw new ParserException("Invalid config. Extra words which are not comments");
            }
            return null;
        });



        ArrayList<Integer> gripValueKM = new ArrayList<>();
        MyRunnable myRunnable1 = new TypeCheckRunnable(firstLinesParams, myFactory) {
            @Override
            public void run(Object[] objects, ParserConfig parserConfig) {
                gripValueKM.add((int) objects[0]);
                gripValueKM.add((int) objects[1]);
                parserConfig.setCurrentRunnableIndex(parserConfig.getCurrentRunnableIndex() + 1);
            }
        };

        ArrayList<Integer> levelCount = new ArrayList<>();
        MyRunnable myRunnable2 = new TypeCheckRunnable(secondLinesParams, myFactory) {
            @Override
            public void run(Object[] objects, ParserConfig parserConfig) {
                levelCount.add((int) objects[0]);
                parserConfig.setCurrentRunnableIndex(parserConfig.getCurrentRunnableIndex() + 1);
            }
        };

        legendColor = new LinkedList<>();
        MyRunnable myRunnable3 = new TypeCheckRunnable(thirdLinesParams, myFactory) {
            private int counter = 0;

            @Override
            public void run(Object[] objects, ParserConfig parserConfig) {
                legendColor.add(new Integer[] {(int) objects[0], (int) objects[1], (int) objects[2]});
                counter++;

                if (counter >= levelCount.get(0)) {
                    parserConfig.setCurrentRunnableIndex(parserConfig.getCurrentRunnableIndex() + 1);
                }
            }
        };

        isolinesColor = new LinkedList<>();
        MyRunnable myRunnable4  = new TypeCheckRunnable(thirdLinesParams, myFactory) {
            @Override
            public void run(Object[] objects, ParserConfig parserConfig) {
                isolinesColor.add(new Integer[] {(int) objects[0], (int) objects[1], (int) objects[2]});
                parserConfig.setCurrentRunnableIndex(parserConfig.getCurrentRunnableIndex() + 1);
            }
        };

        ArrayList<MyRunnable> runnables = new ArrayList<>();
        runnables.add(myRunnable1);
        runnables.add(myRunnable2);
        runnables.add(myRunnable3);
        runnables.add(myRunnable4);

        new Parser(file, new ParserConfig(runnables));
        k = gripValueKM.get(0);
        m = gripValueKM.get(1);
        n = levelCount.get(0);
    }

    public int getK() {
        return k;
    }

    public int getM() {
        return m;
    }

    public int getN() {
        return n;
    }

    public LinkedList<Integer[]> getIsolinesColor() {
        return isolinesColor;
    }

    public LinkedList<Integer[]> getLegendColor() {
        return legendColor;
    }
}
