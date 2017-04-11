package com.chirikhin.universal_parser;

public interface MyRunnable {
    void run(String[] strings, ParserConfig parserConfig) throws TypeConversionException, TypeMatchingException, ParserException;
}
