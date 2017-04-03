package com.chirikhin.universal_parser;

import org.junit.Assert;

public class MyFactoryTest {
    @org.junit.Test
    public void createObject() throws Exception {
        MyFactory myFactory = new MyFactory();
        myFactory.addTypeMaker(Double.class, string -> {
            try {
                return Double.parseDouble(string);
            } catch (Exception e) {
                throw new TypeConversionException("Can not convert string '" + string + "' to double");
            }
        });

        double num = (double) myFactory.createObject("56", Double.class);
        Assert.assertTrue(num == 56d);
    }

}