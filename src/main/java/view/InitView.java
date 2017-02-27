package view;

import model.Cell;
import model.Field;
import util.GraphicsUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.logging.Logger;

import static java.awt.image.BufferedImage.TYPE_INT_RGB;

public class InitView extends JPanel {

    private static final Logger logger = Logger.getLogger(InitView.class.getName());

    private final BufferedImage bufferedImage;
    private final int lineLength;
    private Field currentField = null;
    private int[] fillColor = {0, 255, 0};

    public InitView(int width, int height, final int lineLength) {
        setPreferredSize(new Dimension(width, height));
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
                        }
                    } else {
                        for (int i = 0; i < cells[0].length - 1; ++i) {
                            if (cells[i][k].isAlive()) {
                                GraphicsUtil.fillHexagon(bufferedImage, i, k, lineLength, fillColor);
                            }
                        }
                    }
                }
            }

            g.drawImage(bufferedImage, 0, 0, null);
        }
    }

    public void drawField(Field field) {
        currentField = field;
        repaint();
    }
}
