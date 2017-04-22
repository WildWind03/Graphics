package ru.fit.g14201.chirikhin.wireframe.view;

import chirikhin.swing.dialog.MyDialog;
import ru.fit.g14201.chirikhin.wireframe.model.Model;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

public class SettingsDialog extends MyDialog {
    private static final String MODEL_KEY = "MODEL_KEY";
    private static final int WIDTH = 600;
    private static final int HEIGHT = 300;
    private static final int SHAPES_LIST_WIDTH = 200;
    private static final int SHAPES_LIST_HEIGHT = 340;
    private SplineGraphic splineGraphic;

    private JSpinner nSpinner;
    private JSpinner mSpinner;
    private JSpinner kSpinner;
    private JSpinner numSpinner;
    private JSpinner rColorSpinner;
    private JSpinner gColorSpinner;
    private JSpinner bColorSpinner;
    private JSpinner aSpinner;
    private JSpinner bSpinner;
    private JSpinner cSpinner;
    private JSpinner dSpinner;
    private JSpinner znSpinner;
    private JSpinner zfSpinner;
    private JSpinner swSpinner;
    private JSpinner shSpinner;


    public SettingsDialog(JFrame jFrame, String title, int rowForOkAndCancelButtons, Model model) {
        super(jFrame, title, getArgs(model), rowForOkAndCancelButtons);
    }

    @Override
    protected void onDialogCreated(HashMap<String, Object> propertyResourceBundle) {
        nSpinner = new JSpinner(new SpinnerNumberModel(10, 1, 100, 1));
        mSpinner = new JSpinner(new SpinnerNumberModel(10, 1, 100, 1));
        kSpinner = new JSpinner(new SpinnerNumberModel(10, 1, 100, 1));
        numSpinner = new JSpinner(new SpinnerNumberModel(10, 1, 100, 1));

        rColorSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 255, 1));
        gColorSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 255, 1));
        bColorSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 255, 1));

        aSpinner = new JSpinner(new SpinnerNumberModel(10, 1, 100, 1));
        bSpinner = new JSpinner(new SpinnerNumberModel(10, 1, 100, 1));
        cSpinner = new JSpinner(new SpinnerNumberModel(10, 1, 100, 1));
        dSpinner = new JSpinner(new SpinnerNumberModel(10, 1, 100, 1));
        znSpinner = new JSpinner(new SpinnerNumberModel(10, 1, 100, 1));
        zfSpinner = new JSpinner(new SpinnerNumberModel(10, 1, 100, 1));
        shSpinner = new JSpinner(new SpinnerNumberModel(10, 1, 100, 1));
        swSpinner = new JSpinner(new SpinnerNumberModel(10, 1, 100, 1));

        splineGraphic = new SplineGraphic(WIDTH, HEIGHT);
        setResizable(false);
        addComponent(0, 0, 80, 10, splineGraphic);
        addComponent(0, 81, 50, 5, new JLabel("List of shapes"));

        addNewSpinnerLabel(0, 0, "n", nSpinner);
        addNewSpinnerLabel(0, 1, "m", mSpinner);
        addNewSpinnerLabel(0, 2, "k", kSpinner);
        addNewSpinnerLabel(0, 3, "№", numSpinner);
        addNewSpinnerLabel(0, 4, "R", rColorSpinner);

        addNewSpinnerLabel(1, 0, "a", aSpinner);
        addNewSpinnerLabel(1, 1, "b", bSpinner);
        addNewSpinnerLabel(1, 2, "c", cSpinner);
        addNewSpinnerLabel(1, 3, "d", dSpinner);
        addNewSpinnerLabel(1, 4, "g", gColorSpinner);

        addNewSpinnerLabel(2, 0, "zn", znSpinner);
        addNewSpinnerLabel(2, 1, "zf", zfSpinner);
        addNewSpinnerLabel(2, 2, "sw", swSpinner);
        addNewSpinnerLabel(2, 3, "sh", shSpinner);
        addNewSpinnerLabel(2, 4, "b", bColorSpinner);

        Model model = (Model) propertyResourceBundle.get(MODEL_KEY);

        String[] shapes = new String[0];
        if (null != model) {
            int countOfShapes = model.getShapes().size();
            shapes = new String[countOfShapes];
            for (int i = 0; i < countOfShapes; ++i) {
                shapes[i] = "Shape " + i;
            }
        }

        JList<String> shapesList = new JList<>(shapes);
        JScrollPane shapesListScrollPane = new JScrollPane(shapesList);
        shapesListScrollPane.setPreferredSize(new Dimension(SHAPES_LIST_WIDTH, SHAPES_LIST_HEIGHT));
        addComponent(5, 81, 50, 90, shapesListScrollPane);
    }

    private static HashMap<String, Object> getArgs(Model model) {
        HashMap<String, Object> args = new HashMap<>();
        args.put(MODEL_KEY, model);
        return args;
    }

    private void addNewSpinnerLabel(int row, int column, String label, JSpinner jSpinner) {
        addComponent(80 + row * 5, column * 2, 1, 5, new JLabel(label));
        addComponent(80 + row * 5, column * 2 + 1, 1, 5, jSpinner);
    }
}
