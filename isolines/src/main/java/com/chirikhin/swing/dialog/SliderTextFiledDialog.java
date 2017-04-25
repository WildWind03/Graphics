package com.chirikhin.swing.dialog;

import com.chirikhin.swing.util.FormattedTextFieldUtil;

import javax.swing.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.HashMap;

public class SliderTextFiledDialog extends MyDialog {
    private static final int ROW_FOR_OK_AND_CANCEL_BUTTONS = 1;
    private int startValue;
    private int min;
    private int max;
    private static final int TEXT_FIELD_SIZE = 8;

    private static final String MIN_TAG = "MIN_TAG";
    private static final String MAX_TAG = "MAX_TAG";
    private static final String START_VALUE_TAG = "START_VALUE";

    private JSlider slider;
    private JFormattedTextField jTextField;

    public SliderTextFiledDialog(JFrame jFrame, String title, int min, int max, int startValue) {
        super(jFrame, title, getMap(min, max, startValue), ROW_FOR_OK_AND_CANCEL_BUTTONS);

        this.min = min;
        this.max = max;
        this.startValue = startValue;
    }

    private static HashMap<String,Object> getMap(int min, int max, int startValue) {
        HashMap<String, Object> args = new HashMap<>();
        args.put(MAX_TAG, max);
        args.put(MIN_TAG, min);
        args.put(START_VALUE_TAG, startValue);

        return args;
    }

    @Override
    public void onDialogCreated(HashMap<String, Object> args) {
        if (null != args) {
            this.min = (int) args.get(MIN_TAG);
            this.max = (int) args.get(MAX_TAG);
            this.startValue = (int) args.get(START_VALUE_TAG);
        }

        slider = new JSlider(min, max);
        slider.setValue(startValue);

        jTextField = FormattedTextFieldUtil.getFormattedTextField(min, max, TEXT_FIELD_SIZE);
        jTextField.setValue(startValue);

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

    public int getValue() {
        return ((Number) jTextField.getValue()).intValue();
    }
}
