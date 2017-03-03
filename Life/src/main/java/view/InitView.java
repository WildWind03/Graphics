package view;

import model.Field;
import support.*;
import support.Point;
import util.GraphicsUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;

import static java.awt.image.BufferedImage.TYPE_INT_RGB;
import static util.GraphicsUtil.getHalfOfHorizontalLength;

class InitView extends JPanel {
    private static final String FONT_NAME = "Courier New";
    private BufferedImage bufferedImage;

    private int lineLength;
    private int lineWidth = 1;

    private Field currentField = null;
    private int[] fillColor = {0, 255, 0};
    private boolean isImpactShowing = false;

    InitView(int width, int height, final int lineLength) {
        setPreferredSize(new Dimension(width, height));
        setLayout(null);
        setBackground(Color.WHITE);

        bufferedImage = new BufferedImage(width, height, TYPE_INT_RGB);
        this.lineLength = lineLength;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (null != currentField) {
            Graphics2D drawer = (Graphics2D) bufferedImage.getGraphics();
            drawer.setBackground(Color.WHITE);
            drawer.clearRect(0, 0, getWidth(), getHeight());
            drawer.setColor(Color.BLACK);

            if (null != currentField) {
                for (int k = 0; k < currentField.getHeight(); ++k) {

                    int width = (0 == k % 2) ? currentField.getWidth() : currentField.getWidth() - 1;
                    for (int i =0; i < width; ++i) {
                        GraphicsUtil.drawHexagon(bufferedImage, i, k, lineLength, currentField.getHeight());
                    }
                }

                for (int k = 0; k < currentField.getHeight(); ++k) {
                    int width = (0 == k % 2) ? currentField.getWidth() : currentField.getWidth() - 1;

                    for (int i = 0; i < width; ++i) {
                        if (currentField.isAlive(i, k)) {
                            GraphicsUtil.fillHexagon(bufferedImage, i, k, lineLength, fillColor);
                        }

                        if (isImpactShowing) {
                            printImpact(i, k, currentField.getImpact(i, k), bufferedImage);
                        }
                    }
                }
            }

            g.drawImage(bufferedImage, 0, 0, null);
        }
    }

    public void updateSize(int width, int height) {
        bufferedImage = new BufferedImage(width, height, TYPE_INT_RGB);
        setPreferredSize(new Dimension(width, height));
    }

    private void printImpact(int x, int y, double impact, BufferedImage bufferedImage) {
        Point<Integer> point = GraphicsUtil.fromCellPositionToCoordinatesUpd(x, y, lineLength);
        int centerX = point.getX() + lineLength / 2;
        int centerY = point.getY() + getHalfOfHorizontalLength(lineLength);

        String impactStr = Double.toString(impact);

        int dotPos = impactStr.indexOf('.');
        String wholePart = impactStr.substring(0, dotPos);
        String decimalPart = impactStr.substring(dotPos + 1, dotPos + 2);

        Graphics2D graphics2D = (Graphics2D) bufferedImage.getGraphics();

        int fontSize = lineLength / 2;
        graphics2D.setFont(new Font(FONT_NAME, Font.PLAIN, fontSize));
        graphics2D.setColor(new Color(0, 0, 0));

        String impactToPrint;
        int startX;

        if ("0".equals(decimalPart)) {
            impactToPrint = wholePart;
            startX = centerX + lineLength / 4;
        } else {
            impactToPrint = wholePart + "." + decimalPart;
            startX = centerX - lineLength / 9;
        }

        graphics2D.drawString(impactToPrint, startX, centerY - lineLength / 5);
    }

    void drawField(Field field) {
        currentField = field;
        repaint();
    }

    public boolean isCouldBeFilled(int x, int y) {
        if (x>=0 && x < bufferedImage.getWidth() && y >= 0 && y <= bufferedImage.getHeight()) {
            int rgb[] = new int[3];
            bufferedImage.getRaster().getPixel(x, y, rgb);
            return !Arrays.equals(new int[]{0, 0, 0}, rgb);
        }

        return false;
    }

    void changeImpactMode(boolean isImpactShowing) {
        this.isImpactShowing = isImpactShowing;
    }

    public boolean isImpactShowing() {
        return isImpactShowing;
    }

    public void updateLineWidth(int lineWidth) {
        this.lineWidth = lineWidth;
    }

    public void updateLineLength(int lineLength) {
       this.lineLength = lineLength;
    }
}
