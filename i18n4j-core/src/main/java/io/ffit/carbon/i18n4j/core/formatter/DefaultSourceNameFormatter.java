package io.ffit.carbon.i18n4j.core.formatter;

import java.util.Locale;

/**
 * Default Source Name Formatter
 *
 * @author Lay
 */
public class DefaultSourceNameFormatter extends AbstractSourceNameFormatter {

    public DefaultSourceNameFormatter(String format) {
        super(format);
    }

    @Override
    public String format(String tag, Locale locale) {
        StringBuilder sb = new StringBuilder();
        sb.append(tag);

        if (locale != null) {
            String language = locale.getLanguage();
            String country = locale.getCountry();
            String variant = locale.getVariant();

            if (language.length() > 0) {
                sb.append("_");
                sb.append(language);
            }

            if (country.length() > 0) {
                sb.append("_");
                sb.append(country);
            }

            if (variant.length() > 0 && (language.length() > 0 || country.length() > 0)) {
                sb.append("_");
                sb.append(variant);
            }
        }

        sb.append(".");
        sb.append(format);
        return sb.toString();
    }
}
