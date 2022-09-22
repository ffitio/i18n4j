package io.ffit.carbon.i18n4j.test.formatter;

import io.ffit.carbon.i18n4j.core.formatter.AbstractSourceNameFormatter;

import java.util.Locale;

/**
 * Custom Source Name Formatter
 *
 * @author Lay
 */
public class CustomSourceNameFormatter extends AbstractSourceNameFormatter {

    public CustomSourceNameFormatter(String format) {
        super(format);
    }

    @Override
    public String format(String tag, Locale locale) {
        return String.format("%s.%s.%s", tag, locale.toLanguageTag(), format);
    }
}
