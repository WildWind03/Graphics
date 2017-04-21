package chirikhin.universal_parser;

import java.io.*;
import java.nio.charset.Charset;

public class Parser {
    public Parser(File file, ParserConfig parserConfig) throws ParserException, TypeConversionException, TypeMatchingException {
        try (BufferedReader bufferedReader =
                     new BufferedReader(
                             new InputStreamReader(
                                     new FileInputStream(file), Charset.forName("UTF-8")))) {
            String nextString;
            while ((nextString = bufferedReader.readLine()) != null) {
                if (!nextString.isEmpty()) {
                    parserConfig.execute(nextString);
                }
            }
        } catch (IOException e) {
            throw new ParserException(e.getMessage(), e);
        }
    }
}
