package io.ffit.carbon.i18n4j.springboot.converter;

import io.ffit.carbon.i18n4j.core.formatter.SourceNameFormatter;
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * Source Name Formatter Class Property Converter
 *
 * @author Lay
 */
@Component
@ConfigurationPropertiesBinding
public class SourceNameFormatterClassPropertyConverter implements Converter<String, Class<SourceNameFormatter>> {

    @SuppressWarnings("unchecked")
    @Override
    public Class<SourceNameFormatter> convert(String source) {
        try {
            return (Class<SourceNameFormatter>) Class.forName(source);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("cannot found Class of MessageFormatter", e);
        }
    }
}
