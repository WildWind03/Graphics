package view;

import controller.*;
import model.Field;
import model.ModelConfiguration;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.Timer;

import static javax.swing.JOptionPane.INFORMATION_MESSAGE;

public class MyJFrame extends JFrame {
    private static final String TITLE = "Life";

    private static final String FILE = "File";
    private static final String CREATE = "Create";
    private static final String EXIT = "Exit";

    private static final String EDIT = "Edit";

    private static final String PROPERTIES = "Properties";
    private static final String ABOUT_THE_GAME = "About the game";

    private static final String HELP = "Help";
    private static final String OPEN = "Open";
    private static final String CLEAR = "Clear";
    private static final String XOR_MODE = "XOR mode";
    private static final String REPLACE = "Replace";
    private static final String IMPACT = "Impact";
    private static final String NEXT = "Next";

    private static final String ABOUT_AUTHOR_TEXT = "Life \n Created by Chirikhin Alexander \n 01.03.2017, NSU";

    private static final String NEW_ICON_PATH = "./src/main/resources/create_icon.png";
    private static final String OPEN_ICON_PATH = "./src/main/resources/open_icon.png";
    private static final String SAVE_ICON_PATH = "./src/main/resources/save_icon.png";

    private static final String CLEAR_SHORT = "clr";
    private static final String XOR_SHORT = "xor";
    private static final String REPLACE_SHORT = "rpl";
    private static final String PROPERTIES_SHORT = "opt";
    private static final String RUN_SHORT = "run";
    private static final String RUN = "Run";
    private static final String IMPACT_SHORT = "imp";
    private static final String NEXT_SHORT = "nxt";
    private static final String ABOUT_THE_GAME_SHORT = "?";
    private static final String CLOSE_SHORT = "X";
    private static final String CLOSE = "Close";

    private static final int MAX_SHOWING_WIDTH_START = 1280;
    private static final int MAX_SHOWING_HEIGHT_START = 720;

    private static final int RUN_PERIOD = 1000;
    private static final String SAVE = "Save";
    private static final String CHOOSE_NEW_FIELD_SIZE = "Choose new field size";
    private static final String SAVE_WARNING_REWRITE = "There is already a file with such name. Do you want to rewrite it?";
    private static final String SAVE_REWRITE_WRNING_TITLE = "Rewrite the file?";
    private static final String MODE = "Mode";
    private static final String SETTINGS = "Settings";

    private final JToolBar jToolBar;
    private final JButton newDocumentButton;
    private final JMenuBar jMenuBar;
    private final JButton openButton;

    private final JButton saveButton;

    private final JButton clearButton;
    private final JToggleButton xorButton;
    private final JToggleButton replaceButton;
    private final JButton propertiesButton;
    private final JToggleButton runButton;
    private final JToggleButton impactButton;
    private final JButton nextButton;

    private final JButton aboutAuthorButton;
    private final JButton exitButton;

    private final JScrollPane scrollPane;

    private final JCheckBoxMenuItem impactMenuItem;

    private final InitView initView;
    private final JMenuItem saveMenuFileItem;
    private final JMenuItem clearMenuItem;

    private boolean isRunningMode = false;
    private boolean isReplaceMode = true;

    private Runnable nextButtonAction;

    private Timer runTask;

    private boolean isChanged = true;
    private final JMenuItem createMenuFileItem;
    private final JMenuItem openMenuFileItem;
    private final JCheckBoxMenuItem xorMenuItem;
    private final JCheckBoxMenuItem replaceMenuItem;
    private final JCheckBoxMenuItem runMenuItem;
    private final JMenuItem propertiesMenuItem;
    private final JMenuItem nextMenuItem;
    private Runnable saveFunction;
    private ConfigurationDialog configurationDialog;

    public MyJFrame(int width, int height, int lineLength, int lineWidth) {
        super(TITLE);

        configurationDialog = new ConfigurationDialog(this);
        configurationDialog.setModal(true);

        initView = new InitView(width, height, lineLength, lineWidth);

        setVisible(true);
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                onExit();
            }
        });

        jMenuBar = new JMenuBar();

        JMenu fileMenu = new JMenu(FILE);
        createMenuFileItem = new JMenuItem(CREATE);
        fileMenu.add(createMenuFileItem);

        openMenuFileItem = new JMenuItem(OPEN);
        fileMenu.add(openMenuFileItem);

        saveMenuFileItem = new JMenuItem(SAVE);
        fileMenu.add(saveMenuFileItem);

        fileMenu.addSeparator();

        JMenuItem exitMenuFileItem = new JMenuItem(EXIT);
        setOnItemClickListener(exitMenuFileItem, this::onExit);

        fileMenu.add(exitMenuFileItem);
        jMenuBar.add(fileMenu);

        JMenu editMenu = new JMenu(EDIT);

        JMenu gameModeMenu = new JMenu(MODE);
        editMenu.add(gameModeMenu);

        editMenu.addSeparator();

        clearMenuItem = new JMenuItem(CLEAR);
        editMenu.add(clearMenuItem);

        nextMenuItem = new JMenuItem(NEXT);
        editMenu.add(nextMenuItem);

        editMenu.addSeparator();

        xorMenuItem = new JCheckBoxMenuItem(XOR_MODE);
        replaceMenuItem = new JCheckBoxMenuItem(REPLACE);

        setOnItemClickListener(xorMenuItem, this::onXORButtonClicked);
        setOnItemClickListener(replaceMenuItem, this::onReplaceButtonClicked);

        gameModeMenu.add(xorMenuItem);
        gameModeMenu.add(replaceMenuItem);

        runMenuItem = new JCheckBoxMenuItem(RUN);
        setOnItemClickListener(runMenuItem, this::onRunButtonClicked);
        editMenu.add(runMenuItem);


        impactMenuItem = new JCheckBoxMenuItem(IMPACT);
        setOnItemClickListener(impactMenuItem, this::onImpactButtonClicked);
        editMenu.add(impactMenuItem);

        jMenuBar.add(editMenu);

        JMenu propertiesMenu = new JMenu(PROPERTIES);
        jMenuBar.add(propertiesMenu);
        propertiesMenuItem = new JMenuItem(SETTINGS);
        propertiesMenu.add(propertiesMenuItem);

        JMenu helpMenu = new JMenu(HELP);
        JMenuItem aboutGameMenuItem = new JMenuItem(ABOUT_THE_GAME);
        setOnItemClickListener(aboutGameMenuItem, this::showInformationAboutProgram);
        helpMenu.add(aboutGameMenuItem);
        jMenuBar.add(helpMenu);

        setJMenuBar(jMenuBar);

        jToolBar = new JToolBar();

        ImageIcon newIcon = new ImageIcon(NEW_ICON_PATH);
        newDocumentButton = new JButton(newIcon);
        newDocumentButton.setToolTipText(CREATE);
        jToolBar.add(newDocumentButton);

        ImageIcon openIcon = new ImageIcon(OPEN_ICON_PATH);
        openButton = new JButton(openIcon);
        openButton.setToolTipText(OPEN);
        jToolBar.add(openButton);

        ImageIcon saveIcon = new ImageIcon(SAVE_ICON_PATH);
        saveButton = new JButton(saveIcon);
        saveButton.setToolTipText(SAVE);
        jToolBar.add(saveButton);

        jToolBar.addSeparator();

        clearButton = new JButton(CLEAR_SHORT);
        clearButton.setToolTipText(CLEAR);
        jToolBar.add(clearButton);

        xorButton = new JToggleButton(XOR_SHORT);
        xorButton.setToolTipText(XOR_MODE);
        setOnActionListener(xorButton, this::onXORButtonClicked);
        jToolBar.add(xorButton);

        replaceButton = new JToggleButton(REPLACE_SHORT);
        replaceButton.setToolTipText(REPLACE);
        setOnActionListener(replaceButton, this::onReplaceButtonClicked);
        jToolBar.add(replaceButton);

        propertiesButton = new JButton(PROPERTIES_SHORT);
        propertiesButton.setToolTipText(PROPERTIES);
        jToolBar.add(propertiesButton);

        runButton = new JToggleButton(RUN_SHORT);
        runButton.setToolTipText(RUN);
        setOnActionListener(runButton, this::onRunButtonClicked);
        jToolBar.add(runButton);

        impactButton = new JToggleButton(IMPACT_SHORT);
        impactButton.setToolTipText(IMPACT);
        setOnActionListener(impactButton, this::onImpactButtonClicked);
        jToolBar.add(impactButton);

        nextButton = new JButton(NEXT_SHORT);
        nextButton.setToolTipText(NEXT);
        jToolBar.add(nextButton);

        jToolBar.addSeparator();

        aboutAuthorButton = new JButton(ABOUT_THE_GAME_SHORT);
        aboutAuthorButton.setToolTipText(ABOUT_THE_GAME);
        setOnActionListener(aboutAuthorButton, this::showInformationAboutProgram);

        jToolBar.add(aboutAuthorButton);

        exitButton = new JButton(CLOSE_SHORT);
        exitButton.setToolTipText(CLOSE);
        setOnActionListener(exitButton, this::onExit);

        jToolBar.add(exitButton);

        add(jToolBar, BorderLayout.NORTH);

        scrollPane = new JScrollPane(initView);
        scrollPane.getViewport().setScrollMode(JViewport.SIMPLE_SCROLL_MODE);

        if (width > MAX_SHOWING_WIDTH_START || height > MAX_SHOWING_HEIGHT_START) {
            int realWindowWidth = Math.min(width, MAX_SHOWING_WIDTH_START);
            int realWindowHeight = Math.min(height, MAX_SHOWING_HEIGHT_START);

            scrollPane.setPreferredSize(new Dimension(realWindowWidth, realWindowHeight));
        }

        add(scrollPane, BorderLayout.CENTER);

        pack();

        onReplaceButtonClicked();
    }

    private void onXORButtonClicked() {
        isReplaceMode = false;
        replaceButton.setSelected(false);
        xorButton.setSelected(true);
        xorMenuItem.setSelected(true);
        replaceMenuItem.setSelected(false);
    }

    private void onReplaceButtonClicked() {
        isReplaceMode = true;
        replaceButton.setSelected(true);
        xorButton.setSelected(false);
        replaceMenuItem.setSelected(true);
        xorMenuItem.setSelected(false);
    }

    private void onRunButtonClicked() {
        if (!isRunningMode) {
            isRunningMode = true;
            createMenuFileItem.setEnabled(false);
            openMenuFileItem.setEnabled(false);
            saveMenuFileItem.setEnabled(false);

            clearMenuItem.setEnabled(false);
            xorMenuItem.setEnabled(false);
            replaceMenuItem.setEnabled(false);
            propertiesMenuItem.setEnabled(false);
            nextMenuItem.setEnabled(false);

            newDocumentButton.setEnabled(false);
            openButton.setEnabled(false);
            saveButton.setEnabled(false);

            clearButton.setEnabled(false);
            xorButton.setEnabled(false);
            replaceButton.setEnabled(false);
            propertiesButton.setEnabled(false);
            nextButton.setEnabled(false);

            runButton.setSelected(true);
            runMenuItem.setSelected(true);

            runTask = new Timer();
            runTask.schedule(new TimerTask() {
                @Override
                public void run() {
                    if (null != nextButtonAction) {
                        nextButtonAction.run();
                    }
                }
            }, 0, RUN_PERIOD);
        } else {
            runTask.cancel();
            isRunningMode = false;
            newDocumentButton.setEnabled(true);
            openButton.setEnabled(true);
            saveButton.setEnabled(true);

            clearButton.setEnabled(true);
            xorButton.setEnabled(true);
            replaceButton.setEnabled(true);
            propertiesButton.setEnabled(true);
            nextButton.setEnabled(true);

            createMenuFileItem.setEnabled(true);
            openMenuFileItem.setEnabled(true);
            saveMenuFileItem.setEnabled(true);

            clearMenuItem.setEnabled(true);
            xorMenuItem.setEnabled(true);
            replaceMenuItem.setEnabled(true);
            propertiesMenuItem.setEnabled(true);
            nextMenuItem.setEnabled(true);

            runButton.setSelected(false);
            runMenuItem.setSelected(false);
        }
    }

    public void updateSize(int width, int height) {
        initView.updateSize(width, height);

        if (width > MAX_SHOWING_WIDTH_START || height > MAX_SHOWING_HEIGHT_START) {
            int realWindowWidth = Math.min(width, MAX_SHOWING_WIDTH_START);
            int realWindowHeight = Math.min(height, MAX_SHOWING_HEIGHT_START);

            scrollPane.setPreferredSize(new Dimension(realWindowWidth, realWindowHeight));
        }

        pack();
    }

    public void repaintField(Field field) {
        initView.drawField(field);
    }

    public void setOnClickListener(TwoIntegerOneBooleanRunnable runnable) {
        deleteOnClickListener(initView);

        initView.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                if (!isRunningMode) {
                    if (initView.isCouldBeFilled(e.getX(), e.getY())) {
                        runnable.run(e.getX(), e.getY(), isReplaceMode);
                        isChanged = true;
                    }
                }
            }
        });
    }

    public void setOnMoveListener(TwoIntegerOneBooleanRunnable runnable) {
        deleteMouseMotionListener(initView);
        initView.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);

                if (!isRunningMode) {
                    if (initView.isCouldBeFilled(e.getX(), e.getY())) {
                        runnable.run(e.getX(), e.getY(), isReplaceMode);
                        isChanged = true;
                    }
            }
        }});
    }

    public void setOnNextButtonListener(Runnable runnable) {
        Runnable nextButtonFunction = () -> {
            isChanged = true;
            runnable.run();
        };

        setOnActionListener(nextButton, nextButtonFunction);
        setOnItemClickListener(nextMenuItem, nextButtonFunction);
        this.nextButtonAction = nextButtonFunction;
    }

    private void onExit() {
        if (isChanged) {
            int result = JOptionPane.showConfirmDialog(this, "Do you want to save the field?", "Really exit?", JOptionPane.YES_NO_CANCEL_OPTION);

            if (result == JOptionPane.NO_OPTION) {
                System.exit(0);
            }

            if (result == JOptionPane.YES_OPTION) {
                saveFunction.run();
            }


        } else {
            System.exit(0);
        }
    }

    private void showInformationAboutProgram() {
        JOptionPane.showMessageDialog(this, ABOUT_AUTHOR_TEXT, ABOUT_THE_GAME, INFORMATION_MESSAGE);
    }

    private void onImpactButtonClicked() {
        if (initView.isImpactShowing()) {
            initView.changeImpactMode(false);
            impactMenuItem.setSelected(false);
            impactButton.setSelected(false);
        } else {
            initView.changeImpactMode(true);
            impactMenuItem.setSelected(true);
            impactButton.setSelected(true);
        }

        initView.repaint();
    }

    private void setOnItemClickListener(JMenuItem jMenuItem, Runnable runnable) {
        deleteActionListener(jMenuItem);
        jMenuItem.addActionListener(e -> runnable.run());
    }

    public void setOnClearButtonListener(Runnable runnable) {
        setOnActionListener(clearButton, runnable);
        setOnItemClickListener(clearMenuItem, runnable);
    }

    private void setOnActionListener(AbstractButton jToggleButton, Runnable runnable) {
        deleteActionListener(jToggleButton);
        jToggleButton.addActionListener(e -> runnable.run());
    }

    public void setOnNewGameListener(TwoIntegerRunnable runnable) {
        Runnable newGameFunction = () -> {
            DialogMultipleInput.Result result = DialogMultipleInput.show(CHOOSE_NEW_FIELD_SIZE);
            if (null != result) {
                runnable.run(result.getWidth(), result.getHeight());
            }
        };

        setOnActionListener(newDocumentButton, newGameFunction);
        setOnItemClickListener(createMenuFileItem, newGameFunction);
    }

    public void setOnSaveGameListener(StringRunnable runnable) {
        saveFunction = () -> {
            JFileChooser jFileChooser = new JFileChooser();
            FileNameExtensionFilter fileFilter = new FileNameExtensionFilter("Map file", "txt");

            jFileChooser.setFileFilter(fileFilter);
            jFileChooser.addChoosableFileFilter(fileFilter);

            int result = jFileChooser.showSaveDialog(this);

            if (JFileChooser.APPROVE_OPTION == result) {
                try {
                    File file = jFileChooser.getSelectedFile();

                    if (file.exists()) {
                        int isConfirm = JOptionPane.showConfirmDialog(this, SAVE_WARNING_REWRITE, SAVE_REWRITE_WRNING_TITLE, JOptionPane.YES_NO_OPTION);

                        if (JOptionPane.OK_OPTION != isConfirm) {
                            return;
                        }
                    }

                    runnable.run(jFileChooser.getSelectedFile().getCanonicalPath() + ".txt");
                    isChanged = false;
                } catch (FileException | IOException e) {
                    JOptionPane.showMessageDialog(this, e.getMessage());
                }
            }
        };

        setOnActionListener(saveButton, saveFunction);
        setOnItemClickListener(saveMenuFileItem, saveFunction);
    }

    public void setOnOpenGameListener(StringRunnable runnable) {
        Runnable openGameFunction = () -> {
            JFileChooser jFileChooser = new JFileChooser();
            FileNameExtensionFilter fileFilter = new FileNameExtensionFilter("Map file", "txt");

            jFileChooser.setFileFilter(fileFilter);
            jFileChooser.addChoosableFileFilter(fileFilter);

            int returnVal = jFileChooser.showOpenDialog(this);

            if (JFileChooser.APPROVE_OPTION == returnVal) {
                try {
                    runnable.run(jFileChooser.getSelectedFile().toString());
                } catch (FileException invalidGameFile) {
                    JOptionPane.showMessageDialog(this, invalidGameFile.getMessage());
                }
            }
        };

        setOnActionListener(openButton, openGameFunction);
        setOnItemClickListener(openMenuFileItem, openGameFunction);
    }

    public void deleteActionListener(AbstractButton abstractButton) {
        ActionListener[] actionListeners = abstractButton.getActionListeners();

        for (ActionListener actionListener : actionListeners) {
            abstractButton.removeActionListener(actionListener);
        }
    }

    public void setOnConfigurationChangedListener(ConfigurationRunnable runnable, ConfigurationGetter modelConfiguration) {
        Runnable configurationChanged = () -> {
            Configuration currentConfiguration = new Configuration(modelConfiguration.getModelConfiguration(), getLineLength(), getLineWidth(), isReplaceMode);
            configurationDialog.apparate(currentConfiguration);

            Configuration configuration = configurationDialog.getConfiguration();
            runnable.run(configuration);
        };

        setOnActionListener(propertiesButton, configurationChanged);
        setOnItemClickListener(propertiesMenuItem, configurationChanged);
    }

    public void deleteMouseMotionListener(JPanel jPanel) {
        MouseMotionListener[] mouseMotionListeners = jPanel.getMouseMotionListeners();

        for (MouseMotionListener mouseMotionListener : mouseMotionListeners) {
            jPanel.removeMouseMotionListener(mouseMotionListener);
        }
    }

    public void deleteOnClickListener(JPanel jPanel) {
        MouseListener[] mouseListener = jPanel.getMouseListeners();

        for (MouseListener mouseListener1 : mouseListener) {
            jPanel.removeMouseListener(mouseListener1);
        }
    }

    public void updateLineWidth(int lineWidth) {
        initView.updateLineWidth(lineWidth);
    }

    public void updateLineLength(int lineLength) {
        initView.updateLineLength(lineLength);
    }

    public int getLineLength() {
        return initView.getLineLength();
    }

    public int getLineWidth() {
        return initView.getLineWidth();
    }

    public void setGameMode(boolean isReplaceMode) {
        this.isReplaceMode = isReplaceMode;
    }
}
