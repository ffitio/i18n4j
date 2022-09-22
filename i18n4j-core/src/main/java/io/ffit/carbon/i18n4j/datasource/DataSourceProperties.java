package io.ffit.carbon.i18n4j.datasource;

import java.util.Optional;
import java.util.Properties;

/**
 * Data Source Properties
 *
 * @author Lay
 * @date 2022/9/19
 */
public class DataSourceProperties extends Properties {
    public String getValue(Object key) {
        return get(key).toString();
    }

    public String getValue(Object key, String defaultValue) {
        return Optional.ofNullable(get(key)).map(Object::toString).orElse(defaultValue);
    }

    public Object getValue(Object key, Object defaultValue) {
        return Optional.ofNullable(get(key)).orElse(defaultValue);
    }
}
