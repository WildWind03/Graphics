package ru.fit.g14201.chirikhin.wireframe.view;

import chirikhin.swing.util.MenuToolBarListenerUtil;
import chirikhin.universal_parser.NoObjectFactoryException;
import chirikhin.universal_parser.ParserException;
import chirikhin.universal_parser.TypeConversionException;
import chirikhin.universal_parser.TypeMatchingException;
import ru.fit.g14201.chirikhin.wireframe.model_loader.ModelLoader;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;

public class MainFrame extends JFrame {

    private static final String FILE = "File";
    private static final String WIREFRAME = "Wireframe";
    private static final String SETTINGS = "Settings";
    private static final String ABOUT = "About";
    private static final String OPEN_ICON_PNG = "open_icon.png";
    private static final String OPEN = "Open";
    private static final String SAVE_ICON_PNG = "save_icon.png";
    private static final String SAVE = "Save";
    private static final String INIT_ICON_PNG = "init_icon.png";
    private static final String INIT = "Init";
    private static final String SETTINGS_ICON_PNG = "settings_icon.png";
    private static final String ABOUT_ICON_PNG = "about_icon.png";
    private static final String ABOUT_TEXT = "Wireframe\nCreated 21.04.2017 by Chirikhin Alexander";
    private static final String DATA_FOLDER = "./FIT_g14201_Chirikhin_Wireframe_Data";
    private static final String CAN_T_OPEN = "Can't open";

    public MainFrame() {
        super(WIREFRAME);
        JMenuBar jMenubar = new JMenuBar();

        JMenu fileMenu = new JMenu(FILE);
        jMenubar.add(fileMenu);
        JMenu settingsMenu = new JMenu(SETTINGS);
        jMenubar.add(settingsMenu);
        JMenu aboutMenu = new JMenu(ABOUT);
        jMenubar.add(aboutMenu);

        setJMenuBar(jMenubar);

        JToolBar jToolBar = new JToolBar();
        add(jToolBar);

        MenuToolBarListenerUtil.addNewOption(fileMenu, jToolBar, OPEN_ICON_PNG,
                OPEN, this::onOpenButtonClick);
        MenuToolBarListenerUtil.addNewOption(fileMenu, jToolBar,
                SAVE_ICON_PNG, SAVE, this::onSaveButtonClick);
        MenuToolBarListenerUtil.addNewOption(settingsMenu, jToolBar,
                INIT_ICON_PNG, INIT, this::onInitButtonClick);
        MenuToolBarListenerUtil.addNewOption(settingsMenu, jToolBar,
                SETTINGS_ICON_PNG, SETTINGS, this::onSettingButtonClick);
        MenuToolBarListenerUtil.addNewOption(aboutMenu, jToolBar, ABOUT_ICON_PNG, ABOUT,
                () -> JOptionPane.showMessageDialog(this, ABOUT_TEXT));

        pack();
        setVisible(true);
    }

    private void onOpenButtonClick() {
        JFileChooser jFileChooser = new JFileChooser();

        FileNameExtensionFilter modelFilter = new FileNameExtensionFilter("*.txt", "txt");
        jFileChooser.addChoosableFileFilter(modelFilter);
        jFileChooser.setCurrentDirectory(new File(DATA_FOLDER));

        int result = jFileChooser.showOpenDialog(this);

        if (JFileChooser.APPROVE_OPTION == result) {
            try {
                ModelLoader modelLoader = new ModelLoader(jFileChooser.getSelectedFile());
            } catch (ParserException | TypeConversionException |
                    TypeMatchingException | NoObjectFactoryException e) {
                JOptionPane.showMessageDialog(this, e.getMessage(),
                        CAN_T_OPEN, JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void onSaveButtonClick() {

    }

    private void onInitButtonClick() {

    }

    private void onSettingButtonClick() {

    }

}
