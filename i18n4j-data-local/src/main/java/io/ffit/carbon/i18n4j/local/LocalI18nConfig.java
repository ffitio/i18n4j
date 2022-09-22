package io.ffit.carbon.i18n4j.local;

import io.ffit.carbon.i18n4j.config.I18nConfig;
import io.ffit.carbon.i18n4j.datasource.DataSourcePack;
import io.ffit.carbon.i18n4j.local.datasource.LocalDataSource;

/**
 * Default Config
 *
 * @author Lay
 * @date 2022/9/22
 */
public class LocalI18nConfig {
    public static final I18nConfig DEFAULT;

    static {
        DEFAULT = I18nConfig.builder()
                .autoLoad(true)
                .autoReload(true)
                .useCodeAsDefaultMessage(false)
                .fallbackLanguageOnly(true)
                .dataSourcePack(DataSourcePack.builder().both(new LocalDataSource(null)).build())
                .build();
    }
}
