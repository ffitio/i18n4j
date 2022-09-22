package io.ffit.carbon.i18n4j.rdb.datasource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.ffit.carbon.i18n4j.datasource.DataSourceProperties;
import io.ffit.carbon.i18n4j.util.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.io.Reader;
import java.sql.*;
import java.util.*;

/**
 * MySQL Driver
 *
 * @author Lay
 * @date 2022/9/16
 */
public class RDBDriver {

    private static final Logger logger = LoggerFactory.getLogger(RDBDriver.class);
    private final DataSource dataSource;

    public RDBDriver(DataSourceProperties props) {
        // create data source
        this.dataSource = new HikariDataSource(new HikariConfig(props));
    }

    public int execute(String sql, List<Object> params) {
        try (Connection conn = dataSource.getConnection(); PreparedStatement statement = statementWithParams(conn, sql, params)) {
            return statement.executeUpdate();
        } catch (SQLException e) {
            logger.error("SQL execute failed", e);
            return 0;
        }
    }

    public Map<String, Object> query(String sql, List<Object> params) {
        Map<String, Object> map = new HashMap<>();
        try (Connection conn = dataSource.getConnection(); PreparedStatement statement = statementWithParams(conn, sql, params)) {
            ResultSet rs = statement.executeQuery();
            ResultSetMetaData metaData = rs.getMetaData();

            // get columns
            Set<String> keySet = new HashSet<>();
            for (int i = 0; i < metaData.getColumnCount(); i++) {
                keySet.add(metaData.getColumnName(i + 1));
            }

            // get results
            if (rs.next()) {
                for (String column : keySet) {
                    map.put(column, rs.getObject(column));
                }
            }
        } catch (SQLException e) {
            logger.error("SQL query failed", e);
        }
        return map;
    }

    public List<Map<String, Object>> queryAll(String sql, List<Object> params) {
        List<Map<String, Object>> list = new ArrayList<>();
        try (Connection conn = dataSource.getConnection(); PreparedStatement statement = statementWithParams(conn, sql, params)) {
            ResultSet rs = statement.executeQuery();
            ResultSetMetaData metaData = rs.getMetaData();

            // get columns
            Set<String> keySet = new HashSet<>();
            for (int i = 0; i < metaData.getColumnCount(); i++) {
                keySet.add(metaData.getColumnName(i + 1));
            }

            // get results
            while (rs.next()) {
                Map<String, Object> map = new HashMap<>();
                for (String column : keySet) {
                    map.put(column, rs.getObject(column));
                }
                list.add(map);
            }
        } catch (SQLException e) {
            logger.error("SQL query failed", e);
        }
        return list;
    }

    public void runScript(Reader reader) {
        try (Connection conn = dataSource.getConnection()) {
            ScriptRunner runner = new ScriptRunner(conn, false, true);
            runner.runScript(reader);
        } catch (Exception e) {
            logger.error("SQL script run failed", e);
        }
    }

    private PreparedStatement statementWithParams(Connection connection, String sql, List<Object> params) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(sql);
        if (CollectionUtils.isNotEmpty(params)) {
            for (int i = 0; i < params.size(); i++) {
                statement.setObject(i + 1, params.get(i));
            }
        }

        return statement;
    }
}
