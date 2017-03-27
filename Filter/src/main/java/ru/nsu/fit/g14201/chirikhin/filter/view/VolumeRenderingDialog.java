package ru.nsu.fit.g14201.chirikhin.filter.view;

import ru.nsu.fit.g14201.chirikhin.util.FormattedTextFieldUtil;

import javax.swing.*;
import java.util.HashMap;

public class VolumeRenderingDialog extends MyDialog {

    private static final int MIN_TEXT_FIELD_VALUE = 10;
    private static final int MAX_TEXT_FIELD_VALUE = 350;
    private static final int TEXT_FILED_SIZE = 8;

    private JFormattedTextField nxEdit;
    private JFormattedTextField nyEdit;
    private JFormattedTextField nzEdit;

    public VolumeRenderingDialog(JFrame jFrame, String title) {
        super(jFrame, title);
    }

    @Override
    void onDialogCreated(HashMap<String, Object> propertyResourceBundle) {
        JLabel nxLabel = new JLabel("nx");
        JLabel nyLabel = new JLabel("ny");
        JLabel nzLabel = new JLabel("nz");

        nxEdit = FormattedTextFieldUtil.getFormattedTextField(MIN_TEXT_FIELD_VALUE, MAX_TEXT_FIELD_VALUE, TEXT_FILED_SIZE);
        nyEdit = FormattedTextFieldUtil.getFormattedTextField(MIN_TEXT_FIELD_VALUE, MAX_TEXT_FIELD_VALUE, TEXT_FILED_SIZE);
        nzEdit = FormattedTextFieldUtil.getFormattedTextField(MIN_TEXT_FIELD_VALUE, MAX_TEXT_FIELD_VALUE, TEXT_FILED_SIZE);

        addComponent(0, 0, nxLabel);
        addComponent(1, 0, nxEdit);
        addComponent(0, 1, nyLabel);
        addComponent(1, 1, nyEdit);
        addComponent(0, 2, nzLabel);
        addComponent(1, 2, nzEdit);
    }

    @Override
    int getRowForOkAndCancelButtons() {
        return 3;
    }

    public int getNx() {
        return ((Number) nxEdit.getValue()).intValue();
    }

    public int getNy() {
        return ((Number) nyEdit.getValue()).intValue();
    }

    public int getNz() {
        return ((Number) nzEdit.getValue()).intValue();
    }
}
