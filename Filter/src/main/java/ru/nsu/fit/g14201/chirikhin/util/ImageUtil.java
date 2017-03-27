package ru.nsu.fit.g14201.chirikhin.util;

import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.image.*;

public class ImageUtil {
    public static BufferedImage deepCopy(BufferedImage source) {
        if (null == source) {
            return null;
        }

        BufferedImage b = new BufferedImage(source.getWidth(), source.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics g = b.getGraphics();
        g.drawImage(source, 0, 0, null);
        g.dispose();
        return b;
    }

    public static BufferedImage getSubimage(BufferedImage bufferedImage, int startX, int startY, int width, int height) {
        return createImageFromBytes(getSubimageBytes(bufferedImage, startX, startY, width, height), width, height);
    }

    private static byte[] getByteData(BufferedImage userSpaceImage) {
        WritableRaster raster = userSpaceImage.getRaster();
        DataBufferByte buffer = (DataBufferByte) raster.getDataBuffer();
        return buffer.getData();
    }

    private static BufferedImage createImageFromBytes(byte[] imageData, int width, int height) {
        ColorModel colorModel = new ComponentColorModel(ColorSpace.getInstance(ColorSpace.CS_sRGB), new int[]{8, 8, 8}, false, true, Transparency.OPAQUE, DataBuffer.TYPE_BYTE);
        SampleModel sampleModel = colorModel.createCompatibleSampleModel(width, height);

        return new BufferedImage(colorModel,
                Raster.createWritableRaster(sampleModel,
                        new DataBufferByte(imageData, imageData.length),
                        null),
                colorModel.isAlphaPremultiplied(),
                null);
    }

    public static byte[] getSubimageBytes(BufferedImage bufferedImage, int startX, int startY, int width, int height) {
        byte[] imageBytes = getByteData(bufferedImage);

        int currentSrcPosition = (startY * bufferedImage.getWidth() + startX) * 3;
        int currentDstPosition = 0;
        byte[] newImageBytes = new byte[width * height * 3];

        for (int currentHeight = startY; currentHeight < startY + height; currentHeight++) {
            for (int k = currentSrcPosition; k < currentSrcPosition + width * 3; k+=3) {
                newImageBytes[currentDstPosition++] = imageBytes[k + 2];
                newImageBytes[currentDstPosition++] = imageBytes[k + 1];
                newImageBytes[currentDstPosition++] = imageBytes[k];
            }

            currentSrcPosition += (bufferedImage.getWidth() * 3);
        }

        return newImageBytes;
    }

    public static void drawDashedRectangle(Graphics g, int x, int y, int width, int height){
        Graphics2D g2d = (Graphics2D) g.create();
        Stroke dashed = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{9}, 0);
        g2d.setStroke(dashed);
        g2d.drawRect(x, y, width, height);
        g2d.dispose();
    }

    public static void drawDashedLine(BufferedImage g, int x, int y, int endX, int endY){
        Graphics2D g2d = g.createGraphics();
        g2d.setColor(Color.BLACK);
        Stroke dashed = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{9}, 0);
        g2d.setStroke(dashed);
        g2d.drawLine(x, y, endX, endY);
        g2d.dispose();
    }

}
