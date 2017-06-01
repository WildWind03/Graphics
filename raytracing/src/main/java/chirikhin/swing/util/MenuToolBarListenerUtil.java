package chirikhin.swing.util;

import javax.swing.*;

public class MenuToolBarListenerUtil {
    private MenuToolBarListenerUtil() {

    }

    public static void addNewSelectableOption(JMenu jMenu, JToolBar jToolBar, String iconName, String buttonTitle, BooleanRunnable onChanged) {
        JCheckBoxMenuItem jCheckBoxMenuItem = MenuUtil.addNewCheckBoxMenuItem(jMenu, buttonTitle);
        JToggleButton jToggleButton = ToolBarUtil.addNewToggleButton(jToolBar, new ImageIcon(Object.class.getClass().getResource("/" + iconName)), buttonTitle);

        ListenerUtil.setOnChangeListeners(jToggleButton, jCheckBoxMenuItem, isSelected -> {
            jCheckBoxMenuItem.setSelected(isSelected);
            jToggleButton.setSelected(isSelected);
            onChanged.run(isSelected);
        });
    }

    public static void addNewOption(JMenu jMenu, JToolBar jToolBar, String iconName, String buttonTitle, Runnable runnable) {
        JMenuItem jCheckBoxMenuItem = MenuUtil.addNewMenuItem(jMenu, buttonTitle);
        JButton jToggleButton = ToolBarUtil.addNewButton(jToolBar, new ImageIcon(Object.class.getClass().getResource("/" + iconName)), buttonTitle);

        ListenerUtil.setOnActionListeners(jToggleButton, jCheckBoxMenuItem, runnable);
    }
}
