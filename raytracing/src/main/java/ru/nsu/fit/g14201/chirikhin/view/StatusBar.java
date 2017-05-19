package ru.nsu.fit.g14201.chirikhin.view;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class StatusBar extends JPanel {
    private JLabel jLabel = new JLabel();
    private JPanel colorPanel = new JPanel();

    public StatusBar() {
        super(true);
        Border marginBorder = BorderFactory.createEmptyBorder(1, 10, 8, 10);
        setBorder(marginBorder);
        setLayout(new BorderLayout());

        add(jLabel, BorderLayout.CENTER);
        add(colorPanel, BorderLayout.EAST);
    }

    public void setText(String string, Color color) {
        jLabel.setText(string);

        if (null == color) {
            colorPanel.setVisible(false);
        } else {
            colorPanel.setVisible(true);
            colorPanel.setBackground(color);
        }
    }
}
