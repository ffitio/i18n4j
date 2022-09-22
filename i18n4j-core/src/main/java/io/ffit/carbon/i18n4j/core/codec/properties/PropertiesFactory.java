package io.ffit.carbon.i18n4j.core.codec.properties;

import io.ffit.carbon.i18n4j.util.StringUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Properties;

/**
 * Properties Factory
 *
 * @author Lay
 * @date 2022/9/7
 */
public class PropertiesFactory {

    /**
     * load properties from string
     * @param data string data
     * @return  properties
     */
    public static Properties load(String data) {
        if (StringUtils.isBlank(data)) {
            return null;
        }

        Properties props = new Properties();
        try {
            props.load(new StringReader(data));
        } catch (IOException ignored) {}

        return props.isEmpty() ? null : props;
    }

    /**
     * dump properties to string
     * @param props properties
     * @return string
     */
    public static String dump(Properties props) {
        if (props == null || props.isEmpty()) {
            return "";
        }

        StringWriter writer = new StringWriter();
        props.list(new PrintWriter(writer));
        return writer.getBuffer().toString();
    }
}
