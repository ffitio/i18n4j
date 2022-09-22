package io.ffit.carbon.i18n4j.nacos.mapper;

import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.exception.NacosException;
import io.ffit.carbon.i18n4j.core.mapper.IndexMapper;
import io.ffit.carbon.i18n4j.entity.Index;
import io.ffit.carbon.i18n4j.nacos.datasource.NacosDataSource;

/**
 * Nacos Index Mapper
 *
 * @author Lay
 * @date 2022/9/14
 */
public class NacosIndexMapper implements IndexMapper {

    private final NacosDataSource dataSource;
    
    private final ConfigService configService;

    public NacosIndexMapper(NacosDataSource dataSource) {
        this.dataSource = dataSource;
        this.configService = dataSource.configService();
    }

    @Override
    public int insert(Index index) {
        try {
            if (configService.publishConfig(dataSource.getIndex(), dataSource.getGroup(), dataSource.getIndexCodec().encode(index))) {
                return 1;
            }
        } catch (NacosException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }

    @Override
    public int update(Index index) {
        return insert(index);
    }

    @Override
    public Index select() {
        try {
            String index = configService.getConfig(dataSource.getIndex(), dataSource.getGroup(), dataSource.getTimeoutInMills());
            return dataSource.getIndexCodec().decode(index);
        } catch (NacosException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int delete() {
        try {
            if (configService.removeConfig(dataSource.getIndex(), dataSource.getGroup())) {
                return 1;
            }
        } catch (NacosException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }
}
