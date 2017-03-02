package view;

import controller.MyRunnable;
import model.Field;

import javax.management.relation.RoleUnresolved;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.Timer;
import java.util.logging.Logger;

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

    private final JCheckBoxMenuItem impactMenuItem;

    private final InitView initView;

    private boolean isRunningMode = false;

    private Runnable nextButtonAction;

    private Timer runTask;

    public MyJFrame(int width, int height, int lineLength) {
        super(TITLE);

        initView = new InitView(width, height, lineLength);

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
        JMenuItem createMenuFileItem = new JMenuItem(CREATE);
        fileMenu.add(createMenuFileItem);

        JMenuItem exitMenuFileItem = new JMenuItem(EXIT);
        addOnItemClickListener(exitMenuFileItem, this::onExit);

        fileMenu.add(exitMenuFileItem);
        jMenuBar.add(fileMenu);

        JMenu editMenu = new JMenu(EDIT);

        impactMenuItem = new JCheckBoxMenuItem(IMPACT);
        addOnItemClickListener(impactMenuItem, this::onImpactButtonClicked);
        editMenu.add(impactMenuItem);

        jMenuBar.add(editMenu);

        JMenu propertiesMenu = new JMenu(PROPERTIES);
        jMenuBar.add(propertiesMenu);

        JMenu helpMenu = new JMenu(HELP);
        JMenuItem aboutGameMenuItem = new JMenuItem(ABOUT_THE_GAME);
        addOnItemClickListener(aboutGameMenuItem, this::showInformationAboutProgram);
        helpMenu.add(aboutGameMenuItem);
        jMenuBar.add(helpMenu);

        setJMenuBar(jMenuBar);

        jToolBar = new JToolBar();

        ImageIcon newIcon = new ImageIcon(NEW_ICON_PATH);
        newDocumentButton = new JButton(newIcon);
        newDocumentButton.setToolTipText(CREATE);
        addOnActionListener(newDocumentButton, this::onCreateButtonClicked);
        jToolBar.add(newDocumentButton);

        ImageIcon openIcon = new ImageIcon(OPEN_ICON_PATH);
        openButton = new JButton(openIcon);
        openButton.setToolTipText(OPEN);
        addOnActionListener(openButton, this::onOpenButtonClicked);
        jToolBar.add(openButton);

        ImageIcon saveIcon = new ImageIcon(SAVE_ICON_PATH);
        saveButton = new JButton(saveIcon);
        saveButton.setToolTipText(SAVE);
        addOnActionListener(saveButton, this::onSaveButtonClicked);
        jToolBar.add(saveButton);

        jToolBar.addSeparator();

        clearButton = new JButton(CLEAR_SHORT);
        clearButton.setToolTipText(CLEAR);
        jToolBar.add(clearButton);

        xorButton = new JToggleButton(XOR_SHORT);
        xorButton.setToolTipText(XOR_MODE);
        jToolBar.add(xorButton);

        replaceButton = new JToggleButton(REPLACE_SHORT);
        replaceButton.setToolTipText(REPLACE);
        jToolBar.add(replaceButton);

        propertiesButton = new JButton(PROPERTIES_SHORT);
        propertiesButton.setToolTipText(PROPERTIES);
        jToolBar.add(propertiesButton);

        runButton = new JToggleButton(RUN_SHORT);
        runButton.setToolTipText(RUN);
        addOnActionListener(runButton, this::onRunButtonClicked);
        jToolBar.add(runButton);

        impactButton = new JToggleButton(IMPACT_SHORT);
        impactButton.setToolTipText(IMPACT);
        addOnActionListener(impactButton, this::onImpactButtonClicked);
        jToolBar.add(impactButton);

        nextButton = new JButton(NEXT_SHORT);
        nextButton.setToolTipText(NEXT);
        jToolBar.add(nextButton);

        jToolBar.addSeparator();

        aboutAuthorButton = new JButton(ABOUT_THE_GAME_SHORT);
        aboutAuthorButton.setToolTipText(ABOUT_THE_GAME);
        addOnActionListener(aboutAuthorButton, this::showInformationAboutProgram);

        jToolBar.add(aboutAuthorButton);

        exitButton = new JButton(CLOSE_SHORT);
        exitButton.setToolTipText(CLOSE);
        addOnActionListener(exitButton, this::onExit);

        jToolBar.add(exitButton);

        add(jToolBar, BorderLayout.NORTH);

        JScrollPane scrollPane = new JScrollPane(initView);
        scrollPane.getViewport().setScrollMode(JViewport.SIMPLE_SCROLL_MODE);
        if (width > MAX_SHOWING_WIDTH_START || height > MAX_SHOWING_HEIGHT_START) {
            int realWindowWidth = Math.min(width, MAX_SHOWING_WIDTH_START);
            int realWindowHeight = Math.min(height, MAX_SHOWING_HEIGHT_START);

            scrollPane.setPreferredSize(new Dimension(realWindowWidth, realWindowHeight));
        }

        add(scrollPane, BorderLayout.CENTER);

        pack();
    }

    private void onRunButtonClicked() {
        if (!isRunningMode) {
            isRunningMode = true;
            newDocumentButton.setEnabled(false);
            openButton.setEnabled(false);
            saveButton.setEnabled(false);

            clearButton.setEnabled(false);
            xorButton.setEnabled(false);
            replaceButton.setEnabled(false);
            propertiesButton.setEnabled(false);
            nextButton.setEnabled(false);

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
        }
    }

    public void repaintField(Field field) {
        initView.drawField(field);
    }

    public void addOnClickListener(MyRunnable runnable) {

        initView.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!isRunningMode) {
                    runnable.run(e.getX(), e.getY());
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
    }

    public void addOnMoveListener(MyRunnable runnable) {
        initView.addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (!isRunningMode) {
                    runnable.run(e.getX(), e.getY());
                }
            }

            @Override
            public void mouseMoved(MouseEvent e) {

            }
        });
    }

    public void addOnNextButtonListener(Runnable runnable) {
        addOnActionListener(nextButton, runnable);
        this.nextButtonAction = runnable;
    }

    private void onExit() {
        System.exit(0);
    }

    private void showInformationAboutProgram() {
        JOptionPane.showMessageDialog(this, ABOUT_AUTHOR_TEXT, ABOUT_THE_GAME, INFORMATION_MESSAGE);
    }

    private void onOpenButtonClicked() {

    }

    private void onCreateButtonClicked() {
        DialogMultipleInput.show("Please, choose new field size");
    }

    private void onSaveButtonClicked() {

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

    private void addOnItemClickListener(JMenuItem jMenuItem, Runnable runnable) {
        jMenuItem.addActionListener(e -> runnable.run());
    }

    public void addOnClearButtonListener(Runnable runnable) {
        addOnActionListener(clearButton, runnable);
    }

    private void addOnActionListener(AbstractButton jToggleButton, Runnable runnable) {
        jToggleButton.addActionListener(e -> runnable.run());
    }

    private void addOnActionListener(AbstractButton abstractButton, MyRunnable myRunnable) {
        //abstractButton.addActionListener(e -> myRunnable.run());
    }

    public void addOnNewGameListener(MyRunnable runnable) {
        addOnActionListener(newDocumentButton, runnable);
    }

    public void addOnSaveGameListener(Runnable runnable) {
        addOnActionListener(saveButton, runnable);
    }

    public void addOnOpenGameListener(Runnable runnable) {
        addOnActionListener(openButton, runnable);
    }
}
