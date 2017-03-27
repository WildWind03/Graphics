package ru.nsu.fit.g14201.chirikhin.util;

import javax.swing.*;

public class MenuUtil {
    private MenuUtil() {

    }

    public static JMenu addNewMenuToBar(JMenuBar jMenuBar, String string) {
        JMenu jMenu = new JMenu(string);
        jMenuBar.add(jMenu);

        return jMenu;
    }

    public static JMenuItem addNewMenuItem (JMenu jMenu, String name) {
        JMenuItem jMenuItem = new JMenuItem(name);
        jMenu.add(jMenuItem);

        return jMenuItem;
    }

    public static JCheckBoxMenuItem addNewCheckBoxMenuItem(JMenu jMenu, String name) {
        JCheckBoxMenuItem jCheckBoxMenuItem = new JCheckBoxMenuItem(name);
        jMenu.add(jCheckBoxMenuItem);

        return jCheckBoxMenuItem;
    }

    public static JMenu addMenuToMenu(JMenu jMenu, String name) {
        JMenu newMenu = new JMenu(name);
        jMenu.add(newMenu);

        return newMenu;
    }
}
