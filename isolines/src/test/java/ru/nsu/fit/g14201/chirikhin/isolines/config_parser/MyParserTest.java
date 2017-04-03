package ru.nsu.fit.g14201.chirikhin.isolines.config_parser;

import com.chirikhin.universal_parser.ParserException;
import com.chirikhin.universal_parser.TypeConversionException;
import com.chirikhin.universal_parser.TypeMatchingException;
import org.junit.Test;

import java.io.File;


public class MyParserTest {
    @Test
    public void testParser() {
        try {
            MyParser myParser = new MyParser(new File("./FIT_g14201_Chirikhin_Filter_Data/config.txt"));
        } catch (TypeConversionException | ParserException | TypeMatchingException e) {
            e.printStackTrace();
        }
    }

}