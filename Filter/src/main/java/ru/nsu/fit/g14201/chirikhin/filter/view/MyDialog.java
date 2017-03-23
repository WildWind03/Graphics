package ru.nsu.fit.g14201.chirikhin.filter.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public abstract class MyDialog extends JDialog {
    private static final String OK = "OK";
    private static final String CANCEL = "Cancel";
    private boolean isCancelled = true;

    public MyDialog(JFrame jFrame, String title) {
        super(jFrame, title, true);

        GridBagLayout gridBagLayout = new GridBagLayout();
        setLayout(gridBagLayout);

        onDialogCreated();
        addOkAndCancelButtons();
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

    private void addOkAndCancelButtons() {
        JButton okButton = new JButton(OK);
        JButton cancelButton = new JButton(CANCEL);

        addComponent(0, getRowForOkAndCancelButtons(), okButton);
        addComponent(1, getRowForOkAndCancelButtons(), cancelButton);

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

    abstract void onDialogCreated();

    abstract int getRowForOkAndCancelButtons();
}
