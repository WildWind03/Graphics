package chirikhin.clipper;

import chirikhin.support.Line;
import chirikhin.support.Point3D;

public class Clipper3D {
    private static final int INSIDE = 0;
    private static final int LEFT = 1;
    private static final int RIGHT = 2;
    private static final int BOTTOM = 4;
    private static final int TOP = 8;
    private static final int FRONT = 16;
    private static final int BACK = 32;

    private final float maxX;
    private final float maxY;
    private final float maxZ;
    private final float minX;
    private final float minY;
    private final float minZ;

    public Clipper3D(float maxX, float maxY, float maxZ, float minX, float minY, float minZ) {
        this.maxX = maxX;
        this.maxY = maxY;
        this.maxZ = maxZ;
        this.minX = minX;
        this.minY = minY;
        this.minZ = minZ;
    }

    private int getOutCode(Point3D<Float, Float, Float> point3D) {
        int outcode = INSIDE;

        if (point3D.getX() < minX) {
            outcode |= LEFT;
        } else {
            if (point3D.getX() > maxX) {
                outcode |= RIGHT;
            }
        }

        if (point3D.getY() < minY) {
            outcode |= BOTTOM;
        } else {
            if (point3D.getY() > maxY) {
                outcode |= TOP;
            }
        }

        if (point3D.getZ() < minZ) {
            outcode |= FRONT;
        } else {
            if (point3D.getZ() > maxZ) {
                outcode |= BACK;
            }
        }

        return outcode;
    }

    public Line<Point3D<Float, Float, Float>> getClippedLine(Line<Point3D<Float, Float, Float>> srcLine) {
        Point3D<Float, Float, Float> startPoint = srcLine.getStart();
        Point3D<Float, Float, Float> endPoint = srcLine.getEnd();

        float x0 = startPoint.getX();
        float x1 = endPoint.getX();
        float y0 = startPoint.getY();
        float y1 = endPoint.getY();
        float z0 = startPoint.getZ();
        float z1 = endPoint.getZ();

        int startOutCode = getOutCode(startPoint);
        int endOutCode = getOutCode(endPoint);

        boolean accept = false;

        while (true) {
            if (0 == (startOutCode | endOutCode)) {
                accept = true;
                break;
            } else {
                if ((startOutCode & endOutCode) != 0) {
                    break;
                } else {
                    float x = 0;
                    float y = 0;
                    float z = 0;

                    int outCode = startOutCode != 0 ? startOutCode : endOutCode;

                    if ((outCode & TOP) != 0) {
                        x = x0 + (x1 - x0) * (maxY - y0) /
                                (y1 - y0);
                        y = maxY;
                        z = z0 + (z1 - z0) * (maxY - y0) /
                                (y1 - y0);
                    } else if ((outCode & BOTTOM) != 0) {
                        x = x0 + (x1 - x0) * (minY - y0) /
                                (y1 - y0);
                        y = minY;
                        z = z0 + (z1 - z0) * (minY - y0) /
                                (y1 - y0);
                    } else if ((outCode & RIGHT) != 0) {
                        y = y0 + (y1 - y0) * (maxX - x0) /
                                (x1 - x0);
                        x = maxX;
                        z = z0 + (z1 - z0) * (maxX - x0) /
                                (x1 - x0);

                    } else if ((outCode & LEFT) != 0) {
                        y = y0 + (y1 - y0) * (minX - x0) /
                                (x1 - x0);
                        x = minX;
                        z = z0 + (z1 - z0) * (minX - x0) /
                                (x1 - x0);

                    } else if ((outCode & BACK) != 0) {
                        x = x0 + (x1 - x0) * (maxZ - z0) /
                                (z1 - z0);

                        y = y0 + (y1 - y0) * (maxZ - z0) /
                                (z1 - z0);
                        z = maxZ;
                    } else if ((outCode & FRONT) != 0) {
                        x = x0 + (x1 - x0) * (minZ - z0) /
                                (z1 - z0);

                        y = y0 + (y1 - y0) * (minZ - z0) /
                                (z1 - z0);
                        z = minZ;
                    }

                    if (outCode == startOutCode) {
                        x0 = x;
                        y0 = y;
                        z0 = z;
                        startOutCode = getOutCode(new Point3D<>(x0, y0, z0));
                    } else {
                        x1 = x;
                        y1 = y;
                        z1 = z;
                        endOutCode = getOutCode(new Point3D<>(x1, y1, z1));
                    }
                }
            }
        }

        if (accept) {
            return new Line<>(new Point3D<>(x0, y0, z0), new Point3D<>(x1, y1, z1));
        } else {
            return null;
        }
    }
}
