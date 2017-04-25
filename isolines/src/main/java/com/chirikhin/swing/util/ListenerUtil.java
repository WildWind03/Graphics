package com.chirikhin.swing.util;

import ru.nsu.fit.g14201.chirikhin.isolines.view.BooleanRunnable;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import java.awt.event.ActionListener;

public class ListenerUtil {
    private ListenerUtil() {

    }

    public static void setOnChangeListener(AbstractButton toggleButton, BooleanRunnable booleanRunnable) {
        for (ChangeListener changeListener : toggleButton.getChangeListeners()) {
            toggleButton.removeChangeListener(changeListener);
        }

        toggleButton.addChangeListener(e -> booleanRunnable.run(toggleButton.isSelected()));
    }

    public static void setOnChangeListeners(AbstractButton jToggleButton, AbstractButton jCheckBoxMenuItem, BooleanRunnable booleanRunnable) {
        setOnChangeListener(jToggleButton, booleanRunnable);
        setOnChangeListener(jCheckBoxMenuItem, booleanRunnable);
    }

    public static void setOnActionListeners(AbstractButton abstractButton, Runnable runnable) {
        for (ActionListener actionListener : abstractButton.getActionListeners()) {
            abstractButton.removeActionListener(actionListener);
        }

        abstractButton.addActionListener(e -> runnable.run());
    }

    public static void setOnActionListeners(AbstractButton abstractButton, AbstractButton abstractButton1, Runnable runnable) {
        setOnActionListeners(abstractButton, runnable);
        setOnActionListeners(abstractButton1, runnable);
    }
}

