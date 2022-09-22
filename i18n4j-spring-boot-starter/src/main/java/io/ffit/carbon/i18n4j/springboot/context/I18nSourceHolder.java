package io.ffit.carbon.i18n4j.springboot.context;

import io.ffit.carbon.i18n4j.core.I18nSource;

/**
 * I18n Source Holder
 *
 * @author Lay
 */
public class I18nSourceHolder {
    /**
     * i18n source instance
     */
    private static I18nSource instance;

    public static void init(I18nSource i18nSource) {
        if (instance == null) {
            instance = i18nSource;
        }
    }

    public static I18nSource get() {
        return instance;
    }
}
