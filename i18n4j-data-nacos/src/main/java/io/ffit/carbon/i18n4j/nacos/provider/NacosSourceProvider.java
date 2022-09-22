package io.ffit.carbon.i18n4j.nacos.provider;

import io.ffit.carbon.i18n4j.core.provider.AbstractSourceProvider;
import io.ffit.carbon.i18n4j.nacos.datasource.NacosDataSource;
import io.ffit.carbon.i18n4j.nacos.mapper.NacosSourceMapper;
import io.ffit.carbon.i18n4j.nacos.monitor.NacosSourceMonitor;

/**
 * Nacos Source Provider
 *
 * @author Lay
 * @date 2022/9/14
 */
public class NacosSourceProvider extends AbstractSourceProvider {
    public NacosSourceProvider(NacosDataSource dataSource) {
        super(new NacosSourceMapper(dataSource), new NacosSourceMonitor(dataSource));
    }
}
