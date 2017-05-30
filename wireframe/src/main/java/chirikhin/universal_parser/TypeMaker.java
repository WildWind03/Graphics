package chirikhin.universal_parser;

import ru.fit.g14201.chirikhin.wireframe.model_loader.InvalidCommentException;

public interface TypeMaker {
    Object create(String string) throws TypeConversionException, ParserException;
}
