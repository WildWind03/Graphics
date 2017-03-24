package ru.nsu.fit.g14201.chirikhin.filter.view;

import ru.nsu.fit.g14201.chirikhin.filter.util.FormattedTextFieldUtil;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

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
    public void onDialogCreated() {
        slider = new JSlider(MIN, MAX);
        slider.setValue(threshold);

        jTextField = FormattedTextFieldUtil.getFormattedTextField(MIN, MAX, TEXT_FIELD_SIZE);
        jTextField.setValue(threshold);

        slider.addChangeListener(e -> {
            jTextField.setValue(slider.getValue());
        });

        jTextField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                super.focusLost(e);
                slider.setValue((int) jTextField.getValue());
            }
        });

        addComponent(0, 0, slider);
        addComponent(1, 0, jTextField);
    }

    @Override
    public int getRowForOkAndCancelButtons() {
        return 1;
    }

    public int getValue() {
        return ((Number) jTextField.getValue()).intValue();
    }
}
