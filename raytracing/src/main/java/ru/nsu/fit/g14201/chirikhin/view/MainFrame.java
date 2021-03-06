package ru.nsu.fit.g14201.chirikhin.view;

import chirikhin.swing.util.MenuToolBarListenerUtil;
import ru.nsu.fit.g14201.chirikhin.model.RenderSettings;
import ru.nsu.fit.g14201.chirikhin.model.SceneConfig;
import ru.nsu.fit.g14201.chirikhin.parser.RenderConfigParser;
import ru.nsu.fit.g14201.chirikhin.parser.SceneConfigParser;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class MainFrame extends JFrame {
    private static final String FILE = "File";
    private static final String SETTINGS = "Settings";
    private static final String ABOUT = "About";

    private static final int DEFAULT_SHAPE_VIEW_WIDTH = 621;
    private static final int DEFAULT_SHAPE_VIEW_HEIGHT = 621;

    private static final int MARGIN_BORDER = 10;

    private static final String OPEN_TITLE = "Open scene";
    private static final String LOAD_RENDER_SETTINGS_TITLE = "Load render settings";
    private static final String SAVE_RENDER_SETTINGS_TITLE = "Save render settings";
    private static final String INIT_TITLE = "Init";
    private static final String RENDER_SETTINGS_TITLE = "Render settings";
    private static final String SELECT_VIEW_TITLE = "Select view mode";
    private static final String RENDER_TITLE = "Render";
    private static final String SAVE_IMAGE_TITLE = "Save image";
    private static final String ABOUT_TITLE = "About the app";

    private static final String OPEN_ICON_PNG = "open_icon.png";
    private static final String LOAD_RENDER_SETTINGS_PNG = "load_icon.png";
    private static final String SAVE_RENDER_SETTINGS_PNG = "save_icon.png";
    private static final String INIT_PNG = "init_icon.png";
    private static final String RENDER_SETTINGS_PNG = "settings_icon.png";
    private static final String SELECT_VIEW_PNG = "select_icon.png";
    private static final String RENDER_PNG = "render_icon.png";
    private static final String SAVE_IMAGE_PNG = "save_image.png";
    private static final String ABOUT_PNG = "about_icon.png";

    private static final String APP_NAME = "Raytracing";

    private static final String DATA_FOLDER = "./FIT_g14201_Chirikhin_Raytracing_Data";
    private static final String CAN_T_OPEN = "Can not open";


    private StatusBar statusBar = new StatusBar();
    private ShapeView shapeView = new ShapeView(DEFAULT_SHAPE_VIEW_WIDTH, DEFAULT_SHAPE_VIEW_HEIGHT);

    public MainFrame() {
        super(APP_NAME);

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        Border marginBorder = BorderFactory.createEmptyBorder(MARGIN_BORDER, MARGIN_BORDER, MARGIN_BORDER, MARGIN_BORDER);
        Border lineBorder = BorderFactory.createLineBorder(Color.black);
        Border finalBorder = BorderFactory.createCompoundBorder(marginBorder, lineBorder);

        JPanel mainFrameInnerPanel = new JPanel();
        mainFrameInnerPanel.setBorder(finalBorder);
        mainFrameInnerPanel.setLayout(new BorderLayout());

        JMenuBar jMenubar = new JMenuBar();

        JMenu fileMenu = new JMenu(FILE);
        jMenubar.add(fileMenu);
        JMenu settingsMenu = new JMenu(SETTINGS);
        jMenubar.add(settingsMenu);
        JMenu aboutMenu = new JMenu(ABOUT);
        jMenubar.add(aboutMenu);

        setJMenuBar(jMenubar);

        JToolBar jToolBar = new JToolBar();

        add(jToolBar, BorderLayout.NORTH);
        add(mainFrameInnerPanel, BorderLayout.CENTER);
        add(statusBar, BorderLayout.SOUTH);
        shapeView.setOnRenderUpdateListener(value -> statusBar.updateValue(value));
        shapeView.setRenderEventHandler(renderEvent -> {
            switch (renderEvent) {
                case START:
                    for (Component component : jToolBar.getComponents()) {
                        component.setEnabled(false);
                    }

                    for (Component component : jMenubar.getComponents()) {
                        component.setEnabled(false);
                    }
                    statusBar.startRender();
                    break;
                case END:
                    for (Component component : jToolBar.getComponents()) {
                        component.setEnabled(true);
                    }

                    for (Component component : jMenubar.getComponents()) {
                        component.setEnabled(true);
                    }
                    statusBar.endRender();
                    break;
            }
        });

        mainFrameInnerPanel.add(shapeView, BorderLayout.CENTER);

        MenuToolBarListenerUtil.addNewOption(fileMenu, jToolBar, OPEN_ICON_PNG, OPEN_TITLE, this::onOpenSceneButtonClick);
        MenuToolBarListenerUtil.addNewOption(fileMenu, jToolBar, LOAD_RENDER_SETTINGS_PNG, LOAD_RENDER_SETTINGS_TITLE, this::onLoadRenderSettingsClick);
        MenuToolBarListenerUtil.addNewOption(fileMenu, jToolBar, SAVE_RENDER_SETTINGS_PNG, SAVE_RENDER_SETTINGS_TITLE, this::onSaveRenderSettingsClick);
        MenuToolBarListenerUtil.addNewOption(settingsMenu, jToolBar, INIT_PNG, INIT_TITLE, this::onInitClick);
        MenuToolBarListenerUtil.addNewOption(settingsMenu, jToolBar, RENDER_SETTINGS_PNG, RENDER_SETTINGS_TITLE, this::onRenderSettingsClick);
        MenuToolBarListenerUtil.addNewOption(settingsMenu, jToolBar, SELECT_VIEW_PNG, SELECT_VIEW_TITLE, this::onSelectViewModeClick);
        MenuToolBarListenerUtil.addNewOption(settingsMenu, jToolBar, RENDER_PNG, RENDER_TITLE, this::onRenderModeClick);
        MenuToolBarListenerUtil.addNewOption(fileMenu, jToolBar, SAVE_IMAGE_PNG, SAVE_IMAGE_TITLE, this::onSaveImageClick);
        MenuToolBarListenerUtil.addNewOption(aboutMenu, jToolBar, ABOUT_PNG, ABOUT_TITLE, this::onAboutClick);

        pack();
        setVisible(true);
    }

    private void onSelectViewModeClick() {
        shapeView.wireMode();
    }

    private void onRenderModeClick() {
        shapeView.render();
    }

    private void onAboutClick() {
        JOptionPane.showMessageDialog(this, "Made by Chirikhin Alexander \n Raytracing");
    }

    private void onSaveImageClick() {
        BufferedImage renderedMessage = shapeView.getRenderedImage();

        if (null == renderedMessage) {
            JOptionPane.showMessageDialog(this, "There is not a rendered image");
            return;
        }

        JFileChooser jFileChooser = new JFileChooser();
        FileNameExtensionFilter modelFilter = new FileNameExtensionFilter("*.png", "png");
        jFileChooser.addChoosableFileFilter(modelFilter);
        jFileChooser.setCurrentDirectory(new File(DATA_FOLDER));
        int result = jFileChooser.showSaveDialog(this);

        if (JFileChooser.APPROVE_OPTION ==  result) {
            try {
                ImageIO.write(renderedMessage, "PNG", jFileChooser.getSelectedFile());
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, e.getMessage());
            }
        }
    }

    private void onRenderSettingsClick() {

    }

    private void onInitClick() {

    }

    private void onSaveRenderSettingsClick() {

    }

    private void onLoadRenderSettingsClick() {
        JFileChooser jFileChooser = new JFileChooser();

        FileNameExtensionFilter modelFilter = new FileNameExtensionFilter("*.render", "render");
        jFileChooser.addChoosableFileFilter(modelFilter);
        jFileChooser.setCurrentDirectory(new File(DATA_FOLDER));

        int result = jFileChooser.showOpenDialog(this);

        if (JFileChooser.APPROVE_OPTION == result) {
            try {
                RenderSettings renderSettings = RenderConfigParser.getRenderSettings(jFileChooser.getSelectedFile());
                shapeView.setRenderSettings(renderSettings);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, e.getMessage(),
                        CAN_T_OPEN, JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void onOpenSceneButtonClick() {
        JFileChooser jFileChooser = new JFileChooser();

        FileNameExtensionFilter modelFilter = new FileNameExtensionFilter("*.scene", "scene");
        jFileChooser.addChoosableFileFilter(modelFilter);
        jFileChooser.setCurrentDirectory(new File(DATA_FOLDER));

        int result = jFileChooser.showOpenDialog(this);

        if (JFileChooser.APPROVE_OPTION == result) {
            try {
                SceneConfig sceneConfig = SceneConfigParser.getSceneConfig(jFileChooser.getSelectedFile());
                shapeView.setSceneConfig(sceneConfig);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, e.getMessage(),
                        CAN_T_OPEN, JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
