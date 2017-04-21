package chirikhin.swing.util;

public class DoubleUtil {
    private DoubleUtil() {

    }

    public static String getDouble(String str, int countOfSymbolsInDecimalPart) {
            int posOfWholePart = str.indexOf('.');
            return str.substring(0, Math.min(posOfWholePart + countOfSymbolsInDecimalPart, str.length()));
    }

    public static String getDouble(double d, int countOfSymbolsInDecimalPart) {
        return getDouble(Double.toString(d), countOfSymbolsInDecimalPart + 1);
    }
}
