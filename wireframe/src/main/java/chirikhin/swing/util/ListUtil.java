package chirikhin.swing.util;

import java.util.ArrayList;
import java.util.Collections;

public class ListUtil {
    private ListUtil() {

    }

    public static <T> ArrayList<T> asList(T... t) {
        ArrayList<T> arrayList = new ArrayList<T>();
        Collections.addAll(arrayList, t);
        return arrayList;
    }
}
