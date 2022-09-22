package io.ffit.carbon.i18n4j.local.provider;

import io.ffit.carbon.i18n4j.core.provider.AbstractSourceProvider;
import io.ffit.carbon.i18n4j.local.datasource.LocalDataSource;
import io.ffit.carbon.i18n4j.local.mapper.LocalSourceMapper;
import io.ffit.carbon.i18n4j.local.monitor.LocalSourceMonitor;

/**
 * Local Source Provider
 *
 * @author Lay
 * @date 2022/9/14
 */
public class LocalSourceProvider extends AbstractSourceProvider {
    public LocalSourceProvider(LocalDataSource dataSource) {
        super(new LocalSourceMapper(dataSource), new LocalSourceMonitor(dataSource));
    }
}
