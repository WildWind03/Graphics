package chirikhin.clipper;

import chirikhin.support.Line;
import chirikhin.support.Point3D;
import org.junit.Test;

public class Clipper3DTest {

    @Test
    public void testClipping() {
        Clipper3D clipper3D = new Clipper3D(1, 1, 1, 0, 0, 0);
        clipper3D.getClippedLine(new Line<>(new Point3D<>(0.5f, 0.5f, 2f),
                new Point3D<>(0.5f, 0.5f, 0f)));

    }
}