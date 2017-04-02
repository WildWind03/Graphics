package ru.nsu.fit.g14201.chirikhin.filter.view;

import ru.nsu.fit.g14201.chirikhin.util.FormattedTextFieldUtil;

import javax.swing.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.HashMap;

public class GammaDialog extends MyDialog {
    private static final int SLIDER_MIN = 1;
    private static final int SLIDER_MAX = 50;

    private static final int SLIDER_START = 25;

    private static final double TEXT_FIELD_MIN = 0.1;
    private static final double TEXT_FIELD_MAX = 5;

    private static final int TEXT_FIELD_SIZE = 8;

    private JSlider slider;
    private JFormattedTextField jTextField;

    public GammaDialog(JFrame jFrame, String title) {
        super(jFrame, title);
    }

    @Override
    public void onDialogCreated(HashMap<String, Object> args) {
        slider = new JSlider(SLIDER_MIN, SLIDER_MAX);
        slider.setValue(SLIDER_START);

        jTextField = FormattedTextFieldUtil.getFormattedDoubleTextField(TEXT_FIELD_MIN, TEXT_FIELD_MAX, TEXT_FIELD_SIZE);
        jTextField.setValue((double) (SLIDER_START) / 10d);

        slider.addChangeListener(e -> {
            jTextField.setValue((double) (slider.getValue()) / 10d);
        });

        jTextField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                super.focusLost(e);
                slider.setValue((int) ((double) jTextField.getValue() * 10));
            }
        });

        addComponent(0, 0, slider);
        addComponent(1, 0, jTextField);
    }

    @Override
    public int getRowForOkAndCancelButtons() {
        return 1;
    }

    public double getValue() {
        return ((Number) jTextField.getValue()).doubleValue();
    }
}
