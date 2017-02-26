package view;

import graphics.SpanFiller;
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
    private final int width;
    private final int height;
    private Field currentField = null;

    public InitView(int width, int height) {
        bufferedImage = new BufferedImage(width, height, TYPE_INT_RGB);
        this.width = width;
        this.height = height;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D drawer = (Graphics2D) bufferedImage.getGraphics();
        drawer.setBackground(Color.WHITE);
        drawer.clearRect(0, 0, width, height);
        drawer.setColor(Color.BLACK);

        if (null != currentField) {
            Cell[][] cells = currentField.getCells();

            for (int k = 0; k < cells.length; ++k) {
                if (0 == k%2) {
                    for (int i = 0; i < cells[0].length; ++i) {
                        GraphicsUtil.drawHexagon(bufferedImage, i, k, 50);
                    }
                } else {
                    for (int i = 0; i < cells[0].length - 1; ++i) {
                        GraphicsUtil.drawHexagon(bufferedImage, i, k, 50);
                    }
                }
            }
        }

        SpanFiller spanFiller = new SpanFiller(bufferedImage, 0, 0, new int[] {255, 0, 0});

        spanFiller.applyFiller();

        g.drawImage(bufferedImage, 0, 0, null);

    }

    public void drawField(Field field) {
        currentField = field;
        paintComponent(getGraphics());
    }
}
