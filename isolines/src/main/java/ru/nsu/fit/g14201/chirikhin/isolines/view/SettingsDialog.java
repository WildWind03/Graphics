package ru.nsu.fit.g14201.chirikhin.isolines.view;

import com.chirikhin.swing.dialog.FormattedTextFieldListDialog;

import javax.swing.*;
import java.util.HashMap;

public class SettingsDialog extends FormattedTextFieldListDialog {

    private static final int MAX_COLOR = 255;
    private static final int MIN_COLOR = 0;
    private static final int COUNT_OF_COMPONENTS = 5;
    private static final String GRID_WIDTH_DIVISIONS = "Grid width divisions";
    private static final String GRID_HEIGHT_DIVISIONS = "Grid height divisions";
    private static final String RED_COLOR_VALUE = "Red color value";
    private static final String GREEN_COLOR_VALUE = "Green color value";
    private static final String BLUE_COLOR_VALUE = "Blue color value";
    private static final int MIN_GRID_VALUE = 2;
    private static final int MAX_GRID_VALUE = 40;

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
    }

    private static HashMap<String, Object> getParams(SettingParams settingParams) {
        HashMap<String, Object> params = new HashMap<>();
        params.put(GRID_WIDTH_DIVISIONS, settingParams.getGridWidthDivs());
        params.put(GRID_HEIGHT_DIVISIONS, settingParams.getGridHeightDivs());
        params.put(RED_COLOR_VALUE, settingParams.getRedColor());
        params.put(GREEN_COLOR_VALUE, settingParams.getGreenColor());
        params.put(BLUE_COLOR_VALUE, settingParams.getBlueColor());

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

}
