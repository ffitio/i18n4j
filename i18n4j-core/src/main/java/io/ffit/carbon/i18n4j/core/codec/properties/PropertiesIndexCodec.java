package io.ffit.carbon.i18n4j.core.codec.properties;

import io.ffit.carbon.i18n4j.core.codec.FormatConstants;
import io.ffit.carbon.i18n4j.core.codec.IndexCodec;
import io.ffit.carbon.i18n4j.entity.Index;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Properties Index Codec implementation of {@link IndexCodec}
 *
 * @author Lay
 * @date 2022/9/6
 */
public class PropertiesIndexCodec implements IndexCodec {

    @SuppressWarnings("unchecked")
    @Override
    public Index decode(String data) {
        Properties props = PropertiesFactory.load(data);
        if (props != null) {
            Index index = new Index();
            props.forEach((k, v) -> {
                String tag = k.toString();
                List<String> arr = (List<String>) v;
                index.put(tag, arr.stream().map(Locale::forLanguageTag).collect(Collectors.toSet()));
            });
            return index;
        }

        return null;
    }

    @Override
    public String encode(Index obj) {
        if (obj == null || obj.isEmpty()) {
            return "";
        }

        Map<String, List<String>> map = new HashMap<>();
        obj.forEach((tag, locales) -> map.put(tag, locales.stream().map(Locale::toLanguageTag).collect(Collectors.toList())));
        Properties props = new Properties();
        props.putAll(map);

        return PropertiesFactory.dump(props);
    }

    @Override
    public String format() {
        return FormatConstants.PROPERTIES;
    }
}
