package io.ffit.carbon.i18n4j.core.formatter;

/**
 * Message Formatter
 *
 * @author Lay
 * @date 2022/9/6
 */
public interface MessageFormatter {
    /**
     * format message
     * @param args      arguments
     * @return  formatted message
     */
    String format(Object[] args);
}
