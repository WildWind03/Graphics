package ru.nsu.fit.g14201.chirikhin.filter.util;

import javax.swing.*;
import javax.swing.text.NumberFormatter;
import java.text.NumberFormat;

public class FormattedTextFieldUtil {
    private FormattedTextFieldUtil() {

    }

    public static JFormattedTextField getFormattedTextField(int min, int max, int textFiledSize) {
        NumberFormatter lineWidthFormatter = new NumberFormatter(NumberFormat.getIntegerInstance());
        lineWidthFormatter.setMinimum(min);
        lineWidthFormatter.setMaximum(max);
        lineWidthFormatter.setCommitsOnValidEdit(true);

        JFormattedTextField jFormattedTextField = new JFormattedTextField(lineWidthFormatter);
        jFormattedTextField.setColumns(textFiledSize);

        return jFormattedTextField;
    }
}