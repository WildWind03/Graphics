package view;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class MyJPanel extends JPanel {

    //private static final int ZONE_HEIGHT = 350;
    //private static final int ZONE_WIDTH = 350;

    private static final int ZONE_SIZE = 350;

    private static final int GAP_BETWEEN_ZONES = 20;

    private BufferedImage zoneA;
    private BufferedImage zoneB;
    private BufferedImage zoneC;

    public MyJPanel() {
        //zoneA = new BufferedImage(ZONE_WIDTH, ZONE_HEIGHT, BufferedImage.TYPE_INT_RGB);
        //zoneB = new BufferedImage(ZONE_WIDTH, ZONE_HEIGHT, BufferedImage.TYPE_INT_RGB);
       // zoneC = new BufferedImage(ZONE_WIDTH, ZONE_HEIGHT, BufferedImage.TYPE_INT_RGB);

        setPreferredSize(new Dimension(ZONE_SIZE * 3 + GAP_BETWEEN_ZONES * 3, ZONE_SIZE + GAP_BETWEEN_ZONES));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawDashedRectangle(g, 0, 0, ZONE_SIZE, ZONE_SIZE);
        drawDashedRectangle(g, ZONE_SIZE + GAP_BETWEEN_ZONES, 0, ZONE_SIZE, ZONE_SIZE);
        drawDashedRectangle(g, ZONE_SIZE + ZONE_SIZE + GAP_BETWEEN_ZONES + GAP_BETWEEN_ZONES, 0, ZONE_SIZE, ZONE_SIZE);

        if (null != zoneA) {
            g.drawImage(zoneA, 1, 1, null);
        }
    }

    public void drawDashedRectangle(Graphics g, int x, int y, int width, int height){
        Graphics2D g2d = (Graphics2D) g.create();
        Stroke dashed = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{9}, 0);
        g2d.setStroke(dashed);
        g2d.drawRect(x, y, width, height);
        g2d.dispose();
    }

    public void loadNewImage(File file) throws IOException, LoadImageException {
        zoneA = ImageIO.read(file);

        if (null == zoneA) {
            throw new LoadImageException ("Invalid file");
        }

        int height = zoneA.getHeight();
        int width = zoneA.getWidth();

        int maxSize = height > width ? height : width;

        if (maxSize >= ZONE_SIZE) {
            int newWidth;
            int newHeight;

            if (width > height) {
                float k = width / ZONE_SIZE;

                newWidth = ZONE_SIZE - 1;
                newHeight = (int) (height / k);
            } else {
                float k = height / ZONE_SIZE;

                newHeight = ZONE_SIZE - 1;
                newWidth = (int) (width / k);
            }

            Image image = zoneA.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
            zoneA = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);

            Graphics2D g2d = zoneA.createGraphics();
            g2d.drawImage(image, 0, 0, null);
            g2d.dispose();

        }


        repaint();
    }
}
