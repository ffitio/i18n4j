package io.ffit.carbon.i18n4j.local.provider;

import io.ffit.carbon.i18n4j.core.provider.AbstractIndexProvider;
import io.ffit.carbon.i18n4j.local.datasource.LocalDataSource;
import io.ffit.carbon.i18n4j.local.mapper.LocalIndexMapper;
import io.ffit.carbon.i18n4j.local.monitor.LocalIndexMonitor;

/**
 * Local Index Provider
 *
 * @author Lay
 * @date 2022/9/14
 */
public class LocalIndexProvider extends AbstractIndexProvider {
    public LocalIndexProvider(LocalDataSource dataSource) {
        super(new LocalIndexMapper(dataSource), new LocalIndexMonitor(dataSource));
    }
}
