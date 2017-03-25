package ru.nsu.fit.g14201.chirikhin.filter.view;

import ru.nsu.fit.g14201.chirikhin.filter.util.ListenerUtil;
import ru.nsu.fit.g14201.chirikhin.filter.util.MenuUtil;
import ru.nsu.fit.g14201.chirikhin.filter.util.ToolBarUtil;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

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
    private static final String ABOUT_THE_APPLICATION = "About the application";
    private static final String BLACK_AND_WHITE = "Black and white";
    private static final String NEGATIVE = "Negative";
    private static final String FLOYD_DITHERING = "Floyd dithering";
    private static final String ORDERED_DITHERING = "Ordered dithering";
    private static final String ROBERTS = "Robert's operator";
    private static final String SOBEL = "Sobel's operator";
    private static final String SMOOTHING = "Smoothing";
    private static final String SHARPING = "Sharping";
    private static final String EMBOSS = "Emboss";
    private static final String ROTATION = "Rotation";
    private static final String GAMMA_FILTER = "Gamma filter";
    private static final String VOLUME_VISUALIZATION = "Volume Visualization";
    private static final String ABSORPTION = "Absorption";
    private static final String EMISSION = "Emission";
    private static final String RUN = "Run";
    private static final String OPEN_CONFIG = "Open config";

    private static final String CREATE_ICON_PNG = "/create_icon.png";
    private static final String OPEN_ICON_PNG = "/open_icon.png";
    private static final String SAVE_ICON_PNG = "/save_icon.png";
    private static final String SELECT_ICON_PNG = "/select_icon.png";
    private static final String B_TO_C_ICON_PNG = "/b_to_c_icon.png";
    private static final String C_TO_B_ICON_PNG = "/c_to_b_icon.png";
    private static final String BLACK_AND_WHITE_ICON_PNG = "/black_and_white_icon.png";
    private static final String NEGATIVE_ICON_PNG = "/negative_icon.png";
    private static final String FLOYD_ICON_PNG = "/floyd_icon.png";
    private static final String ORDERED_ICON_PNG = "/ordered_icon.png";
    private static final String MAGNIFIER_ICON_PNG = "/magnifier_icon.png";
    private static final String ROBERT_ICON_PNG = "/robert_icon.png";
    private static final String SOBEL_ICON_PNG = "/sobel_icon.png";
    private static final String BLUR_ICON_PNG = "/blur_icon.png";
    private static final String SHARP_ICON_PNG = "/sharp_icon.png";
    private static final String BLACK_AND_WHITE_ICON_PNG1 = "/black_and_white_icon.png";
    private static final String WATERCOLOR_PNG = "/watercolor.png";
    private static final String ROTATE_PNG = "/rotate.png";
    private static final String GAMMA_PNG = "/gamma.png";
    private static final String ABSORPTION_PNG = "/absorption.png";
    private static final String EMISSION_PNG = "/emission.png";
    private static final String RUN_PNG = "/run.png";
    private static final String CONFIG_PNG = "/config.png";
    private static final String INFO_ICON_PNG = "/info_icon.png";
    private static final String CAN_NOT_LOAD_THE_IMAGE = "Can not load the image";
    private static final String DATA_FOLDER = "./FIT_g14201_Chirikhin_Filter_Data";
    private static final String ERROR_WHILE_SAVING = "Error while saving";
    private static final String THERE_ISN_T_AN_IMAGE_THAT_COULD_BE_SAVED = "There isn't an image that could be saved";
    private static final String ITEM_EXIT = "Exit";
    private static final String FILTER_MADE_BY_CHIRIKHIN_ALEXANDER_3_19_2017 = "Filter \n Made by Chirikhin Alexander, 3.19.2017";
    private static final String ABOUT_THE_PROGRAM = "About the program";
    private static final String ROBERT_S_FILTER_CONFIGURATION = "Robert's filter configuration";
    private static final String SOBEL_CONFIGURATION_DIALOG = "Sobel configuration dialog";
    private static final String ROTATION_CONFIGURATION = "Rotation configuration";
    private static final String GAMMA_CONFIGURATION = "Gamma configuration";

    private final JMenuItem openItem;
    private final JMenuItem newItem;
    private final JMenuItem saveItem;
    private final JMenuItem exitItem;

    private final JCheckBoxMenuItem selectItem;
    private final JMenuItem copyBToC;
    private final JMenuItem copyCToB;

    private final JMenuItem blackAndWhiteFilter;
    private final JMenuItem negativeFilter;
    private final JMenuItem ditheringFloydFilter;
    private final JMenuItem orderedDitheringFilter;
    private final JMenuItem zoom2XFilter;
    private final JMenuItem robertsFilter;
    private final JMenuItem sobelFilter;
    private final JMenuItem smoothingFilter;
    private final JMenuItem sharpingFilter;
    private final JMenuItem embossFilter;
    private final JMenuItem waterColor;
    private final JMenuItem rotationFilter;
    private final JMenuItem gammaFilter;

    private final JCheckBoxMenuItem absorptionCheckBoxMenuItem;
    private final JCheckBoxMenuItem emissionCheckBoxMenuItem;
    private final JCheckBoxMenuItem runMenuItem;
    private final JMenuItem openConfigMenuItem;

    private final JMenuItem aboutAuthor;

    private final JButton openButton;
    private final JButton newButton;
    private final JButton saveButton;

    private final JToggleButton selectButton;
    private final JButton copyBToCButton;
    private final JButton copyCToBButton;

    private final JButton blackAndWhiteFilterButton;
    private final JButton negativeFilterButton;
    private final JButton floydButton;
    private final JButton orderedButton;
    private final JButton zoom2XButton;
    private final JButton robertsButton;
    private final JButton sobelButton;
    private final JButton smoothButton;
    private final JButton sharpButton;
    private final JButton embossButton;
    private final JButton waterColorButton;
    private final JButton rotationButton;
    private final JButton gammaButton;
    private final JToggleButton absorptionButton;
    private final JToggleButton emissionButton;
    private final JToggleButton runButton;
    private final JButton openConfigButton;

    private final JButton aboutAuthorButton;

    private final MyJPanel myJPanel;
    private final JScrollPane scrollPane;

    public MyJFrame()  {
        super(APPLICATION_NAME);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        JMenuBar jMenuBar = new JMenuBar();
        setJMenuBar(jMenuBar);

        JToolBar jToolBar = new JToolBar();
        add(jToolBar, BorderLayout.NORTH);

        JMenu fileMenu = MenuUtil.addNewMenuToBar(jMenuBar, MENU_FILE);
        JMenu filterMenu = MenuUtil.addNewMenuToBar(jMenuBar, MENU_FILTER);
        JMenu aboutMenu = MenuUtil.addNewMenuToBar(jMenuBar, MENU_ABOUT);

        newItem = MenuUtil.addNewMenuItem(fileMenu, ITEM_NEW);
        openItem = MenuUtil.addNewMenuItem(fileMenu, ITEM_OPEN);
        saveItem = MenuUtil.addNewMenuItem(fileMenu, ITEM_SAVE);
        exitItem = MenuUtil.addNewMenuItem(fileMenu, ITEM_EXIT);

        newButton = ToolBarUtil.addNewButton(jToolBar, new ImageIcon(getClass().getResource(CREATE_ICON_PNG)), ITEM_NEW);
        openButton = ToolBarUtil.addNewButton(jToolBar, new ImageIcon(getClass().getResource(OPEN_ICON_PNG)), ITEM_OPEN);
        saveButton = ToolBarUtil.addNewButton(jToolBar, new ImageIcon(getClass().getResource(SAVE_ICON_PNG)), ITEM_SAVE);

        selectItem = MenuUtil.addNewCheckBoxMenuItem(filterMenu, ITEM_SELECT);
        copyBToC = MenuUtil.addNewMenuItem(filterMenu, ITEM_COPY_FROM_B_TO_C);
        copyCToB = MenuUtil.addNewMenuItem(filterMenu, ITEM_COPY_FROM_C_TO_B);
        blackAndWhiteFilter = MenuUtil.addNewMenuItem(filterMenu, BLACK_AND_WHITE);
        negativeFilter = MenuUtil.addNewMenuItem(filterMenu, NEGATIVE);
        ditheringFloydFilter = MenuUtil.addNewMenuItem(filterMenu, FLOYD_DITHERING);
        orderedDitheringFilter = MenuUtil.addNewMenuItem(filterMenu, ORDERED_DITHERING);
        zoom2XFilter = MenuUtil.addNewMenuItem(filterMenu, ITEM_ZOOM);
        robertsFilter = MenuUtil.addNewMenuItem(filterMenu, ROBERTS);
        sobelFilter = MenuUtil.addNewMenuItem(filterMenu, SOBEL);
        smoothingFilter = MenuUtil.addNewMenuItem(filterMenu, SMOOTHING);
        sharpingFilter = MenuUtil.addNewMenuItem(filterMenu, SHARPING);
        embossFilter = MenuUtil.addNewMenuItem(filterMenu, EMBOSS);
        waterColor = MenuUtil.addNewMenuItem(filterMenu, ITEM_WATERCOLOR);
        rotationFilter = MenuUtil.addNewMenuItem(filterMenu, ROTATION);
        gammaFilter = MenuUtil.addNewMenuItem(filterMenu, GAMMA_FILTER);

        selectButton = ToolBarUtil.addNewToggleButton(jToolBar, new ImageIcon(getClass().getResource(SELECT_ICON_PNG)), ITEM_SELECT);
        copyBToCButton = ToolBarUtil.addNewButton(jToolBar, new ImageIcon(getClass().getResource(B_TO_C_ICON_PNG)), ITEM_COPY_FROM_B_TO_C);
        copyCToBButton = ToolBarUtil.addNewButton(jToolBar, new ImageIcon(getClass().getResource(C_TO_B_ICON_PNG)), ITEM_COPY_FROM_C_TO_B);
        blackAndWhiteFilterButton = ToolBarUtil.addNewButton(jToolBar, new ImageIcon(getClass().getResource(BLACK_AND_WHITE_ICON_PNG)), BLACK_AND_WHITE);
        negativeFilterButton = ToolBarUtil.addNewButton(jToolBar, new ImageIcon(getClass().getResource(NEGATIVE_ICON_PNG)), NEGATIVE);
        floydButton = ToolBarUtil.addNewButton(jToolBar, new ImageIcon(getClass().getResource(FLOYD_ICON_PNG)), FLOYD_DITHERING);
        orderedButton = ToolBarUtil.addNewButton(jToolBar, new ImageIcon(getClass().getResource(ORDERED_ICON_PNG)), ORDERED_DITHERING);
        zoom2XButton = ToolBarUtil.addNewButton(jToolBar, new ImageIcon(getClass().getResource(MAGNIFIER_ICON_PNG)), ITEM_ZOOM);
        robertsButton = ToolBarUtil.addNewButton(jToolBar, new ImageIcon(getClass().getResource(ROBERT_ICON_PNG)), ROBERTS);
        sobelButton = ToolBarUtil.addNewButton(jToolBar, new ImageIcon(getClass().getResource(SOBEL_ICON_PNG)), SOBEL);
        smoothButton = ToolBarUtil.addNewButton(jToolBar, new ImageIcon(getClass().getResource(BLUR_ICON_PNG)), SMOOTHING);
        sharpButton = ToolBarUtil.addNewButton(jToolBar, new ImageIcon(getClass().getResource(SHARP_ICON_PNG)), SHARPING);
        embossButton = ToolBarUtil.addNewButton(jToolBar, new ImageIcon(getClass().getResource(BLACK_AND_WHITE_ICON_PNG1)), EMBOSS);
        waterColorButton = ToolBarUtil.addNewButton(jToolBar, new ImageIcon(getClass().getResource(WATERCOLOR_PNG)), ITEM_WATERCOLOR);
        rotationButton = ToolBarUtil.addNewButton(jToolBar, new ImageIcon(getClass().getResource(ROTATE_PNG)), ROTATION);
        gammaButton = ToolBarUtil.addNewButton(jToolBar, new ImageIcon(getClass().getResource(GAMMA_PNG)), GAMMA_FILTER);

        JMenu volumeVisualization = MenuUtil.addMenuToMenu(filterMenu, VOLUME_VISUALIZATION);
        absorptionCheckBoxMenuItem = MenuUtil.addNewCheckBoxMenuItem(volumeVisualization, ABSORPTION);
        emissionCheckBoxMenuItem = MenuUtil.addNewCheckBoxMenuItem(volumeVisualization, EMISSION);
        runMenuItem = MenuUtil.addNewCheckBoxMenuItem(volumeVisualization, RUN);
        openConfigMenuItem = MenuUtil.addNewMenuItem(volumeVisualization, OPEN_CONFIG);

        absorptionButton = ToolBarUtil.addNewToggleButton(jToolBar, new ImageIcon(getClass().getResource(ABSORPTION_PNG)), ABSORPTION);
        emissionButton = ToolBarUtil.addNewToggleButton(jToolBar, new ImageIcon(getClass().getResource(EMISSION_PNG)), EMISSION);
        runButton = ToolBarUtil.addNewToggleButton(jToolBar, new ImageIcon(getClass().getResource(RUN_PNG)), RUN);
        openConfigButton = ToolBarUtil.addNewButton(jToolBar, new ImageIcon(getClass().getResource(CONFIG_PNG)), OPEN_CONFIG);

        aboutAuthor = MenuUtil.addNewMenuItem(aboutMenu, ABOUT_THE_APPLICATION);
        aboutAuthorButton = ToolBarUtil.addNewButton(jToolBar, new ImageIcon(getClass().getResource(INFO_ICON_PNG)), ABOUT_THE_APPLICATION);

        myJPanel = new MyJPanel();
        scrollPane = new JScrollPane(myJPanel);
        scrollPane.getViewport().setScrollMode(JViewport.SIMPLE_SCROLL_MODE);

        add(scrollPane, BorderLayout.CENTER);

        ListenerUtil.setListener(openButton, openItem, this::onOpenFileButtonClicked);
        ListenerUtil.setListener(newButton, newItem, this::onNewButtonClicked);
        ListenerUtil.setListener(saveButton, saveItem, this::onSaveButtonClicked);
        ListenerUtil.setListener(exitItem, this::onExitButtonClicked);

        ListenerUtil.setListener(selectButton, selectItem, this::onSelectButtonClicked);
        ListenerUtil.setListener(copyBToCButton, copyBToC, this::onBToCButtonClicked);
        ListenerUtil.setListener(copyCToBButton, copyCToB, this::onCToBButtonClicked);
        ListenerUtil.setListener(blackAndWhiteFilterButton, blackAndWhiteFilter, this::onGrayscaleFilterButtonClicked);
        ListenerUtil.setListener(negativeFilterButton, negativeFilter, this::onNegativeFilterButtonClicked);
        ListenerUtil.setListener(smoothButton, smoothingFilter, this::onSmoothFilterButtonClicked);
        ListenerUtil.setListener(sharpButton, sharpingFilter, this::onSharpFilterButtonClicked);
        ListenerUtil.setListener(embossButton, embossFilter, this::onEmbossButtonClicked);
        ListenerUtil.setListener(orderedButton, orderedDitheringFilter, this::onOrderedDitheringButtonClicked);
        ListenerUtil.setListener(floydButton, ditheringFloydFilter, this::onFloydDitheringButtonClicked);
        ListenerUtil.setListener(zoom2XButton, zoom2XFilter, this::onZoomButtonClicked);
        ListenerUtil.setListener(robertsButton, robertsFilter, this::onRobertsFilterClicked);
        ListenerUtil.setListener(sobelButton, sobelFilter, this::onSobelFilterClicked);
        ListenerUtil.setListener(waterColorButton, waterColor, this::onWaterColorClicked);
        ListenerUtil.setListener(rotationButton, rotationFilter, this::onRotationFilter);
        ListenerUtil.setListener(gammaButton, gammaFilter, this::onGammaFilter);

        ListenerUtil.setListener(aboutAuthorButton, aboutAuthor, this::onAboutButtonClicked);

        pack();
        setVisible(true);
    }

    private void onGammaFilter() {
        GammaDialog gammaDialog = new GammaDialog(this, GAMMA_CONFIGURATION);
        gammaDialog.apparate();

        if (!gammaDialog.isCancelled()) {
            myJPanel.applyGammaFilter(gammaDialog.getValue());
        }
    }

    private void onRotationFilter() {
        SliderTextFiledDialog rotationFilter = new SliderTextFiledDialog(this, ROTATION_CONFIGURATION, -180, 180, 30);
        rotationFilter.apparate();

        if (!rotationFilter.isCancelled()) {
            myJPanel.applyRotationFilter(rotationFilter.getValue());
        }
    }

    private void onWaterColorClicked() {
        myJPanel.applyWatercolorFilter();
    }

    private void onSobelFilterClicked() {
        SliderTextFiledDialog sobelConfigDialog = new SliderTextFiledDialog(this, SOBEL_CONFIGURATION_DIALOG);
        sobelConfigDialog.apparate();

        if (!sobelConfigDialog.isCancelled()) {
            myJPanel.applySobelFilter(sobelConfigDialog.getValue());
        }
    }

    private void onRobertsFilterClicked() {
        SliderTextFiledDialog sliderTextFiledDialog = new SliderTextFiledDialog(this, ROBERT_S_FILTER_CONFIGURATION);
        sliderTextFiledDialog.apparate();

        if (!sliderTextFiledDialog.isCancelled()) {
            myJPanel.applyRobertsFilter(sliderTextFiledDialog.getValue());
        }
    }


    private void onZoomButtonClicked() {
        myJPanel.applyZoomFilter();
    }

    private void onFloydDitheringButtonClicked() {
        FloydDitheringFilterConfigurationDialog dialog = new FloydDitheringFilterConfigurationDialog(this);
        dialog.apparate();

        if (!dialog.isCancelled()) {
            myJPanel.applyFloydDitheringClicked(dialog.getRedDivision(), dialog.getGreenDivision(), dialog.getBlueDivision());
        }
    }

    private void onOrderedDitheringButtonClicked() {
        myJPanel.applyOrderedDitheringFilter();
    }

    private void onEmbossButtonClicked() {
        myJPanel.applyEmbossFilter();
    }


    private void onSharpFilterButtonClicked() {
        myJPanel.applySharpFilter();
    }

    private void onSmoothFilterButtonClicked() {
        myJPanel.applyBlurFilter();
    }

    private void onNegativeFilterButtonClicked() {
        myJPanel.applyNegativeFilter();
    }

    private void onGrayscaleFilterButtonClicked() {
        myJPanel.applyGrayscaleFilter();
    }

    private void onAboutButtonClicked() {
        JOptionPane.showMessageDialog(this, FILTER_MADE_BY_CHIRIKHIN_ALEXANDER_3_19_2017, ABOUT_THE_PROGRAM, JOptionPane.INFORMATION_MESSAGE);
    }

    private void onExitButtonClicked() {
        System.exit(0);
    }

    private void onSaveButtonClicked() {
        JFileChooser jFileChooser = new JFileChooser();

        FileNameExtensionFilter fileFilter = new FileNameExtensionFilter("*.bmp", "bmp");

        jFileChooser.setFileFilter(fileFilter);
        jFileChooser.addChoosableFileFilter(fileFilter);
        jFileChooser.setCurrentDirectory(new File(DATA_FOLDER));
        jFileChooser.setAcceptAllFileFilterUsed(false);

        int result = jFileChooser.showSaveDialog(this);

        if (JFileChooser.APPROVE_OPTION == result) {
            try {
                BufferedImage imageToSave = myJPanel.getFilteredImage();
                if (null != imageToSave) {
                    ImageIO.write(myJPanel.getFilteredImage(), "bmp", new File(jFileChooser.getSelectedFile().getAbsolutePath() + ".bmp"));
                } else {
                    JOptionPane.showMessageDialog(this, THERE_ISN_T_AN_IMAGE_THAT_COULD_BE_SAVED, ERROR_WHILE_SAVING, JOptionPane.ERROR_MESSAGE);
                }
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, e.getMessage(), ERROR_WHILE_SAVING, JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void onCToBButtonClicked() {
        myJPanel.copyCToB();
        repaint();
    }

    private void onBToCButtonClicked() {
        myJPanel.copyBToC();
        repaint();

    }

    private void onNewButtonClicked() {
        myJPanel.clear();
        changeSelectMode(false);

        repaint();

    }

    private void onSelectButtonClicked() {
        if (myJPanel.isSelectMode()) {
            changeSelectMode(false);
        } else {
            changeSelectMode(true);
        }
    }

    private void changeSelectMode(boolean selectMode) {
        myJPanel.setSelectMode(selectMode);
        selectButton.setSelected(selectMode);
        selectItem.setSelected(selectMode);
    }

    private void onOpenFileButtonClicked() {
        JFileChooser jFileChooser = new JFileChooser();

        FileNameExtensionFilter imageFilter = new FileNameExtensionFilter("*.png, *.bmp", "png", "bmp");
        jFileChooser.addChoosableFileFilter(imageFilter);
        jFileChooser.setAcceptAllFileFilterUsed(false);
        jFileChooser.setCurrentDirectory(new File(DATA_FOLDER));

        int result = jFileChooser.showOpenDialog(this);

        if (JFileChooser.APPROVE_OPTION == result) {
            try {
                myJPanel.loadNewImage(jFileChooser.getSelectedFile());
            } catch (IOException | LoadImageException e) {
                JOptionPane.showMessageDialog(this, e.getMessage(), CAN_NOT_LOAD_THE_IMAGE, JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
