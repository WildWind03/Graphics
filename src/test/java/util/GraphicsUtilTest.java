package util;


public class GraphicsUtilTest {
    @org.junit.Test
    public void fromCellPositionToCoordinates() throws Exception {
        GraphicsUtil.Point point = GraphicsUtil.fromCellPositionToCoordinates(4, 0 , 5);
        System.out.println(point.getX());
        System.out.println(point.getY());
    }

}