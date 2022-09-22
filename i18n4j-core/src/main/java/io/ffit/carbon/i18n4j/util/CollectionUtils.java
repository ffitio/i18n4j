package io.ffit.carbon.i18n4j.util;

import java.util.Collection;
import java.util.Map;

/**
 * Collection Utilities
 *
 * @author Lay
 * @date 2022/9/6
 */
public class CollectionUtils {

    /**
     * if collection is empty
     * @param c collection
     * @return  is empty
     */
    public static boolean isEmpty(Collection<?> c) {
        return c == null || c.isEmpty();
    }

    /**
     * if collection is not empty
     * @param c collection
     * @return  is not empty
     */
    public static boolean isNotEmpty(Collection<?> c) {
        return !isEmpty(c);
    }

    /**
     * if array is empty
     * @param array array
     * @return  is empty
     * @param <T> type of array
     */
    public static <T> boolean isEmpty(T[] array) {
        return array == null || array.length == 0;
    }

    /**
     * if array is not empty
     * @param array array
     * @return  is not empty
     * @param <T>   type of array
     */
    public static <T> boolean isNotEmpty(T[] array) {
        return !isEmpty(array);
    }

    /**
     * if map is empty
     * @param map   map
     * @return  is empty
     */
    public static boolean isEmpty(Map<?, ?> map) {
        return map == null || map.isEmpty();
    }

    /**
     * if map is not empty
     * @param map   map
     * @return  is not empty
     */
    public static boolean isNotEmpty(Map<?, ?> map) {
        return !isEmpty(map);
    }
}
