package io.ffit.carbon.i18n4j.rdb.mapper;

import io.ffit.carbon.i18n4j.core.mapper.IndexMapper;
import io.ffit.carbon.i18n4j.entity.Index;
import io.ffit.carbon.i18n4j.rdb.datasource.RDBDataSource;
import io.ffit.carbon.i18n4j.rdb.datasource.RDBDriver;
import io.ffit.carbon.i18n4j.util.CollectionUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.*;

/**
 * SQL Index Mapper
 *
 * @author Lay
 * @date 2022/9/16
 */
public class RDBIndexMapper implements IndexMapper {

    private static final String COLUMNS = "tag, locale";

    private final RDBDataSource dataSource;

    private final RDBDriver driver;

    public RDBIndexMapper(RDBDataSource dataSource) {
        this.dataSource = dataSource;
        this.driver = dataSource.driver();

        if (dataSource.isAutoCreateTable()) {
            createTable();
        }
    }

    private void createTable() {
        try (InputStream is = this.getClass().getClassLoader().getResourceAsStream("sql/i18n_index.sql")) {
            if (is != null) {
                Reader reader = new InputStreamReader(is);
                driver.runScript(reader);
            }
        } catch (IOException ignored) {}
    }

    @Override
    public int insert(Index index) {
        if (index == null) {
            return 0;
        }
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO ").append(dataSource.getIndexTableName())
                .append("(").append(COLUMNS).append(")")
                        .append("VALUES");
        index.forEach((tag, locales) -> {
            List<Locale> localeList = new ArrayList<>(locales);
            int size = locales.size();
            for (int i = 0; i < size; i++) {
                sql.append("(")
                        .append("\"").append(tag).append("\",")
                        .append("\"").append(localeList.get(i)).append("\"")
                        .append(")");
                if (i != size - 1) {
                    sql.append(",");
                }
            }
        });
        return driver.execute(sql.toString(), null);
    }

    @Override
    public int update(Index index) {
        if (index == null) {
            return 0;
        }
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO ").append(dataSource.getIndexTableName())
                .append("(").append(COLUMNS).append(")")
                .append("VALUES");
        index.forEach((tag, locales) -> {
            List<Locale> localeList = new ArrayList<>(locales);
            int size = locales.size();
            for (int i = 0; i < size; i++) {
                sql.append("(")
                        .append("\"").append(tag).append("\",")
                        .append("\"").append(localeList.get(i)).append("\"")
                        .append(")");
                if (i != size - 1) {
                    sql.append(",");
                }
            }
        });
        sql.append("ON DUPLICATE KEY UPDATE locale=VALUES(locale)");

        return driver.execute(sql.toString(), null);
    }

    @Override
    public Index select() {
        String sql = String.format("SELECT %s FROM %s", COLUMNS, dataSource.getIndexTableName());
        List<Map<String, Object>> list = driver.queryAll(sql, null);
        if (CollectionUtils.isNotEmpty(list)) {
            Map<String, Set<Locale>> target = new HashMap<>();
            list.forEach(m -> {
                Set<Locale> locales = target.computeIfAbsent(m.get("tag").toString(), k -> new HashSet<>());
                locales.add(Locale.forLanguageTag(m.get("locale").toString()));
            });

            Index index = new Index();
            index.putAll(target);
            return index;
        }
        return null;
    }

    @Override
    public int delete() {
        String sql = String.format("DELETE FROM %s", dataSource.getIndexTableName());
        return driver.execute(sql, null);
    }
}
