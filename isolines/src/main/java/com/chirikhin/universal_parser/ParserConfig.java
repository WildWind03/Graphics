package com.chirikhin.universal_parser;

import java.util.ArrayList;

public class ParserConfig {
    private final ArrayList<MyRunnable> runnables;
    private int currentRunnableIndex = 0;
    private String splitRegex = " ";

    public ParserConfig(ArrayList<MyRunnable> runnables) {
        this.runnables = runnables;
    }

    public void setSplitRegex(String splitRegex) {
        this.splitRegex = splitRegex;
    }

    void execute(String string) throws TypeConversionException, TypeMatchingException, ParserException {
        if (currentRunnableIndex >= runnables.size()) {
            throw new ParserException("Can't choose runnable by offered index!");
        }

        runnables.get(currentRunnableIndex).run(string.split(splitRegex), this);
    }

    public void setCurrentRunnableIndex(int currentRunnableIndex) {
        this.currentRunnableIndex = currentRunnableIndex;
    }

    public int getCurrentRunnableIndex() {
        return currentRunnableIndex;
    }
}
