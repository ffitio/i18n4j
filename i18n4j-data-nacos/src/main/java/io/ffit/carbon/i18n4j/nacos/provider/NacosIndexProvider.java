package io.ffit.carbon.i18n4j.nacos.provider;

import io.ffit.carbon.i18n4j.core.provider.AbstractIndexProvider;
import io.ffit.carbon.i18n4j.nacos.datasource.NacosDataSource;
import io.ffit.carbon.i18n4j.nacos.mapper.NacosIndexMapper;
import io.ffit.carbon.i18n4j.nacos.monitor.NacosIndexMonitor;

/**
 * Nacos Index Provider
 *
 * @author Lay
 * @date 2022/9/14
 */
public class NacosIndexProvider extends AbstractIndexProvider {
    public NacosIndexProvider(NacosDataSource dataSource) {
        super(new NacosIndexMapper(dataSource), new NacosIndexMonitor(dataSource));
    }
}
