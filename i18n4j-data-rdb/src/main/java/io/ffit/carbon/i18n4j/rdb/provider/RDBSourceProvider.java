package io.ffit.carbon.i18n4j.rdb.provider;

import io.ffit.carbon.i18n4j.core.provider.AbstractSourceProvider;
import io.ffit.carbon.i18n4j.rdb.datasource.RDBDataSource;
import io.ffit.carbon.i18n4j.rdb.mapper.RDBSourceMapper;
import io.ffit.carbon.i18n4j.rdb.monitor.RDBSourceMonitor;

/**
 * 备注
 *
 * @author Lay
 * @date 2022/9/16
 */
public class RDBSourceProvider extends AbstractSourceProvider {
    public RDBSourceProvider(RDBDataSource dataSource) {
        super(new RDBSourceMapper(dataSource), new RDBSourceMonitor(dataSource));
    }
}
