package io.ffit.carbon.i18n4j.core;

import io.ffit.carbon.i18n4j.config.I18nConfig;
import io.ffit.carbon.i18n4j.core.entity.LocaleProperties;
import io.ffit.carbon.i18n4j.core.provider.pipe.ProviderPipe;
import io.ffit.carbon.i18n4j.core.provider.pipe.ProviderPipeImpl;
import io.ffit.carbon.i18n4j.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Locale;
/**
 * Abstract implementation of {@link I18nSource}
 *
 * @author Lay
 * @date 2022/9/13
 */
public class DefaultI18nSource implements I18nSource {

    private static final Logger logger = LoggerFactory.getLogger(I18nSource.class);

    protected final I18nConfig config;

    protected final ProviderPipe pipe;

    public DefaultI18nSource(I18nConfig config) {
        this.config = config;
        this.pipe = new ProviderPipeImpl(config);
    }

    @Override
    public ProviderPipe pipe() {
        return pipe;
    }

    @Override
    public String getMessage(String code, Object[] args, String defaultMessage, Locale locale) {
        if (code == null) {
            throw new RuntimeException("code cannot be null");
        }

        if (locale == null) {
            throw new RuntimeException("locale cannot be null");
        }

        // get message
        String message = getMessageInternal(code, args, locale);
        if (message == null) {
            // 1 - fallback to Locale without country
            if (StringUtils.isNotEmpty(locale.getCountry()) && config.isFallbackLanguageOnly()) {
                message = getMessageInternal(code, args, Locale.forLanguageTag(locale.getLanguage()));
            }

            // 2 - fallback to default message
            if (message == null && defaultMessage != null) {
                // default message
                message = defaultMessage;
            }

            // 3 - fallback to code
            if (message == null && config.isUseCodeAsDefaultMessage()) {
                message = code;
            }
        }

        return message;
    }

    protected String getMessageInternal(String code, Object[] args, Locale locale) {
        LocaleProperties props = pipe.getProps(locale);
        if (props == null) {
            logger.warn("no messages found for the locale: {}", locale.toLanguageTag());
            return null;
        }

        // get message directly
        return props.getProperty(code, args);
    }

    @Override
    public String getMessage(String code, Object[] args, Locale locale) {
        return getMessage(code, args, null, locale);
    }

    @Override
    public String getMessage(String code, String defaultMessage, Locale locale) {
        return getMessage(code, null, defaultMessage, locale);
    }

    @Override
    public String getMessage(String code, Locale locale) {
        return getMessage(code, null, null, locale);
    }

    @Override
    public String getMessage(String code) {
        return getMessage(code, null, null, Locale.getDefault());
    }
}
