package ru.fit.g14201.chirikhin.wireframe.view;

import chirikhin.matrix.Matrix;
import chirikhin.swing.dialog.MyDialog;
import ru.fit.g14201.chirikhin.wireframe.model.Model;
import ru.fit.g14201.chirikhin.wireframe.model.Shape;
import ru.fit.g14201.chirikhin.wireframe.model.ShapeBuilder;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Arc2D;
import java.util.ArrayList;
import java.util.HashMap;

public class SettingsDialog extends MyDialog {
    private static final String MODEL_KEY = "MODEL_KEY";
    private static final int WIDTH = 600;
    private static final int HEIGHT = 300;
    private static final int SHAPES_LIST_WIDTH = 200;
    private static final int SHAPES_LIST_HEIGHT = 340;
    private static final float SCALE_RATE_PLUS = 1.1f;
    private static final float SCALE_RATE_MINUS = 0.9f;
    private static final int DEFAILT_COLUMN_COUNT = 3;


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
        Model model = (propertyResourceBundle.get(MODEL_KEY) != null) ?
                (Model) propertyResourceBundle.get(MODEL_KEY)
                : new Model(10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, new Matrix(new float[][]{{1, 2, 3},
                {4, 5, 6}, {7, 8, 9}}), Color.BLACK);

        splineGraphic = new SplineGraphic(WIDTH, HEIGHT, model.getA(), model.getB());

        nSpinner = new JSpinner(new SpinnerNumberModel(10, 1, 100, 1));
        mSpinner = new JSpinner(new SpinnerNumberModel(10, 1, 100, 1));
        kSpinner = new JSpinner(new SpinnerNumberModel(10, 1, 100, 1));
        numSpinner = new JSpinner(new SpinnerNumberModel(10, 1, 100, 1));

        rColorSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 255, 1));
        gColorSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 255, 1));
        bColorSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 255, 1));

        aSpinner = new JSpinner(new SpinnerNumberModel(0.1f, -0.01f, 1.01f, 0.05f));
        bSpinner = new JSpinner(new SpinnerNumberModel(0.1f, -0.01f, 1.01f, 0.05f));

        aSpinner.addChangeListener(e -> {
            float aFloatValue = getValueOfSpinner(aSpinner);
            float bFloatValue = getValueOfSpinner(bSpinner);

            if (aFloatValue > bFloatValue) {
                aFloatValue = bFloatValue;
                aSpinner.setValue(aFloatValue);
            }

            model.setA(aFloatValue);
            splineGraphic.setNewStartLength(aFloatValue);
        });

        bSpinner.addChangeListener(e -> {
            float aFloatValue = getValueOfSpinner(aSpinner);
            float bFloatValue = getValueOfSpinner(bSpinner);

            if (aFloatValue > bFloatValue) {
                bFloatValue = aFloatValue;
                bSpinner.setValue(bFloatValue);
            }
            model.setB(bFloatValue);
            splineGraphic.setNewEndLength(bFloatValue);
        });

        ((JSpinner.DefaultEditor) aSpinner.getEditor()).getTextField().setColumns(DEFAILT_COLUMN_COUNT);
        ((JSpinner.DefaultEditor) bSpinner.getEditor()).getTextField().setColumns(DEFAILT_COLUMN_COUNT);

        cSpinner = new JSpinner(new SpinnerNumberModel(10, 0, 360, 1));
        dSpinner = new JSpinner(new SpinnerNumberModel(10, 0, 360, 1));

        cSpinner.addChangeListener(e -> {
            int cValue = ((Number) cSpinner.getValue()).intValue();
            int dValue = ((Number) dSpinner.getValue()).intValue();

            if (cValue > dValue) {
                cValue = dValue;
                cSpinner.setValue(cValue);
            }

            model.setC(cValue);
        });

        dSpinner.addChangeListener(e -> {
            int cValue = ((Number) cSpinner.getValue()).intValue();
            int dValue = ((Number) dSpinner.getValue()).intValue();

            if (cValue > dValue) {
                dValue = cValue;
                dSpinner.setValue(dValue);
            }

            model.setD(dValue);
        });


        znSpinner = new JSpinner(new SpinnerNumberModel(10, 1, 100, 1));
        zfSpinner = new JSpinner(new SpinnerNumberModel(10, 1, 100, 1));
        shSpinner = new JSpinner(new SpinnerNumberModel(10, 1, 100, 1));
        swSpinner = new JSpinner(new SpinnerNumberModel(10, 1, 100, 1));

        JLabel listOfShapesLabel = new JLabel("List of shapes");
        listOfShapesLabel.setFont(new Font(listOfShapesLabel.getFont().getName(), Font.PLAIN, 20));
        setResizable(false);
        addComponent(0, 0, 80, 10, splineGraphic);
        addComponent(0, 81, 50, 5, listOfShapesLabel);

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

        ArrayList<String> shapes = new ArrayList<>();
        int countOfShapes = model.getShapes().size();
        for (int i = 0; i < countOfShapes; ++i) {
            shapes.add("Shape " + i);
        }

        nSpinner.setValue(model.getN());
        kSpinner.setValue(model.getK());
        mSpinner.setValue(model.getM());

        rColorSpinner.setValue(model.getBackgroundColor().getRed());
        gColorSpinner.setValue(model.getBackgroundColor().getGreen());
        bColorSpinner.setValue(model.getBackgroundColor().getBlue());

        aSpinner.setValue(model.getA());
        bSpinner.setValue(model.getB());
        cSpinner.setValue(model.getC());
        dSpinner.setValue(model.getD());
        znSpinner.setValue(model.getZn());
        zfSpinner.setValue(model.getZf());
        shSpinner.setValue(model.getSh());
        swSpinner.setValue(model.getSw());

        JList<String> shapesList = new JList<>(shapes.toArray(new String[shapes.size()]));

        shapesList.addListSelectionListener(e -> {
            int selectedModelIndex = shapesList.getSelectedIndex();
            if (selectedModelIndex >= 0) {
                Shape shape = model.getShapes().get(selectedModelIndex);
                splineGraphic.setShape(shape);
            }
        });

        JButton addNewShapeButton = new JButton("Add a new shape to the model");
        addNewShapeButton.addActionListener(e -> {
                if(model.getShapes().add(new ShapeBuilder()
                        .withColor(Color.BLACK)
                        .withCx(0)
                        .withCy(0)
                        .withCz(0)
                        .withRoundMatrix(new Matrix(new float[][] {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}}))
                        .build())) {
                    shapes.add(getShapeName(shapes));
                    shapesList.setListData(shapes.toArray(new String[shapes.size()]));
            }
        });

        addComponent(95, 80, 5, 5, addNewShapeButton);
        JButton zoomPlusButton = new JButton("Zoom +");
        zoomPlusButton.addActionListener((e) -> splineGraphic.scaleField(SCALE_RATE_MINUS));
        addNewComponent(3, 0, zoomPlusButton);

        JButton zoomMinusButton = new JButton("Zoom -");
        zoomMinusButton.addActionListener(e -> splineGraphic.scaleField(SCALE_RATE_PLUS));
        addNewComponent(3, 1, zoomMinusButton);

        JButton autoSizeButton = new JButton("Autosize");
        autoSizeButton.addActionListener(e -> splineGraphic.autosizeField());

        addNewComponent(3, 2, autoSizeButton);

        shapesList.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                if (SwingUtilities.isRightMouseButton(e) && model.getShapes().size() > 0) {
                    shapesList.setSelectedIndex(shapesList.locationToIndex(e.getPoint()));
                    JPopupMenu jPopupMenu = new JPopupMenu();
                    JMenuItem itemRemove = new JMenuItem("Remove");
                    itemRemove.addActionListener(e1 -> {
                        shapes.remove(shapesList.getSelectedValue());
                        model.getShapes().remove(shapesList.locationToIndex(e.getPoint()));
                        shapesList.setListData(shapes.toArray(new String[shapes.size()]));
                        splineGraphic.setShape(null);
                    });

                    jPopupMenu.add(itemRemove);
                    jPopupMenu.show(shapesList, e.getX(), e.getY());
                }
            }
        });
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

    private void addNewComponent(int row, int column, JComponent jComponent) {
        addComponent(80 + row * 5, column * 2, 2, 5, jComponent);
    }

    private float getValueOfSpinner(JSpinner jSpinner) {
        Number value = (Number) jSpinner.getValue();
        return value.floatValue();
    }

    private String getShapeName(ArrayList<String> names) {
        m:
        for (int i = 0; ;++i) {
            for (String name : names) {
                if (name.equals("Shape " + i)) {
                    continue m;
                }
            }

            return "Shape " + i;
        }
    }
}
