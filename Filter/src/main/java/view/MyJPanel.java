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
    private static final int ZONE_SIZE = 350;

    private static final int GAP_BETWEEN_ZONES = 20;
    private static final String INVALID_FILE = "Invalid file";

    private int selectRectSize = ZONE_SIZE;
    private Point point;

    private BufferedImage zoneA;
    private BufferedImage zoneB;
    private BufferedImage zoneC;

    public MyJPanel() {

        setPreferredSize(new Dimension(ZONE_SIZE * 3 + GAP_BETWEEN_ZONES * 3, ZONE_SIZE + GAP_BETWEEN_ZONES));
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);

                if (null != zoneA) {
                    int startX = e.getX() - selectRectSize / 2;

                    if (startX < 1) {
                        startX = 1;
                    }

                    if (e.getX() + selectRectSize / 2 > ZONE_SIZE) {
                        startX -= (e.getX() + selectRectSize / 2 - (ZONE_SIZE));
                    }

                    int startY = e.getY() - selectRectSize / 2;

                    if (startY < 1) {
                        startY = 1;
                    }

                    if (e.getY() + selectRectSize / 2 > ZONE_SIZE) {
                        startY -= (e.getY() + selectRectSize / 2 - (ZONE_SIZE));
                    }

                    point = new Point(startX, startY);
                    repaint();
                }
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);
                mousePressed(e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                point = null;
                repaint();
            }
        });
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

        if (null != point) {
            drawDashedRectangle(g, point.x, point.y, selectRectSize, selectRectSize);
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
            throw new LoadImageException (INVALID_FILE);
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

                selectRectSize = (int) (ZONE_SIZE / k);
            } else {
                float k = height / ZONE_SIZE;

                newHeight = ZONE_SIZE - 1;
                newWidth = (int) (width / k);

                selectRectSize = (int) (ZONE_SIZE / k);
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
