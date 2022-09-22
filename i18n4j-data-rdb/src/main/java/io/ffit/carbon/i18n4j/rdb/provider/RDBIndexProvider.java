package io.ffit.carbon.i18n4j.rdb.provider;

import io.ffit.carbon.i18n4j.core.provider.AbstractIndexProvider;
import io.ffit.carbon.i18n4j.rdb.datasource.RDBDataSource;
import io.ffit.carbon.i18n4j.rdb.mapper.RDBIndexMapper;
import io.ffit.carbon.i18n4j.rdb.monitor.RDBIndexMonitor;

/**
 * MySQL Index Provider
 *
 * @author Lay
 * @date 2022/9/16
 */
public class RDBIndexProvider extends AbstractIndexProvider {
    public RDBIndexProvider(RDBDataSource dataSource) {
        super(new RDBIndexMapper(dataSource), new RDBIndexMonitor(dataSource));
    }
}
