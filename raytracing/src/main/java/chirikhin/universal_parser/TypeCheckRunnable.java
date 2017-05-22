package chirikhin.universal_parser;

import chirikhin.swing.util.ListUtil;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public abstract class TypeCheckRunnable {

    private final ArrayList<Class<?>> types;

    private boolean isLastArgumentMandatory = false;

    public TypeCheckRunnable(ArrayList<Class<?>> types) {
        this.types = types;
    }

    public TypeCheckRunnable(Class<?>... classes){
        types = ListUtil.asList(classes);
    }

    public void setLastArgumentsBehavior(boolean isMandatory) {
        this.isLastArgumentMandatory = isMandatory;
    }

    public void run(String[] strings, ParserConfig parserConfig, MyFactory objectFactory)
            throws TypeConversionException, TypeMatchingException, ParserException {
        if (strings.length < types.size() - (isLastArgumentMandatory ? 0 : 1)) {
            throw new TypeMatchingException("Different count of arguments in the type mask and in the config file." +
                    " Your strings are " + Arrays.toString(strings) + " Types are " + Arrays.toString(new ArrayList[]{types}));
        }

        Object[] objects = new Object[types.size()];

        for (int i = 0; i < types.size(); ++i) {
            if (!isLastArgumentMandatory && i == types.size() - 1) {
                if (strings.length < types.size()) {
                    objects[i] = null;
                    break;
                }
            }
            try {
                objects[i] = objectFactory.createObject(strings[i], types.get(i));
            } catch (ArrayIndexOutOfBoundsException e) {
                throw new TypeMatchingException("Real data doesn't fit to arg map");
            } catch (Exception e) {
                throw new TypeConversionException("Error while converting", e);
            }
        }

        run(objects, parserConfig);
    }

    public abstract void run(Object[] objects, ParserConfig parserConfig);
}
