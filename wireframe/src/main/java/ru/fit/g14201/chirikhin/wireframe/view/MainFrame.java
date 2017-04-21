package ru.fit.g14201.chirikhin.wireframe.view;

import chirikhin.swing.util.MenuToolBarListenerUtil;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    private static final String FILE = "File";

    public MainFrame() {
        JMenuBar jMenubar = new JMenuBar();
        JMenu fileMenu = new JMenu(FILE);
        jMenubar.add(fileMenu);
        setJMenuBar(jMenubar);
        //MenuToolBarListenerUtil.addNewOption(fileMenu);
    }
}
