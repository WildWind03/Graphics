package ru.nsu.fit.g14201.chirikhin.isolines.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class MyJFrame extends JFrame {
    private static final int DEFAULT_WIDTH = 50;
    private static final int DEFAULT_HEIGHT = 50;
    private final MyJPanel myJPanel;

    public MyJFrame() {
        this.myJPanel = new MyJPanel(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                myJPanel.update(getContentPane().getWidth(), getContentPane().getHeight());
            }
        });

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        JMenuBar jMenuBar = new JMenuBar();
        setJMenuBar(jMenuBar);

        JToolBar jToolBar = new JToolBar();
        add(jToolBar, BorderLayout.NORTH);
        add(myJPanel, BorderLayout.CENTER);

        pack();
        setVisible(true);
    }
}
