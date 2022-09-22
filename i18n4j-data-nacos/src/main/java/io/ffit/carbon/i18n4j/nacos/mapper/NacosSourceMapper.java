package io.ffit.carbon.i18n4j.nacos.mapper;

import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.exception.NacosException;
import io.ffit.carbon.i18n4j.core.mapper.SourceMapper;
import io.ffit.carbon.i18n4j.entity.Source;
import io.ffit.carbon.i18n4j.nacos.datasource.NacosDataSource;
import io.ffit.carbon.i18n4j.util.MapUtils;
import io.ffit.carbon.i18n4j.util.StringUtils;

import java.util.*;

/**
 * Nacos Source Mapper
 *
 * @author Lay
 * @date 2022/9/14
 */
public class NacosSourceMapper implements SourceMapper {

    private final NacosDataSource dataSource;

    private final ConfigService configService;

    public NacosSourceMapper(NacosDataSource dataSource) {
        this.dataSource = dataSource;
        this.configService = dataSource.configService();
    }

    @Override
    public int insert(Source source) {
        try {
            if (configService.publishConfig(dataSource.getSourceNameFormatter().format(source.getTag(), source.getLocale()), dataSource.getGroup(), dataSource.getSourceCodec().encode(source.getProps()))) {
                return 1;
            }
        } catch (NacosException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }

    @Override
    public int update(Source source) {
        return insert(source);
    }

    @Override
    public int insertOrUpdate(Source source) {
        return insert(source);
    }

    @Override
    public Source selectOne(String tag, Locale locale) {
        try {
            String data = configService.getConfig(dataSource.getSourceNameFormatter().format(tag, locale), dataSource.getGroup(), dataSource.getTimeoutInMills());
            if (StringUtils.isNotBlank(data)) {
                return new Source(tag, locale, dataSource.getSourceCodec().decode(data));
            }
        } catch (NacosException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public List<Source> selectAll(Map<String, Set<Locale>> map) {
        if (MapUtils.isEmpty(map)) {
            return Collections.emptyList();
        }

        List<Source> sources = new ArrayList<>();
        map.forEach((k, v) -> v.forEach(locale -> {
            Source source = selectOne(k, locale);
            if (source != null) {
                sources.add(source);
            }
        }));
        return sources;
    }

    @Override
    public int delete(String tag, Locale locale) {
        try {
            if (configService.removeConfig(dataSource.getSourceNameFormatter().format(tag, locale), dataSource.getGroup())) {
                return 1;
            }
        } catch (NacosException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }
}
