package view;

import util.MenuUtil;

import javax.swing.*;
import java.awt.*;

public class MyJFrame extends JFrame {
    private static final String APPLICATION_NAME = "Filter";

    private static final String MENU_FILE = "File";
    private static final String MENU_FILTER = "Filter";
    private static final String MENU_ABOUT = "About";

    private static final String ITEM_NEW = "New";
    private static final String ITEM_OPEN = "Open";
    private static final String ITEM_SAVE = "Save";

    private static final String ITEM_SELECT = "Select";
    private static final String ITEM_COPY_FROM_B_TO_C = "Copy from B to C";
    private static final String ITEM_COPY_FROM_C_TO_B  = "Copy from C to B";
    private static final String ITEM_ZOOM = "Zoom";
    private static final String ITEM_WATERCOLOR = "Watercolor";

    private final JMenuItem openItem;
    private final JMenuItem newItem;
    private final JMenuItem saveItem;

    private final JMenuItem selectItem;
    private final JMenuItem copyBToC;
    private final JMenuItem copyCToB;
    private final JMenuItem zoom;
    private final JMenuItem waterColor;


    public MyJFrame() throws HeadlessException {
        super(APPLICATION_NAME);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        JMenuBar jMenuBar = new JMenuBar();
        add(jMenuBar);

        JMenu fileMenu = MenuUtil.addNewMenuToBar(jMenuBar, MENU_FILE);
        JMenu filterMenu = MenuUtil.addNewMenuToBar(jMenuBar, MENU_FILTER);
        JMenu aboutMenu = MenuUtil.addNewMenuToBar(jMenuBar, MENU_ABOUT);

        newItem = MenuUtil.addNewMenuItem(fileMenu, ITEM_NEW);
        openItem = MenuUtil.addNewMenuItem(fileMenu, ITEM_OPEN);
        saveItem = MenuUtil.addNewMenuItem(fileMenu, ITEM_SAVE);

        selectItem = MenuUtil.addNewMenuItem(filterMenu, ITEM_SELECT);
        copyBToC = MenuUtil.addNewMenuItem(filterMenu, ITEM_COPY_FROM_B_TO_C);
        copyCToB = MenuUtil.addNewMenuItem(filterMenu, ITEM_COPY_FROM_C_TO_B);
        zoom = MenuUtil.addNewMenuItem(filterMenu, ITEM_ZOOM);
        waterColor = MenuUtil.addNewMenuItem(filterMenu, ITEM_WATERCOLOR);

        pack();
        setVisible(true);
    }

    public void setOpenFileListener(Runnable runnable) {
        MenuUtil.setListener(openItem, runnable);
    }

    public void setNewFileListener(Runnable runnable) {
        MenuUtil.setListener(newItem, runnable);
    }

    public void setSaveFileListener(Runnable runnable) {
        MenuUtil.setListener(saveItem, runnable);
    }


}
