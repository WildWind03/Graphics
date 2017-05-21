package chirikhin.universal_parser;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Predicate;

public class ParserConfig {
    private final ArrayList<TypeCheckRunnable> runnables;
    private final MyFactory objectFactory;
    private int currentRunnableIndex = 0;
    private String splitRegex = " ";
    private int alreadyExecuted = 0;

    private final HashMap<String, Object> savedObjects = new HashMap<>();
    private final List<Integer> endPoints = new ArrayList<>();

    public ParserConfig(ArrayList<TypeCheckRunnable> runnables, MyFactory objectFactory) {
        this.runnables = runnables;
        this.objectFactory = objectFactory;
    }

    public void setSplitRegex(String splitRegex) {
        this.splitRegex = splitRegex;
    }

    public void execute(File file, Predicate<String> stringFilter) throws ParserException, TypeConversionException, TypeMatchingException {
        try (BufferedReader bufferedReader =
                     new BufferedReader(
                             new InputStreamReader(
                                     new FileInputStream(file), Charset.forName("UTF-8")))) {
            String nextString;
            while ((nextString = bufferedReader.readLine()) != null) {
                if (stringFilter.test(nextString)) {
                    execute(nextString);
                }
            }
        } catch (IOException e) {
            throw new ParserException(e.getMessage(), e);
        }

        for (Integer endPoint : endPoints) {
            if (endPoint != getCurrentRunnableIndex()) {
                throw new ParserException("At the end of working the parser is in not finish state");
            }
        }
    }

    public void addEndPoint(int endPoint) {
        endPoints.add(endPoint);
    }

    private void execute(String string) throws TypeConversionException, TypeMatchingException, ParserException {
        if (currentRunnableIndex >= runnables.size()) {
            throw new ParserException("Can't choose runnable by offered index!");
        }

        runnables.get(currentRunnableIndex).run(string.split(splitRegex), this, objectFactory);
        alreadyExecuted++;
    }

    public void nextIndex() {
        setCurrentRunnableIndex(getCurrentRunnableIndex() + 1);
    }

    public void saveObject(String tag, Object savedObject) {
        this.savedObjects.put(tag, savedObject);
    }

    public Object getObject(String tag) {
        return this.savedObjects.get(tag);
    }

    public void setCurrentRunnableIndex(int currentRunnableIndex) {
        this.currentRunnableIndex = currentRunnableIndex;
        alreadyExecuted = 0;
    }

    public int getAlreadyExecuted() {
        return alreadyExecuted;
    }

    public void addRunnableIndex(int count) {
        setCurrentRunnableIndex(getCurrentRunnableIndex() + count);
    }

    public int getCurrentRunnableIndex() {
        return currentRunnableIndex;
    }
}
