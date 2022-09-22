package io.ffit.carbon.i18n4j.config;

import io.ffit.carbon.i18n4j.datasource.DataSourcePack;
import io.ffit.carbon.i18n4j.core.formatter.MessageFormatter;

/**
 * Intl Configuration
 *
 * @author Lay
 * @date 2022/9/6
 */
public class I18nConfig {
    /**
     * is return code when message is null
     */
    private boolean useCodeAsDefaultMessage;

    /**
     * is fallback to Locale without country
     */
    private boolean fallbackLanguageOnly;

    /**
     * auto load
     */
    private boolean autoLoad;

    /**
     * auto reload when index or source changed
     */
    private boolean autoReload;

    /**
     * loader
     */
    private DataSourcePack dataSourcePack;

    /**
     * message formatter class
     */
    private Class<? extends MessageFormatter> messageFormatterClass;

    private I18nConfig(Builder builder) {
        useCodeAsDefaultMessage = builder.useCodeAsDefaultMessage;
        fallbackLanguageOnly = builder.fallbackLanguageOnly;
        autoLoad = builder.autoLoad;
        autoReload = builder.autoReload;
        dataSourcePack = builder.dataSourcePack;
        messageFormatterClass = builder.messageFormatterClass;
    }

    public boolean isUseCodeAsDefaultMessage() {
        return useCodeAsDefaultMessage;
    }

    public void setUseCodeAsDefaultMessage(boolean useCodeAsDefaultMessage) {
        this.useCodeAsDefaultMessage = useCodeAsDefaultMessage;
    }

    public boolean isFallbackLanguageOnly() {
        return fallbackLanguageOnly;
    }

    public void setFallbackLanguageOnly(boolean fallbackLanguageOnly) {
        this.fallbackLanguageOnly = fallbackLanguageOnly;
    }

    public boolean isAutoLoad() {
        return autoLoad;
    }

    public void setAutoLoad(boolean autoLoad) {
        this.autoLoad = autoLoad;
    }

    public boolean isAutoReload() {
        return autoReload;
    }

    public void setAutoReload(boolean autoReload) {
        this.autoReload = autoReload;
    }

    public DataSourcePack getDataSourcePack() {
        return dataSourcePack;
    }

    public void setDataSourcePack(DataSourcePack dataSourcePack) {
        this.dataSourcePack = dataSourcePack;
    }

    public Class<? extends MessageFormatter> getMessageFormatterClass() {
        return messageFormatterClass;
    }

    public void setMessageFormatterClass(Class<? extends MessageFormatter> messageFormatterClass) {
        this.messageFormatterClass = messageFormatterClass;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        /**
         * is return code when message is null
         */
        private boolean useCodeAsDefaultMessage;

        /**
         * is fallback to Locale without country
         */
        private boolean fallbackLanguageOnly;

        /**
         * auto load
         */
        private boolean autoLoad;

        /**
         * auto reload when index or source changed
         */
        private boolean autoReload;

        /**
         * data source pack
         */
        private DataSourcePack dataSourcePack;

        /**
         * message formatter
         */
        private Class<? extends MessageFormatter> messageFormatterClass;

        public Builder useCodeAsDefaultMessage(boolean useCodeAsDefaultMessage) {
            this.useCodeAsDefaultMessage = useCodeAsDefaultMessage;
            return this;
        }

        public Builder fallbackLanguageOnly(boolean fallbackLanguageOnly) {
            this.fallbackLanguageOnly = fallbackLanguageOnly;
            return this;
        }

        public Builder autoLoad(boolean autoLoad) {
            this.autoLoad = autoLoad;
            return this;
        }

        public Builder autoReload(boolean autoReload) {
            this.autoReload = autoReload;
            return this;
        }

        public Builder dataSourcePack(DataSourcePack dataSourcePack) {
            this.dataSourcePack = dataSourcePack;
            return this;
        }

        public Builder messageFormatterClass(Class<? extends MessageFormatter> messageFormatterClass) {
            this.messageFormatterClass = messageFormatterClass;
            return this;
        }

        public I18nConfig build() {
            return new I18nConfig(this);
        }
    }
}
