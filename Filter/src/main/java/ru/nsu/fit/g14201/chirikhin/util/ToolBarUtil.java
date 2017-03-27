package ru.nsu.fit.g14201.chirikhin.util;

import javax.swing.*;

public class ToolBarUtil {
    private ToolBarUtil() {

    }

    public static JButton addNewButton(JToolBar jToolBar, ImageIcon imageIcon, String tooltip) {
        JButton jButton = new JButton(imageIcon);
        jButton.setToolTipText(tooltip);
        jToolBar.add(jButton);

        return jButton;
    }

    public  static JToggleButton addNewToggleButton(JToolBar jToolBar, ImageIcon imageIcon, String tooltip) {
        JToggleButton jButton = new JToggleButton(imageIcon);
        jButton.setToolTipText(tooltip);
        jToolBar.add(jButton);

        return jButton;
    }

}
