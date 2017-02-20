package view;

import com.sun.javafx.iio.ImageStorage;

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

        drawer.drawLine(0,0,100,100);

        g.drawImage(bufferedImage, 0, 0, null);
        //repaint();
    }
}
