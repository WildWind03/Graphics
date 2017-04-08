package com.chirikhin.swing.dialog;

import com.chirikhin.swing.util.FormattedTextFieldUtil;
import javafx.util.Pair;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;

public class FormattedTextFieldListDialog extends MyDialog {
    private static final int DEFAULT_TEXT_FIELD_LENGTH = 8;
    private int currentRow = 0;
    private ArrayList<Pair<String, JFormattedTextField>> textFields;

    public FormattedTextFieldListDialog(JFrame jFrame, String title, int countOfComponents) {
        super(jFrame, title, countOfComponents);
    }

    public FormattedTextFieldListDialog(JFrame jFrame, String title, HashMap<String, Object> args, int rowForOkAndCancelButtons) {
        super(jFrame, title, args, rowForOkAndCancelButtons);
    }

    @Override
    protected void onDialogCreated(HashMap<String, Object> propertyResourceBundle) {
        textFields = new ArrayList<>();
    }

    public void addTextField(String label, int minValue, int maxValue, String TAG, Number defaultValue) {
        addComponent(0, currentRow, new JLabel(label));

        JFormattedTextField jFormattedTextField = FormattedTextFieldUtil.getFormattedTextField(minValue, maxValue, DEFAULT_TEXT_FIELD_LENGTH);
        textFields.add(new Pair<>(TAG, jFormattedTextField));
        jFormattedTextField.setValue(defaultValue);
        addComponent(1, currentRow, jFormattedTextField);
        currentRow++;
    }

    public void addTextField(String label, int minValue, int maxValue, String TAG) {
        addTextField(label, minValue, maxValue, TAG, 0);
    }

    public HashMap<String, String> getResult() {
        HashMap<String, String> values = new HashMap<>();
        for (Pair<String, JFormattedTextField> pair : textFields) {
            values.put(pair.getKey(), pair.getValue().getValue().toString());
        }

        return values;
    }
}
