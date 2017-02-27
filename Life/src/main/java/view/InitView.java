package view;

import model.Cell;
import model.Field;
import util.GraphicsUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.logging.Logger;

import static java.awt.image.BufferedImage.TYPE_INT_RGB;
import static util.GraphicsUtil.getHalfOfHorizontalLength;

public class InitView extends JPanel {

    private static final Logger logger = Logger.getLogger(InitView.class.getName());

    private final BufferedImage bufferedImage;
    private final int lineLength;
    private Field currentField = null;
    private int[] fillColor = {0, 255, 0};

    public InitView(int width, int height, final int lineLength) {
        setPreferredSize(new Dimension(width, height));
        setLayout(null);

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
                Cell[][] cells = currentField.getCells();

                for (int k = 0; k < cells.length; ++k) {
                    if (0 == k % 2) {
                        for (int i = 0; i < cells[0].length; ++i) {
                            GraphicsUtil.drawHexagon(bufferedImage, i, k, lineLength, currentField.getHeight());
                        }
                    } else {
                        for (int i = 0; i < cells[0].length - 1; ++i) {
                            GraphicsUtil.drawHexagon(bufferedImage, i, k, lineLength, currentField.getHeight());
                        }
                    }
                }

                for (int k = 0; k < cells.length; ++k) {
                    if (0 == k % 2) {
                        for (int i = 0; i < cells[0].length; ++i) {
                            if (cells[i][k].isAlive()) {
                                GraphicsUtil.fillHexagon(bufferedImage, i, k, lineLength, fillColor);
                            }

                            printImpact(i, k, cells[i][k].getImpact(), bufferedImage);
                        }
                    } else {
                        for (int i = 0; i < cells[0].length - 1; ++i) {
                            if (cells[i][k].isAlive()) {
                                GraphicsUtil.fillHexagon(bufferedImage, i, k, lineLength, fillColor);
                            }

                            printImpact(i, k, cells[i][k].getImpact(), bufferedImage);
                        }
                    }
                }
            }

            g.drawImage(bufferedImage, 0, 0, null);
        }
    }

    private void printImpact(int x, int y, double impact, BufferedImage bufferedImage) {
        GraphicsUtil.Point point = GraphicsUtil.fromCellPositionToCoordinatesUpd(x, y, lineLength);
        int centerX = point.getX() + lineLength / 2;
        int centerY = point.getY() + getHalfOfHorizontalLength(lineLength);

        String impactStr = Double.toString(impact);

        int dotPos = impactStr.indexOf('.');
        String wholePart = impactStr.substring(0, dotPos);
        String decimalPart = impactStr.substring(dotPos + 1, dotPos + 2);

        Graphics2D graphics2D = (Graphics2D) bufferedImage.getGraphics();

        int fontSize = lineLength / 2;
        graphics2D.setFont(new Font("Courier New", Font.PLAIN, fontSize));
        graphics2D.setColor(new Color(0, 0, 0));
        graphics2D.drawString(wholePart + "." + decimalPart, centerX - lineLength / 9, centerY - lineLength / 5);
    }

    public void drawField(Field field) {
        currentField = field;
        repaint();
    }
}
