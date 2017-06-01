package ru.nsu.fit.g14201.chirikhin.view;

import chirikhin.support.Line;
import chirikhin.support.Point3D;
import ru.nsu.fit.g14201.chirikhin.model.Sphere;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;


public class SphereDrawer extends Drawer {
    private final Sphere sphere;
    private final Color color;

    private final List<Line<Point3D<Float, Float, Float>>> lines = new ArrayList<>();

    public SphereDrawer(Sphere sphere, Color color, int k) {
        this.sphere = sphere;
        this.color = color;

        int step = 360 / k;

        for (int x = 0; x < 360; x+=step) {
            float currentAngleInRadiansX = (float) Math.toRadians(x);
            float nextAngleInRadiansX = (float) Math.toRadians(x + step);
            for (int y = 0; y < 360; y+=step) {
                float currentAngleInRadiansY = (float) Math.toRadians(y);
                float nextAngleInRadiansY = (float) Math.toRadians(y + step);

                Point3D<Float, Float, Float> startPoint = new Point3D<>(
                        (float) (sphere.getCenter().getX() + sphere.getRadius() * Math.cos(currentAngleInRadiansX) * Math.sin(currentAngleInRadiansY)),
                        (float) (sphere.getCenter().getY() + sphere.getRadius() * Math.sin(currentAngleInRadiansX) * Math.sin(currentAngleInRadiansY)),
                        (float) (sphere.getCenter().getZ() + sphere.getRadius() * Math.cos(currentAngleInRadiansY)));

                Point3D<Float, Float, Float> endPointX = new Point3D<>(
                        (float) (sphere.getCenter().getX() + sphere.getRadius() * Math.cos(nextAngleInRadiansX) * Math.sin(currentAngleInRadiansY)),
                        (float) (sphere.getCenter().getY() + sphere.getRadius() * Math.sin(nextAngleInRadiansX) * Math.sin(currentAngleInRadiansY)),
                        (float) (sphere.getCenter().getZ() + sphere.getRadius() * Math.cos(currentAngleInRadiansY)));

                Point3D<Float, Float, Float> endPointY = new Point3D<>(
                        (float) (sphere.getCenter().getX() + sphere.getRadius() * Math.cos(nextAngleInRadiansX) * Math.sin(nextAngleInRadiansY)),
                        (float) (sphere.getCenter().getY() + sphere.getRadius() * Math.sin(nextAngleInRadiansX) * Math.sin(nextAngleInRadiansY)),
                        (float) (sphere.getCenter().getZ() + sphere.getRadius() * Math.cos(nextAngleInRadiansY)));

                lines.add(new Line<>(startPoint, endPointX));
                lines.add(new Line<>(startPoint, endPointY));
            }
        }

    }

    @Override
    public void draw(ShapeView shapeView) {
        lines.forEach(point3DLine -> shapeView.drawLine(point3DLine.getStart(), point3DLine.getEnd(), color));
    }
}
