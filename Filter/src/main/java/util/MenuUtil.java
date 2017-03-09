package util;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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

    public static void setListener(JMenuItem jMenuItem, Runnable runnable) {
        for (ActionListener actionListener : jMenuItem.getActionListeners()) {
            jMenuItem.removeActionListener(actionListener);
        }

        jMenuItem.addActionListener(e -> runnable.run());
    }
}
