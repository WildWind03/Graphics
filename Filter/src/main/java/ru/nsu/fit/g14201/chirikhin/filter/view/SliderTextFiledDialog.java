package ru.nsu.fit.g14201.chirikhin.filter.view;

import ru.nsu.fit.g14201.chirikhin.filter.util.FormattedTextFieldUtil;

import javax.swing.*;

public class SliderTextFiledDialog extends MyDialog {
    private final int threshold = 80;
    private final int MIN = 2;
    private final int MAX = 255;
    private final int TEXT_FIELD_SIZE = 8;

    private JSlider slider;
    private JFormattedTextField jTextField;

    public SliderTextFiledDialog(JFrame jFrame, String title) {
        super(jFrame, title);
    }

    @Override
    void onDialogCreated() {
        slider = new JSlider(MIN, MAX);
        slider.setValue(threshold);

        jTextField = FormattedTextFieldUtil.getFormattedTextField(MIN, MAX, TEXT_FIELD_SIZE);

        addComponent(0, 0, slider);
        addComponent(0, 1, jTextField);

    }

    @Override
    int getRowForOkAndCancelButtons() {
        return 1;
    }
}
