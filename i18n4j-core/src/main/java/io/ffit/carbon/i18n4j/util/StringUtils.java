package io.ffit.carbon.i18n4j.util;

import java.util.Objects;
import java.util.StringJoiner;

/**
 * String Utilities
 *
 * @author Lay
 * @date 2022/9/6
 */
public class StringUtils {
    /**
     * if string is empty
     * @param s string value
     * @return is empty
     */
    public static boolean isEmpty(String s) {
        return s == null || s.isEmpty();
    }

    /**
     * if string is not empty
     * @param s string value
     * @return  is not empty
     */
    public static boolean isNotEmpty(String s) {
        return !isEmpty(s);
    }

    /**
     * if string is blank
     * @param s string value
     * @return is blank
     */
    public static boolean isBlank(String s) {
        return isEmpty(s) || s.isBlank();
    }

    /**
     * if string is not blank
     * @param s string value
     * @return is not blank
     */
    public static boolean isNotBlank(String s) {
        return !isBlank(s);
    }

    public static String join(CharSequence delimiter, CharSequence... elements) {
        Objects.requireNonNull(delimiter);
        Objects.requireNonNull(elements);
        // Number of elements not likely worth Arrays.stream overhead.
        StringJoiner joiner = new StringJoiner(delimiter);
        for (CharSequence cs: elements) {
            joiner.add("\"" + cs + "\"");
        }
        return joiner.toString();
    }
}
