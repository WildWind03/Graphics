package ru.nsu.fit.g14201.chirikhin.isolines.view;

import com.chirikhin.swing.dialog.FormattedTextFieldListDialog;

import javax.swing.*;
import java.util.HashMap;

public class SettingsDialog extends FormattedTextFieldListDialog {

    private static final int MAX_COLOR = 255;
    private static final int MIN_COLOR = 0;
    private static final int COUNT_OF_COMPONENTS = 9;
    private static final String GRID_WIDTH_DIVISIONS = "Grid width divisions";
    private static final String GRID_HEIGHT_DIVISIONS = "Grid height divisions";
    private static final String RED_COLOR_VALUE = "Red color value";
    private static final String GREEN_COLOR_VALUE = "Green color value";
    private static final String BLUE_COLOR_VALUE = "Blue color value";
    private static final int MIN_GRID_VALUE = 2;
    private static final int MAX_GRID_VALUE = 40;
    private static final int MIN_FIELD_VALUE = -200;
    private static final int MAX_FIELD_VALUE = 200;
    private static final String X_0 = "x0";
    private static final String Y_0 = "y0";
    private static final String X_1 = "x1";
    private static final String Y_1 = "y1";

    public SettingsDialog(JFrame jFrame, String title, SettingParams settingParams) {
        super(jFrame, title, getParams(settingParams), COUNT_OF_COMPONENTS);
    }

    @Override
    protected void onDialogCreated(HashMap<String, Object> propertyResourceBundle) {
        super.onDialogCreated(propertyResourceBundle);

        addTextField(GRID_WIDTH_DIVISIONS, MIN_GRID_VALUE, MAX_GRID_VALUE, GRID_WIDTH_DIVISIONS, (Number) propertyResourceBundle.get(GRID_WIDTH_DIVISIONS));
        addTextField(GRID_HEIGHT_DIVISIONS, MIN_GRID_VALUE, MAX_GRID_VALUE, GRID_HEIGHT_DIVISIONS, (Number) propertyResourceBundle.get(GRID_HEIGHT_DIVISIONS));
        addTextField(RED_COLOR_VALUE, MIN_COLOR, MAX_COLOR, RED_COLOR_VALUE, (Number) propertyResourceBundle.get(RED_COLOR_VALUE));
        addTextField(GREEN_COLOR_VALUE, MIN_COLOR, MAX_COLOR, GREEN_COLOR_VALUE, (Number) propertyResourceBundle.get(GREEN_COLOR_VALUE));
        addTextField(BLUE_COLOR_VALUE, MIN_COLOR, MAX_COLOR, BLUE_COLOR_VALUE, (Number) propertyResourceBundle.get(BLUE_COLOR_VALUE));
        addTextField(X_0, MIN_FIELD_VALUE, MAX_FIELD_VALUE, X_0, (Number) propertyResourceBundle.get(X_0));
        addTextField(Y_0, MIN_FIELD_VALUE, MAX_FIELD_VALUE, Y_0, (Number) propertyResourceBundle.get(Y_0));
        addTextField(X_1, MIN_FIELD_VALUE, MAX_FIELD_VALUE, X_1, (Number) propertyResourceBundle.get(X_1));
        addTextField(Y_1, MIN_FIELD_VALUE, MAX_FIELD_VALUE, Y_1, (Number) propertyResourceBundle.get(Y_1));
    }

    private static HashMap<String, Object> getParams(SettingParams settingParams) {
        HashMap<String, Object> params = new HashMap<>();
        params.put(GRID_WIDTH_DIVISIONS, settingParams.getGridWidthDivs());
        params.put(GRID_HEIGHT_DIVISIONS, settingParams.getGridHeightDivs());
        params.put(RED_COLOR_VALUE, settingParams.getRedColor());
        params.put(GREEN_COLOR_VALUE, settingParams.getGreenColor());
        params.put(BLUE_COLOR_VALUE, settingParams.getBlueColor());
        params.put(X_0, settingParams.getX0());
        params.put(Y_0, settingParams.getY0());
        params.put(X_1, settingParams.getX1());
        params.put(Y_1, settingParams.getY1());

        return params;
    }

    public int getGridWidthDivisions() {
        return Integer.parseInt(getResult().get(GRID_WIDTH_DIVISIONS));
    }

    public int getGridHeightDivisions() {
        return Integer.parseInt(getResult().get(GRID_HEIGHT_DIVISIONS));
    }

    public int getRedColor() {
        return Integer.parseInt(getResult().get(RED_COLOR_VALUE));
    }

    public int getGreenColor() {
        return Integer.parseInt(getResult().get(GREEN_COLOR_VALUE));
    }

    public int getBlueColor() {
        return Integer.parseInt(getResult().get(BLUE_COLOR_VALUE));
    }

    public int getStartX() {
        return Integer.parseInt(getResult().get(X_0));
    }

    public int getStartY() {
        return Integer.parseInt(getResult().get(Y_0));
    }

    public int getEndX() {
        return Integer.parseInt(getResult().get(X_1));
    }

    public int getEndY() {
        return Integer.parseInt(getResult().get(Y_1));
    }
}
