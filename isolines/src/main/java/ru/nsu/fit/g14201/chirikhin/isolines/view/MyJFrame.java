package ru.nsu.fit.g14201.chirikhin.isolines.view;

import com.chirikhin.swing.util.MenuToolBarListenerUtil;
import com.chirikhin.swing.util.MenuUtil;
import com.chirikhin.universal_parser.ParserException;
import com.chirikhin.universal_parser.TypeConversionException;
import com.chirikhin.universal_parser.TypeMatchingException;
import ru.nsu.fit.g14201.chirikhin.isolines.config_parser.MyParser;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;

public class MyJFrame extends JFrame {
    private static final int DEFAULT_WIDTH = 500;
    private static final int DEFAULT_HEIGHT = 500;
    private static final String OPEN_ICON_PNG = "open_icon.png";
    private static final String FILE_MENU = "File";
    private static final String OPEN_MENU_TITLE = "Open config";
    private static final String DATA_FOLDER = "./FIT_g14201_Chirikhin_Isolines_Data";
    private static final String ERROR_WHILE_LOADING_CONFIG = "Error while loading config";
    private static final String VIEW_MENU = "View";
    private static final String GRID_VISIBILITY = "Grid visibility";
    private static final String GRID_ICON_PNG = "grid_icon.png";
    private static final String COLOR_MAP_VISIBILITY = "Color map visibility";
    private static final String COLOR_MAP_PNG = "color_map.png";
    private static final String INTERACTIVE_ICON_PNG = "interactive_icon.png";
    private static final String INTERACTIVE_MODE = "Interactive mode";
    private static final String HELP = "Help";
    private static final String ABOUT = "About";
    private static final String ABOUT_ICON_PNG = "about_icon.png";
    private static final String ISOLINES_INFO_MESSAGE = "Isolines \n Created by Chirikhin Alexander 5.4.2017";
    private static final String ISOLINES_INFO_TITLE = "About program";
    private static final String INTERPOLATION_MODE_ICON_PNG = "interpolation_mode_icon.png";
    private static final String INTERPOLATION_MODE = "Interpolation mode";
    private static final String APP_NAME = "Isolines";
    private final MyJPanel myJPanel;

    public MyJFrame() {
        super(APP_NAME);
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

        JMenu fileMenu = MenuUtil.addNewMenuToBar(jMenuBar, FILE_MENU);
        JMenu viewMenu = MenuUtil.addNewMenuToBar(jMenuBar, VIEW_MENU);

        MenuToolBarListenerUtil.addNewOption(fileMenu, jToolBar, OPEN_ICON_PNG, OPEN_MENU_TITLE, this::onOpenButtonClicked);

        MenuToolBarListenerUtil.addNewSelectableOption(viewMenu, jToolBar, GRID_ICON_PNG, GRID_VISIBILITY, myJPanel::setGridShownMode);
        MenuToolBarListenerUtil.addNewSelectableOption(viewMenu, jToolBar, COLOR_MAP_PNG, COLOR_MAP_VISIBILITY, myJPanel::setColorMapVisibility);
        MenuToolBarListenerUtil.addNewSelectableOption(viewMenu, jToolBar, INTERACTIVE_ICON_PNG, INTERACTIVE_MODE, myJPanel::setInteractiveModeEnabled);
        MenuToolBarListenerUtil.addNewSelectableOption(viewMenu, jToolBar, INTERPOLATION_MODE_ICON_PNG, INTERPOLATION_MODE, myJPanel::setColorInterpolationModeEnabled);

        JMenu aboutMenu = MenuUtil.addNewMenuToBar(jMenuBar, HELP);

        MenuToolBarListenerUtil.addNewOption(aboutMenu, jToolBar, ABOUT_ICON_PNG, ABOUT,
                () -> JOptionPane.showMessageDialog(this, ISOLINES_INFO_MESSAGE, ISOLINES_INFO_TITLE, JOptionPane.INFORMATION_MESSAGE));

        pack();
        setVisible(true);
    }

    private void onOpenButtonClicked() {
        JFileChooser jFileChooser = new JFileChooser();

        FileNameExtensionFilter imageFilter = new FileNameExtensionFilter("*.txt", "txt");
        jFileChooser.addChoosableFileFilter(imageFilter);
        jFileChooser.setAcceptAllFileFilterUsed(false);
        jFileChooser.setCurrentDirectory(new File(DATA_FOLDER));

        int result = jFileChooser.showOpenDialog(this);

        if (JFileChooser.APPROVE_OPTION == result) {
            try {
                MyParser myParser = new MyParser(jFileChooser.getSelectedFile());
                myJPanel.applyNewConfiguration(myParser.getM(), myParser.getK(), myParser.getLegendColor(), myParser.getIsolinesColor().getFirst());
            } catch (TypeConversionException | TypeMatchingException | ParserException e) {
                JOptionPane.showMessageDialog(this, e.getMessage(), ERROR_WHILE_LOADING_CONFIG, JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
