package io.ffit.carbon.i18n4j.springboot.autoconfigure;

import io.ffit.carbon.i18n4j.config.I18nConfig;
import io.ffit.carbon.i18n4j.core.DefaultI18nSource;
import io.ffit.carbon.i18n4j.core.I18nSource;
import io.ffit.carbon.i18n4j.datasource.DataSource;
import io.ffit.carbon.i18n4j.datasource.DataSourcePack;
import io.ffit.carbon.i18n4j.local.datasource.LocalDataSource;
import io.ffit.carbon.i18n4j.springboot.constants.PropertiesKeyConstants;
import io.ffit.carbon.i18n4j.springboot.context.I18nSourceHolder;
import io.ffit.carbon.i18n4j.springboot.properties.I18nProperties;
import io.ffit.carbon.i18n4j.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.origin.OriginTrackedValue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.PropertySource;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

/**
 * I18n Auto Configuration
 *
 * @author Lay
 * @date 2022/9/14
 */
@Configuration
@EnableConfigurationProperties(I18nProperties.class)
@ConditionalOnProperty(prefix = PropertiesKeyConstants.I18N, value = "enabled", matchIfMissing = true)
public class I18nAutoConfiguration {

    private final I18nProperties i18nProperties;

    private final Environment env;

    @Autowired
    public I18nAutoConfiguration(I18nProperties i18nProperties, Environment env) {
        this.i18nProperties = i18nProperties;
        this.env = env;
    }

    @Bean
    public I18nSource i18nSource() {
        I18nConfig config = I18nConfig.builder()
                .useCodeAsDefaultMessage(i18nProperties.isUseCodeAsDefaultMessage())
                .fallbackLanguageOnly(i18nProperties.isFallbackLanguageOnly())
                .autoLoad(i18nProperties.isAutoLoad())
                .autoReload(i18nProperties.isAutoReload())
                .messageFormatterClass(i18nProperties.getMessageFormatterClass())
                .dataSourcePack(dataSourcePack())
                .build();
        I18nSource i18nSource = new DefaultI18nSource(config);
        I18nSourceHolder.init(i18nSource);
        return i18nSource;
    }

    @Bean
    public DataSourcePack dataSourcePack() {
        I18nProperties.DataSource dataSource = Objects.requireNonNullElse(i18nProperties.getDatasource(), new I18nProperties.DataSource(LocalDataSource.DATA_TYPE, LocalDataSource.DATA_TYPE));

        if (Objects.equals(dataSource.getIndex(), dataSource.getSource())) {
            // same data source type
            DataSource ds = parseDataSource(dataSource.getIndex());
            return DataSourcePack.builder().both(ds).build();
        } else {
            return DataSourcePack.builder()
                    .index(parseDataSource(dataSource.getIndex()))
                    .source(parseDataSource(dataSource.getSource()))
                    .build();
        }
    }

    private DataSource parseDataSource(String dataType) {
        if (StringUtils.isBlank(dataType)) {
            dataType = LocalDataSource.DATA_TYPE;
        }

        Properties dataSourceProps = propsOfDataType(dataType);

        // find implementation of DataSource
        if (dataType.equals(LocalDataSource.DATA_TYPE)) {
            return new LocalDataSource(dataSourceProps);
        }

        return DataSourceScanner.scan(dataType, dataSourceProps);
    }

    private Properties propsOfDataType(String dataType) {
        String dsPrefix = PropertiesKeyConstants.DATA_SOURCE + "." + dataType;

        Map<String, Object> map = new HashMap<>();
        for (PropertySource<?> propertySource : ((AbstractEnvironment) env).getPropertySources()) {
            if (propertySource instanceof MapPropertySource) {
                ((MapPropertySource) propertySource).getSource().forEach((k, v) -> {
                    if (k.startsWith(dsPrefix)) {
                        String key = k.substring(dsPrefix.length() + 1);
                        map.put(key, ((OriginTrackedValue)v).getValue());
                    }
                });
            }
        }
        if (!map.isEmpty()) {
            Properties props = new Properties();
            props.putAll(map);
            return props;
        }
        return null;
    }
}
