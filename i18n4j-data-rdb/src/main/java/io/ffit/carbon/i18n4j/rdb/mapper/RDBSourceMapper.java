package io.ffit.carbon.i18n4j.rdb.mapper;

import io.ffit.carbon.i18n4j.core.mapper.SourceMapper;
import io.ffit.carbon.i18n4j.entity.Source;
import io.ffit.carbon.i18n4j.rdb.datasource.RDBDataSource;
import io.ffit.carbon.i18n4j.rdb.datasource.RDBDriver;
import io.ffit.carbon.i18n4j.util.MapUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.*;

/**
 * SQL Source Mapper
 *
 * @author Lay
 * @date 2022/9/16
 */
public class RDBSourceMapper implements SourceMapper {

    private final RDBDataSource dataSource;
    private final RDBDriver driver;

    public RDBSourceMapper(RDBDataSource dataSource) {
        this.dataSource = dataSource;
        this.driver = dataSource.driver();

        if (dataSource.isAutoCreateTable()) {
            createTable();
        }
    }

    private void createTable() {
        try (InputStream is = this.getClass().getClassLoader().getResourceAsStream("sql/i18n_source.sql")) {
            if (is != null) {
                Reader reader = new InputStreamReader(is);
                driver.runScript(reader);
            }
        } catch (IOException ignored) {}
    }

    @Override
    public int insert(Source source) {
        String sql = String.format("INSERT INTO %s (tag, locale, props, update_time)VALUES(?, ?, ?, ?)", dataSource.getSourceTableName());
        return driver.execute(sql, Arrays.asList(source.getTag(), source.getLocale().toLanguageTag(), dataSource.getSourceCodec().encode(source.getProps()), System.currentTimeMillis()));
    }

    @Override
    public int update(Source source) {
        String sql = String.format("UPDATE %s SET props = ?, update_time = ? WHERE tag = ? AND locale = ?", dataSource.getSourceTableName());
        return driver.execute(sql, Arrays.asList(dataSource.getSourceCodec().encode(source.getProps()), System.currentTimeMillis(), source.getTag(), source.getLocale().toLanguageTag()));
    }

    @Override
    public int insertOrUpdate(Source source) {
        String sql = String.format("INSERT INTO %s (tag, locale, props, update_time)VALUES(?, ?, ?, ?) ON DUPLICATE KEY UPDATE SET props=VALUES(props), update_time=VALUES(update_time)", dataSource.getSourceTableName());
        return driver.execute(sql, Arrays.asList(source.getTag(), source.getLocale().toLanguageTag(), dataSource.getSourceCodec().encode(source.getProps()), System.currentTimeMillis()));
    }

    @Override
    public Source selectOne(String tag, Locale locale) {
        String sql = String.format("SELECT props FROM %s WHERE tag = ? AND locale = ?", dataSource.getSourceTableName());
        Map<String, Object> map = driver.query(sql, Arrays.asList(tag, locale.toLanguageTag()));
        if (MapUtils.isNotEmpty(map)) {
            return new Source(tag, locale, dataSource.getSourceCodec().decode(map.get("props").toString()));
        }
        return null;
    }

    @Override
    public List<Source> selectAll(Map<String, Set<Locale>> map) {
        if (MapUtils.isNotEmpty(map)) {
            StringBuilder where = new StringBuilder();
            List<Map.Entry<String, Set<Locale>>> list = new ArrayList<>(map.entrySet());
            int size = list.size();
            for (int i = 0; i < size; i++) {
                Map.Entry<String, Set<Locale>> entry = list.get(i);
                where.append("(tag = '").append(entry.getKey()).append("' AND FIND_IN_SET(locale,'").append(String.join(",", entry.getValue().stream().map(Locale::toLanguageTag).toArray(String[]::new))).append("'))");
                if (i != size - 1) {
                    where.append(" OR ");
                }
            }
            String sql = String.format("SELECT * FROM %s WHERE %s", dataSource.getSourceTableName(), where);
            List<Map<String, Object>> res = driver.queryAll(sql, null);
            List<Source> sources = new ArrayList<>();
            res.forEach(m -> {
                Source source = new Source(m.get("tag").toString(), Locale.forLanguageTag(m.get("locale").toString()), dataSource.getSourceCodec().decode(m.get("props").toString()));
                sources.add(source);
            });
            return sources;
        }
        return null;
    }

    @Override
    public int delete(String tag, Locale locale) {
        String sql = String.format("DELETE FROM %s WHERE tag = ? AND locale = ?", dataSource.getSourceTableName());
        return driver.execute(sql, Arrays.asList(tag, locale.toLanguageTag()));
    }
}
