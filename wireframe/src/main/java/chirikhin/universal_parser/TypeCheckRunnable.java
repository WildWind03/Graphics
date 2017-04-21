package chirikhin.universal_parser;

import java.util.ArrayList;

public abstract class TypeCheckRunnable implements MyRunnable {

    private final ArrayList<Class<?>> types;
    private final ObjectFactory objectFactory;

    private boolean isLastArgumentMandatory = false;

    public TypeCheckRunnable(ArrayList<Class<?>> types, ObjectFactory objectFactory) throws TypeConversionException {
        this.objectFactory = objectFactory;
        this.types = types;
    }

    public void setLastArgumentsBehavior(boolean isMandatory) {
        this.isLastArgumentMandatory = isMandatory;
    }

    public void run(String[] strings, ParserConfig parserConfig)
            throws TypeConversionException, TypeMatchingException, ParserException {
        if (strings.length < types.size() - (isLastArgumentMandatory ? 0 : 1)) {
            throw new TypeMatchingException("Different count of arguments in the type mask and in the config file");
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
            }
        }

        run(objects, parserConfig);
    }

    public abstract void run(Object[] objects, ParserConfig parserConfig);
}
