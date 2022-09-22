package io.ffit.carbon.i18n4j.core;

import io.ffit.carbon.i18n4j.core.provider.pipe.ProviderPipe;

import java.util.Locale;

/**
 * I18n Source
 *
 * @author Lay
 * @date 2022/9/11
 */
public interface I18nSource {

    /**
     * get provider pipe
     * @return {@link ProviderPipe}
     */
    ProviderPipe pipe();

    /**
     * get message
     * @param code              message code
     * @param args              template arguments
     * @param defaultMessage    default message
     * @param locale            target locale
     * @return  message
     */
    String getMessage(String code, Object[] args, String defaultMessage, Locale locale);

    /**
     * overload of {@link I18nSource#getMessage(String, Object[], String, Locale)}
     * @param code      message code
     * @param args      template arguments
     * @param locale    target locale
     * @return  message
     */
    String getMessage(String code, Object[] args, Locale locale);

    /**
     * overload of {@link I18nSource#getMessage(String, Object[], String, Locale)}
     * @param code              message code
     * @param defaultMessage    template arguments
     * @param locale            target locale
     * @return message
     */
    String getMessage(String code, String defaultMessage, Locale locale);

    /**
     * overload of {@link I18nSource#getMessage(String, Object[], String, Locale)}
     * @param code      message code
     * @param locale    target locale
     * @return  message
     */
    String getMessage(String code, Locale locale);

    /**
     * overload of {@link I18nSource#getMessage(String, Object[], String, Locale)}
     * <p>use {@link Locale#getDefault()} Locale instance</p>
     * @param code  message code
     * @return  message
     */
    String getMessage(String code);
}
