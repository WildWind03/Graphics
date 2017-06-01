package ru.nsu.fit.g14201.chirikhin.view;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class StatusBar extends JPanel {
    private JProgressBar jProgressBar = new JProgressBar();

    public StatusBar() {
        super(true);
        Border marginBorder = BorderFactory.createEmptyBorder(1, 10, 8, 10);
        setBorder(marginBorder);
        setLayout(new BorderLayout());

        add(jProgressBar, BorderLayout.CENTER);
        jProgressBar.setMinimum(0);
        jProgressBar.setMaximum(100);
        jProgressBar.setVisible(false);
    }

    public void updateValue(int pos) {
        jProgressBar.setValue(pos);
    }

    public void startRender() {
        jProgressBar.setVisible(true);
    }

    public void endRender() {
        jProgressBar.setVisible(false);
    }
}
