package io.ffit.carbon.i18n4j.core.formatter;

import java.util.Locale;

/**
 * Source Name Formatter
 *
 * @author Lay
 */
public interface SourceNameFormatter {
    /**
     * format source name
     * @param tag       source tag
     * @param locale    locale
     * @return  formatted source name
     */
    String format(String tag, Locale locale);
}
