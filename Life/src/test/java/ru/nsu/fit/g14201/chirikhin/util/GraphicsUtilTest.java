package ru.nsu.fit.g14201.chirikhin.util;


import ru.nsu.fit.g14201.chirikhin.support.Point;

public class GraphicsUtilTest {
    @org.junit.Test
    public void fromCellPositionToCoordinates() throws Exception {
        Point point = GraphicsUtil.fromCellPositionToCoordinates(4, 0 , 5);
        System.out.println(point.getX());
        System.out.println(point.getY());
    }

}