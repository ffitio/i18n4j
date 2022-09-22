package io.ffit.carbon.i18n4j.core.formatter;

import java.util.Locale;

/**
 * Abstract implementation of the {@link MessageFormatter} interface
 *
 * @author Lay
 */
public abstract class AbstractMessageFormatter implements MessageFormatter {

    protected String code;
    protected Locale locale;

    public AbstractMessageFormatter(String code, Locale locale) {
        this.code = code;
        this.locale = locale;
    }
}
