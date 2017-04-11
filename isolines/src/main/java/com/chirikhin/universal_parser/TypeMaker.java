package com.chirikhin.universal_parser;

public interface TypeMaker {
    Object create(String string) throws TypeConversionException, ParserException;
}
