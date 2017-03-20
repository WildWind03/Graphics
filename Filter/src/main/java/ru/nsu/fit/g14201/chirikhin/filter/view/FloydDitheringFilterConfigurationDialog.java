package ru.nsu.fit.g14201.chirikhin.filter.view;

import javax.swing.*;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.NumberFormat;

public class FloydDitheringFilterConfigurationDialog extends JDialog {

    private static final String TITLE = "Floyd filter configuration";
    private static final int TEXT_FIELD_SIZE = 3;
    private static final int MIN = 2;
    private static final int MAX = 16;

    private final JFormattedTextField greenDivisionTextField;
    private final JFormattedTextField blueDivisionTextField;
    private final JFormattedTextField redDivisionTextField;

    private boolean isCancelled = true;

    public FloydDitheringFilterConfigurationDialog(JFrame jFrame) {
        super(jFrame, TITLE, true);

        GridBagLayout gridBagLayout = new GridBagLayout();
        setLayout(gridBagLayout);

        JLabel redDivisionLabel = new JLabel("Red divisions");
        redDivisionTextField = getFormattedTextField(MIN, MAX);
        redDivisionTextField.setColumns(TEXT_FIELD_SIZE);


        addComponent(this, 0, 0, redDivisionLabel);
        addComponent(this, 1, 0, redDivisionTextField);

        JLabel greenDivisionLabel = new JLabel("Green divisions");
        greenDivisionTextField = getFormattedTextField(MIN, MAX);
        greenDivisionTextField.setColumns(TEXT_FIELD_SIZE);

        addComponent(this, 0, 1, greenDivisionLabel);
        addComponent(this, 1, 1, greenDivisionTextField);

        JLabel blueDivisionLabel = new JLabel("Blue divisions");
        blueDivisionTextField = getFormattedTextField(MIN, MAX);
        blueDivisionTextField.setColumns(TEXT_FIELD_SIZE);

        addComponent(this, 0, 2, blueDivisionLabel);
        addComponent(this, 1, 2, blueDivisionTextField);

        JButton okButton = new JButton("OK");
        JButton cancelButton = new JButton("Cancel");

        addComponent(this, 0, 3, okButton);
        addComponent(this, 1, 3, cancelButton);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                isCancelled = true;
                setVisible(false);
            }
        });

        cancelButton.addActionListener(e -> {
            isCancelled = true;
            setVisible(false);
        });

        okButton.addActionListener(e -> {
            isCancelled = false;
            setVisible(false);
        });

        greenDivisionTextField.setValue(MIN);
        blueDivisionTextField.setValue(MIN);
        redDivisionTextField.setValue(MIN);

        pack();
    }

    public void apparate() {
        setVisible(true);
    }

    public boolean isCancelled() {
        return isCancelled;
    }

    public int getGreenDivision() {
        return ((Number) greenDivisionTextField.getValue()).intValue();
    }

    public int getBlueDivision() {
        return ((Number) blueDivisionTextField.getValue()).intValue();
    }

    public int getRedDivision() {
        return ((Number) redDivisionTextField.getValue()).intValue();
    }

    private void addComponent(Container container, int row, int column, JComponent jComponent) {
        GridBagConstraints constraints = new GridBagConstraints();

        Insets insets = new Insets(5, 10, 5, 10);

        constraints.gridx = row;
        constraints.gridy = column;
        constraints.insets = insets;

        container.add(jComponent, constraints);
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
}
