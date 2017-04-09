package ru.nsu.fit.g14201.chirikhin.isolines.view;

import com.chirikhin.swing.util.DoubleUtil;

import javax.swing.*;

public class StatusBar extends JPanel {
    private final JLabel barLabel;
    private static final int COUNT_OF_SYMBOLS_AFTER_INTEGER_PART = 2;

    public StatusBar() {
        barLabel = new JLabel();
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        add(barLabel);

        updateValues(0d, 0d, 0d);
    }

    public void updateValues(double x, double y, double value) {
        barLabel.setText("x: " + DoubleUtil.getDouble(x, COUNT_OF_SYMBOLS_AFTER_INTEGER_PART) +
                " y: " + DoubleUtil.getDouble(y, COUNT_OF_SYMBOLS_AFTER_INTEGER_PART) +
                " value: " + DoubleUtil.getDouble(value, COUNT_OF_SYMBOLS_AFTER_INTEGER_PART));
    }
}
