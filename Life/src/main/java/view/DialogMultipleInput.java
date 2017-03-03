package view;

import javax.swing.*;
import javax.swing.text.NumberFormatter;
import java.text.NumberFormat;

public class DialogMultipleInput {

    private static final String WIDTH = "width:";
    private static final String HEIGHT = "height:";

    public static class Result {
        private final int width;
        private final int height;

        public Result(int width, int height) {
            this.width = width;
            this.height = height;
        }

        public int getWidth() {
            return width;
        }

        public int getHeight() {
            return height;
        }
    }

    private static final int TEXT_FIELD_SIZE = 3;
    private static final int DEFAULT_FIELD_WIDTH = 5;
    private static final int DEFAULT_FIELD_HEIGHT = 5;
    private static final int GAP_BETWEEN_FIELDS = 8;

    private static final int MAX_WIDTH_VALUE = 100;
    private static final int MIN_WIDTH_VALUE = 1;

    private DialogMultipleInput() {

    }

    public static Result show(String title) {
        NumberFormat numberFormat = NumberFormat.getIntegerInstance();

        NumberFormatter numberFormatter = new NumberFormatter(numberFormat);
        numberFormatter.setAllowsInvalid(false);
        numberFormatter.setMinimum(MIN_WIDTH_VALUE);
        numberFormatter.setMaximum(MAX_WIDTH_VALUE);

        JFormattedTextField xField = new JFormattedTextField(numberFormatter);
        xField.setColumns(TEXT_FIELD_SIZE);
        xField.setValue(DEFAULT_FIELD_WIDTH);
        JFormattedTextField yField = new JFormattedTextField(numberFormatter);
        yField.setColumns(TEXT_FIELD_SIZE);
        yField.setValue(DEFAULT_FIELD_HEIGHT);

        JPanel myPanel = new JPanel();
        myPanel.add(new JLabel(WIDTH));
        myPanel.add(xField);
        myPanel.add(Box.createHorizontalStrut(GAP_BETWEEN_FIELDS));
        myPanel.add(new JLabel(HEIGHT));
        myPanel.add(yField);

        int result = JOptionPane.showConfirmDialog(null, myPanel,
                title, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (JOptionPane.OK_OPTION == result) {
            int width = Integer.parseInt(xField.getText());
            int height = Integer.parseInt(yField.getText());

            return new Result(width, height);
        } else {
            return null;
        }
    }
}
