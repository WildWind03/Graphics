package ru.nsu.fit.g14201.chirikhin.filter.view;

import ru.nsu.fit.g14201.chirikhin.filter.util.FormattedTextFieldUtil;

import javax.swing.*;
import java.util.HashMap;

public class FloydDitheringFilterConfigurationDialog extends MyDialog {

    private static final String TITLE = "Floyd filter configuration";
    private static final int TEXT_FIELD_SIZE = 3;
    private static final int MIN = 2;
    private static final int MAX = 16;

    private JFormattedTextField greenDivisionTextField;
    private JFormattedTextField blueDivisionTextField;
    private JFormattedTextField redDivisionTextField;

    public FloydDitheringFilterConfigurationDialog(JFrame jFrame) {
        super(jFrame, TITLE);
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


    @Override
    void onDialogCreated(HashMap<String, Object> propertyResourceBundle) {
        JLabel redDivisionLabel = new JLabel("Red divisions");
        redDivisionTextField = FormattedTextFieldUtil.getFormattedTextField(MIN, MAX, TEXT_FIELD_SIZE);
        redDivisionTextField.setColumns(TEXT_FIELD_SIZE);


        addComponent(0, 0, redDivisionLabel);
        addComponent(1, 0, redDivisionTextField);

        JLabel greenDivisionLabel = new JLabel("Green divisions");
        greenDivisionTextField = FormattedTextFieldUtil.getFormattedTextField(MIN, MAX, TEXT_FIELD_SIZE);
        greenDivisionTextField.setColumns(TEXT_FIELD_SIZE);

        addComponent(0, 1, greenDivisionLabel);
        addComponent(1, 1, greenDivisionTextField);

        JLabel blueDivisionLabel = new JLabel("Blue divisions");
        blueDivisionTextField = FormattedTextFieldUtil.getFormattedTextField(MIN, MAX, TEXT_FIELD_SIZE);
        blueDivisionTextField.setColumns(TEXT_FIELD_SIZE);

        addComponent(0, 2, blueDivisionLabel);
        addComponent(1, 2, blueDivisionTextField);

        greenDivisionTextField.setValue(MIN);
        blueDivisionTextField.setValue(MIN);
        redDivisionTextField.setValue(MIN);
    }

    @Override
    int getRowForOkAndCancelButtons() {
        return 3;
    }
}
