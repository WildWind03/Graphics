package ru.nsu.fit.g14201.chirikhin.isolines.view;

import com.chirikhin.swing.util.ListenerUtil;
import com.chirikhin.swing.util.MenuUtil;
import com.chirikhin.swing.util.ToolBarUtil;
import com.chirikhin.universal_parser.ParserException;
import com.chirikhin.universal_parser.TypeConversionException;
import com.chirikhin.universal_parser.TypeMatchingException;
import com.sun.corba.se.impl.orb.ORBConfiguratorImpl;
import ru.nsu.fit.g14201.chirikhin.isolines.config_parser.MyParser;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.io.IOException;

public class MyJFrame extends JFrame {
    private static final int DEFAULT_WIDTH = 500;
    private static final int DEFAULT_HEIGHT = 500;
    private static final String OPEN_ICON_PNG = "/open_icon.png";
    private static final String OPEN_CONFIG_TOOLTIP = "Open config";
    private static final String FILE_MENU = "File";
    private static final String OPEN_MENU_ITEM = "Open";
    private static final String DATA_FOLDER = "./FIT_g14201_Chirikhin_Isolines_Data";
    private static final String ERROR_WHILE_LOADING_CONFIG = "Error while loading config";
    private final MyJPanel myJPanel;

    public MyJFrame() {
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
        JMenuItem openItem = MenuUtil.addNewMenuItem(fileMenu, OPEN_MENU_ITEM);
        JButton openButton = ToolBarUtil.addNewButton(jToolBar, new ImageIcon(getClass().getResource(OPEN_ICON_PNG)), OPEN_CONFIG_TOOLTIP);

        ListenerUtil.setListener(openButton, openItem, this::onOpenButtonClicked);
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
