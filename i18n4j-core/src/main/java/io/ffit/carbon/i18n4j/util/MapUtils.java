package io.ffit.carbon.i18n4j.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;

/**
 * Map Utilities
 *
 * @author Lay
 * @date 2022/9/7
 */
public class MapUtils {

    public static boolean isEmpty(Map<?, ?> map) {
        return map == null || map.isEmpty();
    }

    public static boolean isNotEmpty(Map<?, ?> map) {
        return !isEmpty(map);
    }

    /**
     * build tree map from properties
     * @param props properties
     * @return tree map
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Object> tree(Properties props) {
        Map<String, Object> map = new HashMap<>();
        props.forEach((k, v) -> {
            String key = k.toString();
            String[] keys = key.split("\\.");

            Map<String, Object> node = map;
            for (int i = 0; i < keys.length; i++) {
                if (i == keys.length - 1) {
                    node.put(keys[i], v);
                    break;
                }
                node = (Map<String, Object>) node.merge(keys[i], new HashMap<>(), (o, o2) -> o);
            }
        });
        return map;
    }

    /**
     * convert objects to flattened map
     * @param iterable  objects
     * @return flattened map
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Object> flat(Iterable<Object> iterable) {
        if (iterable == null || !iterable.iterator().hasNext()) {
            return null;
        }

        Map<String, Object> map = new HashMap<>();
        iterable.forEach(o -> {
            if (o instanceof Map) {
                Map<String, Object> result = new HashMap<>();
                mapRecursion(result, (Map<String, Object>) o, null);
                if (!result.isEmpty()) {
                    map.putAll(result);
                }
            }
        });

        return map.isEmpty() ? null : map;
    }

    @SuppressWarnings("unchecked")
    private static void mapRecursion(Map<String, Object> result, Map<String, Object> source, String path) {
        source.forEach((k, v) -> {
            if (StringUtils.isNotBlank(path)) {
                k = path + "." + k;
            }
            if (v instanceof Map) {
                Map<String, Object> map = (Map<String, Object>) v;
                mapRecursion(result, map, k);
            } else {
                result.put(k, Optional.ofNullable(v).map(Object::toString).orElse(""));
            }
        });
    }
}
