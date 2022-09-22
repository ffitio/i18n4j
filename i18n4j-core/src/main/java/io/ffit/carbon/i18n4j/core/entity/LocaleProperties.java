package io.ffit.carbon.i18n4j.core.entity;

import io.ffit.carbon.i18n4j.core.formatter.MessageFormatter;
import io.ffit.carbon.i18n4j.util.CollectionUtils;
import io.ffit.carbon.i18n4j.core.formatter.DefaultMessageFormatter;

import java.util.Locale;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Properties Holder
 *
 * @author Lay
 * @date 2022/9/6
 */
public class LocaleProperties extends Properties {

    private final Locale locale;

    private final Class<? extends MessageFormatter> messageFormatterClass;

    private final ConcurrentMap<String, MessageFormatter> cachedMessageFormatter = new ConcurrentHashMap<>();

    public LocaleProperties(Properties props, Locale locale, Class<? extends  MessageFormatter> messageFormatterClass) {
        this.putAll(props);
        this.locale = locale;
        this.messageFormatterClass = messageFormatterClass;
    }

    public String getProperty(String code, Object[] args) {
        if (CollectionUtils.isNotEmpty(args)) {
            MessageFormatter formatter = getMessageFormatter(code);
            if (formatter != null) {
                return formatter.format(args);
            }
        }
        return super.getProperty(code);
    }

    public LocaleProperties copy() {
        return new LocaleProperties(this, locale, messageFormatterClass);
    }

    private MessageFormatter getMessageFormatter(String code) {
        MessageFormatter result = cachedMessageFormatter.get(code);
        if (result != null) {
            return result;
        }

        // build cache
        String msg = this.getProperty(code);
        if (msg != null) {
            result = createMessageFormatter(msg, locale);
            return this.cachedMessageFormatter.putIfAbsent(code, result);
        }

        return null;
    }

    private MessageFormatter createMessageFormatter(String msg, Locale locale) {
        if (this.messageFormatterClass == null) {
            return createDefaultMessageFormatter(msg, locale);
        }

        try {
            return this.messageFormatterClass.getDeclaredConstructor(String.class, Locale.class).newInstance(msg, locale);
        } catch (Exception e) {
            throw new RuntimeException("Message Formatter created failed:", e);
        }
    }

    private MessageFormatter createDefaultMessageFormatter(String msg, Locale locale) {
        return new DefaultMessageFormatter(msg, locale);
    }
}
