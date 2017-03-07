package view;

import javax.swing.*;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.text.NumberFormat;

public class ConfigurationDialog extends JDialog {

    private static final int TEXT_FIELD_SIZE = 3;

    private static final int MAX_WIDTH_VALUE = 250;
    private static final int MIN_WIDTH_VALUE = 1;

    private static final int MAX_HEIGHT_VALUE = 250;
    private static final int MIN_HEIGHT_VALUE = 1;

    private static final String WIDTH = "width";
    private static final String HEIGHT = "height";
    private static final String CONFIGURATION = "Configuration";
    private static final String FIELD_SIZES = "Field sizes";
    private static final String GAME_MODE = "Game mode";
    private static final String GAME_PROPERTIES = "Game properties";
    private static final String LINE_LENGTH = "line length";
    private static final String LINE_WIDTH = "line width";
    private static final String XOR = "XOR";
    private static final String REPLACE = "Replace";
    private static final int LINE_LENGTH_MIN = 10;
    private static final int LINE_LENGTH_MAX = 60;
    private static final int LINE_WIDTH_MIN = 1;
    private static final int LINE_WIDTH_MAX = 10;
    private static final float MIN_GAME_PROPERTIES = 0.1F;
    private static final float MAX_GAME_PROPERTIES = 30F;
    private static final String LIFE_ENDS = "life ends";
    private static final String BIRTH_BEGINS = "birth begins";
    private static final String BIRTH_ENDS = "birth ends";
    private static final String FIRST_IMPACT = "first impact";
    private static final String SECOND_IMPACT = "second impact";
    private static final String LIFE_BEGINS = "life begins";
    private static final String CANCEL = "Cancel";
    private static final String OK = "OK";

    private Configuration configuration;
    private final JFormattedTextField xField;
    private final JFormattedTextField yField;
    private final JFormattedTextField lineLength;
    private final JFormattedTextField lineWidth;
    private final JSlider jSlider;
    private final JRadioButton xorButton;
    private final JRadioButton replaceButton;
    private final JFormattedTextField lifeEndsTextField;
    private final JFormattedTextField birthBeginsTextField;
    private final JFormattedTextField birthEndsTextField;
    private final JFormattedTextField firstImpact;
    private final JFormattedTextField secondImpact;
    private final JFormattedTextField lifeBeginsTextField;

    public ConfigurationDialog(JFrame jFrame)  {
        super(jFrame, CONFIGURATION, false);

        setLayout(new BorderLayout());

        JPanel fieldSizePanel = new JPanel();
        JPanel gameModePanel = new JPanel();
        JPanel gamePropertiesPanel = new JPanel();

        GridBagLayout fieldSizeLayout = new GridBagLayout();
        fieldSizePanel.setLayout(fieldSizeLayout);

        GridBagLayout gameModeLayout = new GridBagLayout();
        gameModePanel.setLayout(gameModeLayout);

        GridBagLayout gamePropertiesLayout = new GridBagLayout();
        gamePropertiesPanel.setLayout(gamePropertiesLayout);

        JTabbedPane jTabbedPane = new JTabbedPane();
        jTabbedPane.addTab(FIELD_SIZES, fieldSizePanel);
        jTabbedPane.addTab(GAME_MODE, gameModePanel);
        jTabbedPane.addTab(GAME_PROPERTIES, gamePropertiesPanel);

        add(jTabbedPane, BorderLayout.CENTER);
        JPanel buttonPanel = new JPanel();

        JButton okButton = new JButton(OK);
        JButton cancelButton = new JButton(CANCEL);

        cancelButton.addActionListener(e -> setVisible(false));

        buttonPanel.add(okButton, BorderLayout.WEST);
        buttonPanel.add(cancelButton, BorderLayout.EAST);
        add(buttonPanel, BorderLayout.SOUTH);

        xField = getFormattedTextField(MIN_WIDTH_VALUE, MAX_WIDTH_VALUE);
        xField.setColumns(TEXT_FIELD_SIZE);
        yField = getFormattedTextField(MIN_HEIGHT_VALUE, MAX_HEIGHT_VALUE);
        yField.setColumns(TEXT_FIELD_SIZE);

        addComponent(fieldSizePanel, 0, 0, new JLabel(WIDTH));
        addComponent(fieldSizePanel, 1, 0, xField);
        addComponent(fieldSizePanel, 0, 1, new JLabel(HEIGHT));
        addComponent(fieldSizePanel, 1, 1, yField);

        lineLength = getFormattedTextField(LINE_LENGTH_MIN, LINE_LENGTH_MAX);
        lineWidth = getFormattedTextField(LINE_WIDTH_MIN, LINE_WIDTH_MAX);

        addComponent(fieldSizePanel, 0, 2, new JLabel(LINE_WIDTH));
        addComponent(fieldSizePanel, 1, 2, lineWidth);

        addComponent(fieldSizePanel, 0, 3, new JLabel(LINE_LENGTH));
        addComponent(fieldSizePanel, 1, 3, lineLength);

        jSlider = new JSlider();
        jSlider.setMinimum(LINE_LENGTH_MIN);
        jSlider.setMaximum(LINE_LENGTH_MAX);

        jSlider.addChangeListener(e -> {
            lineLength.setValue(jSlider.getValue());
        });

        lineLength.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                super.focusLost(e);
                int value = (int) lineLength.getValue();
                jSlider.setValue(value);
            }
        });

        GridBagConstraints jSliderConstraint = new GridBagConstraints();
        Insets insets = new Insets(5, 10, 5, 10);
        jSliderConstraint.gridx = 0;
        jSliderConstraint.gridy = 4;
        jSliderConstraint.insets = insets;
        jSliderConstraint.gridwidth = 2;

        fieldSizePanel.add(jSlider, jSliderConstraint);

        xorButton = new JRadioButton(XOR);
        replaceButton = new JRadioButton(REPLACE);

        xorButton.addActionListener(e -> {
            replaceButton.setSelected(false);
            xorButton.setSelected(true);
        });

        replaceButton.addActionListener(e -> {
            xorButton.setSelected(false);
            replaceButton.setSelected(true);
        });

        addComponent(gameModePanel, 0, 0, xorButton);
        addComponent(gameModePanel, 1, 0, replaceButton);

        addComponent(gamePropertiesPanel, 0, 0, new JLabel(LIFE_BEGINS));
        lifeBeginsTextField = getFormattedTextField(MIN_GAME_PROPERTIES, MAX_GAME_PROPERTIES);
        addComponent(gamePropertiesPanel, 1, 0, lifeBeginsTextField);

        lifeEndsTextField = getFormattedTextField(MIN_GAME_PROPERTIES, MAX_GAME_PROPERTIES);
        birthBeginsTextField = getFormattedTextField(MIN_GAME_PROPERTIES, MAX_GAME_PROPERTIES);
        birthEndsTextField = getFormattedTextField(MIN_GAME_PROPERTIES, MAX_GAME_PROPERTIES);
        firstImpact = getFormattedTextField(MIN_GAME_PROPERTIES, MAX_GAME_PROPERTIES);
        secondImpact = getFormattedTextField(MIN_GAME_PROPERTIES, MAX_GAME_PROPERTIES);

        lifeBeginsTextField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                super.focusLost(e);

                double newValue = ((Number) lifeBeginsTextField.getValue()).doubleValue();
                double birthBeginValue = ((Number) birthBeginsTextField.getValue()).doubleValue();

                if (newValue > birthBeginValue) {
                    lifeBeginsTextField.setValue(birthBeginValue);
                }
            }
        });

        birthBeginsTextField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                super.focusLost(e);

                double lifeBeginValue = ((Number) lifeBeginsTextField.getValue()).doubleValue();
                double newValue = ((Number) birthBeginsTextField.getValue()).doubleValue();
                double birthEndValue = ((Number) birthEndsTextField.getValue()).doubleValue();

                if (newValue < lifeBeginValue) {
                    birthBeginsTextField.setValue(lifeBeginValue);
                }

                if (newValue > birthEndValue) {
                    birthBeginsTextField.setValue(birthEndValue);
                }
            }
        });

        birthEndsTextField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                super.focusLost(e);

                double lifeEndValue = ((Number) lifeEndsTextField.getValue()).doubleValue();
                double birthBeginsValue = ((Number) birthBeginsTextField.getValue()).doubleValue();
                double newValue = ((Number) birthEndsTextField.getValue()).doubleValue();

                if (newValue < birthBeginsValue) {
                    birthEndsTextField.setValue(birthBeginsValue);
                }

                if (newValue > lifeEndValue) {
                    birthEndsTextField.setValue(lifeEndValue);
                }
            }
        });

        lifeEndsTextField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                super.focusLost(e);

                double birthEnds = ((Number) birthEndsTextField.getValue()).doubleValue();
                double lifeEndValue = ((Number) lifeEndsTextField.getValue()).doubleValue();

                if (lifeEndValue < birthEnds) {
                    lifeEndsTextField.setValue(birthEnds);
                }
            }
        });

        addComponent(gamePropertiesPanel, 0, 1, new JLabel(LIFE_ENDS));
        addComponent(gamePropertiesPanel, 1, 1, lifeEndsTextField);

        addComponent(gamePropertiesPanel, 0, 2, new JLabel(BIRTH_BEGINS));
        addComponent(gamePropertiesPanel, 1, 2, birthBeginsTextField);

        addComponent(gamePropertiesPanel, 0, 3, new JLabel(BIRTH_ENDS));
        addComponent(gamePropertiesPanel, 1, 3, birthEndsTextField);

        addComponent(gamePropertiesPanel, 0, 4, new JLabel(FIRST_IMPACT));
        addComponent(gamePropertiesPanel, 1, 4, firstImpact);

        addComponent(gamePropertiesPanel, 0, 5, new JLabel(SECOND_IMPACT));
        addComponent(gamePropertiesPanel, 1, 5, secondImpact);

        setMinimumSize(getPreferredSize());

        okButton.addActionListener(e -> {
            configuration = new Configuration(((Number) xField.getValue()).intValue(),
                    ((Number) yField.getValue()).intValue(),
                    ((Number) lineWidth.getValue()).intValue(),
                    ((Number) lineLength.getValue()).intValue(),
                    ((Number) lifeBeginsTextField.getValue()).floatValue(),
                    ((Number) lifeEndsTextField.getValue()).floatValue(),
                    ((Number) birthBeginsTextField.getValue()).floatValue(),
                    ((Number) birthEndsTextField.getValue()).floatValue(),
                    ((Number) firstImpact.getValue()).floatValue(),
                    ((Number) secondImpact.getValue()).floatValue(), replaceButton.isSelected());

            setVisible(false);
        });


        pack();
    }

    public void apparate(Configuration configuration) {
        this.configuration = configuration;

        xField.setValue(configuration.getWidth());
        yField.setValue(configuration.getHeight());
        lineWidth.setValue(configuration.getLineWidth());
        lineLength.setValue(configuration.getLineLength());
        lifeBeginsTextField.setValue(configuration.getLiveBegin());
        lifeEndsTextField.setValue(configuration.getLiveEnd());

        birthBeginsTextField.setValue(configuration.getBirthBegin());
        birthEndsTextField.setValue(configuration.getBirthEnd());
        firstImpact.setValue(configuration.getFirstImpact());
        secondImpact.setValue(configuration.getSecondImpact());

        jSlider.setValue(configuration.getLineLength());

        if (configuration.isReplaceMode()) {
            replaceButton.setSelected(true);
            xorButton.setSelected(false);
        } else {
            replaceButton.setSelected(false);
            xorButton.setSelected(true);
        }

        setVisible(true);
    }

    private void addComponent(JPanel jPanel, int row, int column, JComponent jComponent) {
        GridBagConstraints constraints = new GridBagConstraints();

        Insets insets = new Insets(5, 10, 5, 10);

        constraints.gridx = row;
        constraints.gridy = column;
        constraints.insets = insets;

        jPanel.add(jComponent, constraints);
    }

    private JFormattedTextField getFormattedTextField(int min, int max) {
        NumberFormatter lineWidthFormatter = new NumberFormatter(NumberFormat.getIntegerInstance());
        lineWidthFormatter.setMinimum(min);
        lineWidthFormatter.setMaximum(max);
        lineWidthFormatter.setCommitsOnValidEdit(true);

        JFormattedTextField jFormattedTextField = new JFormattedTextField(lineWidthFormatter);
        jFormattedTextField.setColumns(TEXT_FIELD_SIZE);

        return jFormattedTextField;
    }

    private JFormattedTextField getFormattedTextField(double min, double max) {
        NumberFormatter lineWidthFormatter = new NumberFormatter(NumberFormat.getNumberInstance());
        lineWidthFormatter.setMinimum(min);
        lineWidthFormatter.setMaximum(max);
        lineWidthFormatter.setCommitsOnValidEdit(true);

        JFormattedTextField jFormattedTextField = new JFormattedTextField(lineWidthFormatter);
        jFormattedTextField.setColumns(TEXT_FIELD_SIZE);

        return jFormattedTextField;
    }

    public Configuration getConfiguration() {
        return configuration;
    }


}
