package com.chirikhin.swing.dialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;

public abstract class MyDialog extends JDialog {
    private static final String OK = "OK";
    private static final String CANCEL = "Cancel";
    private boolean isCancelled = true;

    public MyDialog(JFrame jFrame, String title, int rowForOkAndCancelButtons) {
        this(jFrame, title, null, rowForOkAndCancelButtons);
    }

    public MyDialog(JFrame jFrame, String title, HashMap<String, Object> args, int rowForOkAndCancelButtons) {
        super(jFrame, title, true);

        GridBagLayout gridBagLayout = new GridBagLayout();
        setLayout(gridBagLayout);

        onDialogCreated(args);
        addOkAndCancelButtons(rowForOkAndCancelButtons);
        pack();
    }

    protected void addComponent(int row, int column, JComponent jComponent) {
        GridBagConstraints constraints = new GridBagConstraints();

        Insets insets = new Insets(5, 10, 5, 10);

        constraints.gridx = row;
        constraints.gridy = column;
        constraints.insets = insets;

        add(jComponent, constraints);
    }

    public void apparate() {
        setVisible(true);
    }

    private void addOkAndCancelButtons(int row) {
        JButton okButton = new JButton(OK);
        JButton cancelButton = new JButton(CANCEL);

        addComponent(0, row, okButton);
        addComponent(1, row, cancelButton);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                isCancelled = true;
                setVisible(false);
            }
        });

        cancelButton.addActionListener(e -> {
            isCancelled = true;
            setVisible(false);
        });

        okButton.addActionListener(e -> {
            isCancelled = false;
            setVisible(false);
        });
    }

    public boolean isCancelled() {
        return isCancelled;
    }

    protected abstract void onDialogCreated(HashMap<String, Object> propertyResourceBundle);
}
