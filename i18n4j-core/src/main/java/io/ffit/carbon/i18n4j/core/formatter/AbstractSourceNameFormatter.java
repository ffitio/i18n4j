package io.ffit.carbon.i18n4j.core.formatter;

/**
 * Abstract implementation of the {@link SourceNameFormatter} interface
 *
 * @author Lay
 */
public abstract class AbstractSourceNameFormatter implements SourceNameFormatter {
    protected final String format;

    public AbstractSourceNameFormatter(String format) {
        this.format = format;
    }
}
