package io.ffit.carbon.i18n4j.springboot.converter;

import io.ffit.carbon.i18n4j.core.formatter.MessageFormatter;
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * Message Formatter Class Property Converter
 *
 * @author Lay
 */
@Component
@ConfigurationPropertiesBinding
public class MessageFormatterClassPropertyConverter implements Converter<String, Class<MessageFormatter>> {

    @SuppressWarnings("unchecked")
    @Override
    public Class<MessageFormatter> convert(String source) {
        try {
            return (Class<MessageFormatter>) Class.forName(source);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("cannot found Class of MessageFormatter", e);
        }
    }
}
