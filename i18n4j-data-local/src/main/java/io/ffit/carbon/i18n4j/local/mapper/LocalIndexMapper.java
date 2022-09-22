package io.ffit.carbon.i18n4j.local.mapper;

import io.ffit.carbon.i18n4j.core.mapper.IndexMapper;
import io.ffit.carbon.i18n4j.entity.Index;
import io.ffit.carbon.i18n4j.local.datasource.LocalDataSource;

/**
 * Local Index Mapper
 *
 * @author Lay
 * @date 2022/9/6
 */
public class LocalIndexMapper implements IndexMapper {

    private final LocalDataSource dataSource;

    private final LocalDataSource.Driver driver;
    
    public LocalIndexMapper(LocalDataSource datasource) {
        this.dataSource = datasource;
        this.driver = datasource.driver();
    }
    
    @Override
    public int insert(Index index) {
        if (driver.create(dataSource.getIndex())) {
            String data = dataSource.getIndexCodec().encode(index);
            driver.write(dataSource.getIndex(), data);
            return 1;
        }
        return 0;
    }

    @Override
    public int update(Index index) {
        if (driver.exists(dataSource.getIndex())) {
            String data = dataSource.getIndexCodec().encode(index);
            driver.write(dataSource.getIndex(), data);
            return 1;
        }
        return 0;
    }

    @Override
    public Index select() {
        String data = driver.read(dataSource.getIndex());
        return dataSource.getIndexCodec().decode(data);
    }

    @Override
    public int delete() {
        return driver.del(dataSource.getIndex());
    }
}
