package com.chirikhin.swing.util;

import javax.swing.*;
import java.awt.event.ActionListener;

public class ListenerUtil {
    private ListenerUtil() {

    }

    public static void setListener(AbstractButton abstractButton, Runnable runnable) {
        for (ActionListener actionListener : abstractButton.getActionListeners()) {
            abstractButton.removeActionListener(actionListener);
        }

        abstractButton.addActionListener(e -> runnable.run());
    }

    public static void setListener(AbstractButton abstractButton, AbstractButton abstractButton1, Runnable runnable) {
        setListener(abstractButton, runnable);
        setListener(abstractButton1, runnable);
    }
}

