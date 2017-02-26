package view;

import model.Field;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.logging.Logger;

public class MyJFrame extends JFrame {
    private static final Logger logger = Logger.getLogger(MyJFrame.class.getName());

    private final int height;
    private final int width;

    private JFrame jFrame;
    private JToolBar jToolBar;
    private JButton newDocumentButton;
    private JMenuBar jMenuBar;
    private JButton openButton;

    private JButton clearButton;
    private JToggleButton xorButton;
    private JToggleButton replaceButton;
    private JButton propertiesButton;
    private JToggleButton runButton;
    private JToggleButton impactButton;
    private JButton nextButton;

    private JButton aboutAuthorButton;

    private InitView initView;

    public MyJFrame(int width, int height, int lineLength) {
        this.width = width;
        this.height = height;

        initView = new InitView(width, height, lineLength);

        jFrame = new JFrame();
        jFrame.setSize(width, height);
        jFrame.setVisible(true);

        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        jMenuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        JMenuItem createMenuFileItem = new JMenuItem("Create");
        fileMenu.add(createMenuFileItem);
        JMenuItem exitMenuFileItem = new JMenuItem("Exit");
        fileMenu.add(exitMenuFileItem);
        jMenuBar.add(fileMenu);

        JMenu editMenu = new JMenu("Edit");
        jMenuBar.add(editMenu);

        JMenu propertiesMenu = new JMenu("Properties");
        jMenuBar.add(propertiesMenu);

        JMenu helpMenu = new JMenu("Help");
        jMenuBar.add(helpMenu);

        jFrame.setJMenuBar(jMenuBar);

        jToolBar = new JToolBar();

        ImageIcon newIcon = new ImageIcon("./src/main/resources/new_icon_22.png");
        newDocumentButton = new JButton(newIcon);
        newDocumentButton.setToolTipText("Create");
        jToolBar.add(newDocumentButton);

        ImageIcon openIcon = new ImageIcon("./src/main/resources/open_icon_22.png");
        openButton = new JButton(openIcon);
        openButton.setToolTipText("Open");
        jToolBar.add(openButton);

        jToolBar.addSeparator();

        clearButton = new JButton("clr");
        clearButton.setToolTipText("Clear");
        jToolBar.add(clearButton);

        xorButton = new JToggleButton("xor");
        xorButton.setToolTipText("XOR Mode");
        jToolBar.add(xorButton);

        replaceButton = new JToggleButton("rpl");
        replaceButton.setToolTipText("Replace");
        jToolBar.add(replaceButton);

        propertiesButton = new JButton("opt");
        propertiesButton.setToolTipText("Properties");
        jToolBar.add(propertiesButton);

        runButton = new JToggleButton("run");
        runButton.setToolTipText("Run");
        jToolBar.add(runButton);

        impactButton = new JToggleButton("imp");
        impactButton.setToolTipText("Impact");
        jToolBar.add(impactButton);

        nextButton = new JButton("nxt");
        nextButton.setToolTipText("Next");
        jToolBar.add(nextButton);

        jToolBar.addSeparator();

        aboutAuthorButton = new JButton("?");
        aboutAuthorButton.setToolTipText("About author");

        jToolBar.add(aboutAuthorButton);

        jFrame.add(jToolBar, BorderLayout.NORTH);

        jFrame.add(initView);
    }

    public void repaintField(Field field) {
        initView.drawField(field);
    }

    public void addOnClickListener(MouseListener mouseListener) {
        initView.addMouseListener(mouseListener);
    }
}
