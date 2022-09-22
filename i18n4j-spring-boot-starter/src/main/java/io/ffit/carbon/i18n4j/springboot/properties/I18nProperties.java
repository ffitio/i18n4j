package io.ffit.carbon.i18n4j.springboot.properties;

import io.ffit.carbon.i18n4j.core.formatter.MessageFormatter;
import io.ffit.carbon.i18n4j.springboot.constants.PropertiesKeyConstants;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

/**
 * I18n Properties
 *
 * @author Lay
 * @date 2022/9/14
 */
@ConfigurationProperties(prefix = PropertiesKeyConstants.I18N)
@ConfigurationPropertiesScan
public class I18nProperties {
    /**
     * i18n enabled
     */
    private boolean enabled;

    /**
     * is return code when message is null
     */
    private boolean useCodeAsDefaultMessage = false;

    /**
     * is fallback to Locale without country
     */
    private boolean fallbackLanguageOnly = false;

    /**
     * auto load
     */
    private boolean autoLoad = true;

    /**
     * auto reload when index or source changed
     */
    private boolean autoReload = true;

    /**
     * message formatter class
     */
    private Class<? extends MessageFormatter> messageFormatterClass;

    /**
     * data source
     */
    private DataSource datasource;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
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

    public Class<? extends MessageFormatter> getMessageFormatterClass() {
        return messageFormatterClass;
    }

    public void setMessageFormatterClass(Class<? extends MessageFormatter> messageFormatterClass) {
        this.messageFormatterClass = messageFormatterClass;
    }

    public DataSource getDatasource() {
        return datasource;
    }

    public void setDatasource(DataSource datasource) {
        this.datasource = datasource;
    }

    public static class DataSource {
        private String index;
        private String source;

        public DataSource() {}

        public DataSource(String index, String source) {
            this.index = index;
            this.source = source;
        }

        public String getIndex() {
            return index;
        }

        public void setIndex(String index) {
            this.index = index;
        }

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }
    }
}
