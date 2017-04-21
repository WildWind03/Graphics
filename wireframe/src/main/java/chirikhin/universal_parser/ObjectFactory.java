package chirikhin.universal_parser;

public interface ObjectFactory {
    <T> Object createObject(String string, Class<T> clazz) throws TypeConversionException, ParserException;
}
