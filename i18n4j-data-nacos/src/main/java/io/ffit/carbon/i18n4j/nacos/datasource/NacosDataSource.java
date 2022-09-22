package io.ffit.carbon.i18n4j.nacos.datasource;

import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.exception.NacosException;
import io.ffit.carbon.i18n4j.core.provider.IndexProvider;
import io.ffit.carbon.i18n4j.core.provider.SourceProvider;
import io.ffit.carbon.i18n4j.datasource.AbstractDataSource;
import io.ffit.carbon.i18n4j.datasource.DataSourceProperties;
import io.ffit.carbon.i18n4j.datasource.annotation.I18nDataSource;
import io.ffit.carbon.i18n4j.nacos.provider.NacosIndexProvider;
import io.ffit.carbon.i18n4j.nacos.provider.NacosSourceProvider;

import java.util.Optional;
import java.util.Properties;

/**
 * Nacos Data Source
 *
 * @author Lay
 * @date 2022/9/14
 */
@I18nDataSource(NacosDataSource.DATA_TYPE)
public class NacosDataSource extends AbstractDataSource {

    public static final String DATA_TYPE = "nacos";

    public static final String GROUP = "group";

    public static final String INDEX = "index";

    public static final String TIMEOUT_IN_MILLS = "timeoutInMills";

    /**
     * default nacos group
     */
    private static final String DEFAULT_GROUP = "I18N";

    /**
     * default index filename
     */
    public static final String DEFAULT_INDEX = "index.yaml";

    /**
     * default timeout in milliseconds
     */
    public static final int DEFAULT_TIMEOUT_IN_MILLS = 1000;

    private ConfigService configService;

    private String group;

    private String index;

    private int timeoutInMills;

    public NacosDataSource(Properties props) {
        super(props);
    }

    @Override
    protected DataSourceProperties getDataSourceProps() {
        DataSourceProperties props = super.getDataSourceProps();
        props.remove(GROUP);
        props.remove(INDEX);
        props.remove(TIMEOUT_IN_MILLS);
        return props;
    }

    @Override
    protected void parseProps() {
        this.group = props.getProperty(GROUP, DEFAULT_GROUP);
        this.index = props.getProperty(INDEX, DEFAULT_INDEX);
        this.timeoutInMills = Optional.ofNullable(props.getProperty(TIMEOUT_IN_MILLS)).map(Integer::parseInt).orElse(DEFAULT_TIMEOUT_IN_MILLS);
        try {
            configService = NacosFactory.createConfigService(getDataSourceProps());
        } catch (NacosException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected IndexProvider createIndexProvider() {
        return new NacosIndexProvider(this);
    }

    @Override
    protected SourceProvider createSourceProvider() {
        return new NacosSourceProvider(this);
    }

    public ConfigService configService() {
        return configService;
    }

    public String getGroup() {
        return group;
    }

    public String getIndex() {
        return index;
    }

    public int getTimeoutInMills() {
        return timeoutInMills;
    }
}
