package io.ffit.carbon.i18n4j.local.mapper;

import io.ffit.carbon.i18n4j.core.mapper.SourceMapper;
import io.ffit.carbon.i18n4j.entity.Source;
import io.ffit.carbon.i18n4j.local.datasource.LocalDataSource;
import io.ffit.carbon.i18n4j.util.MapUtils;

import java.util.*;

/**
 * Source Mapper
 *
 * @author Lay
 * @date 2022/9/6
 */
public class LocalSourceMapper implements SourceMapper {

    private final LocalDataSource dataSource;
    private final LocalDataSource.Driver driver;

    public LocalSourceMapper(LocalDataSource datasource) {
        this.dataSource = datasource;
        this.driver = datasource.driver();
    }

    @Override
    public int insert(Source source) {
        String filename = dataSource.getSourceFileName(source.getTag(), source.getLocale());
        if (driver.create(filename)) {
            String data = dataSource.getSourceCodec().encode(source.getProps());
            driver.write(filename, data);
            return 1;
        }
        return 0;
    }

    @Override
    public int update(Source source) {
        String filename = dataSource.getSourceFileName(source.getTag(), source.getLocale());
        if (driver.exists(filename)) {
            Source orig = selectOne(source.getTag(), source.getLocale());
            if (orig.equals(source)) {
                return 0;
            }
            String data = dataSource.getSourceCodec().encode(source.getProps());
            driver.write(filename, data);
            return 1;
        }
        return 0;
    }

    @Override
    public int insertOrUpdate(Source source) {
        return insert(source) == 1 ? 1 : update(source);
    }

    @Override
    public Source selectOne(String tag, Locale locale) {
        String filename = dataSource.getSourceFileName(tag, locale);
        if (driver.exists(filename)) {
            Properties props = dataSource.getSourceCodec().decode(driver.read(filename));
            return new Source(tag, locale, props);
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
        String filename = dataSource.getSourceFileName(tag, locale);
        return driver.del(filename);
    }
}
