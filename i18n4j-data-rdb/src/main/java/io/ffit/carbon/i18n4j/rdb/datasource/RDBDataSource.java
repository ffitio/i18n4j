package io.ffit.carbon.i18n4j.rdb.datasource;

import io.ffit.carbon.i18n4j.core.provider.IndexProvider;
import io.ffit.carbon.i18n4j.core.provider.SourceProvider;
import io.ffit.carbon.i18n4j.datasource.AbstractDataSource;
import io.ffit.carbon.i18n4j.datasource.DataSourceProperties;
import io.ffit.carbon.i18n4j.datasource.annotation.I18nDataSource;
import io.ffit.carbon.i18n4j.rdb.provider.RDBIndexProvider;
import io.ffit.carbon.i18n4j.rdb.provider.RDBSourceProvider;

import java.util.Optional;
import java.util.Properties;

/**
 * SQL Data Source
 *
 * @author Lay
 * @date 2022/9/16
 */
@I18nDataSource(RDBDataSource.DATA_TYPE)
public class RDBDataSource extends AbstractDataSource {
    public static final String DATA_TYPE = "rdb";

    public static final String DEFAULT_INDEX_TABLE = "i18n_index";

    public static final String DEFAULT_SOURCE_TABLE = "i18n_source";

    public static final String INDEX_TABLE_NAME = "indexTableName";

    public static final String SOURCE_TABLE_NAME = "sourceTableName";

    public static final String AUTO_CREATE_TABLE = "autoCreateTable";

    /**
     * driver
     */
    private RDBDriver driver;

    /**
     * table name of Index
     */
    private String indexTableName;

    /**
     * table name of Source
     */
    private String sourceTableName;

    /**
     * auto create table
     */
    private boolean autoCreateTable;

    public RDBDataSource(Properties props) {
        super(props);
    }

    @Override
    protected DataSourceProperties getDataSourceProps() {
        DataSourceProperties props = super.getDataSourceProps();
        props.remove(INDEX_TABLE_NAME);
        props.remove(SOURCE_TABLE_NAME);
        props.remove(AUTO_CREATE_TABLE);
        return props;
    }

    @Override
    protected void parseProps() {
        this.indexTableName = props.getProperty(INDEX_TABLE_NAME, DEFAULT_INDEX_TABLE);
        this.sourceTableName = props.getProperty(SOURCE_TABLE_NAME, DEFAULT_SOURCE_TABLE);
        this.autoCreateTable = Optional.ofNullable(props.get(AUTO_CREATE_TABLE)).map(t -> Boolean.parseBoolean(t.toString())).orElse(false);
        this.driver = new RDBDriver(getDataSourceProps());
    }

    @Override
    protected IndexProvider createIndexProvider() {
        return new RDBIndexProvider(this);
    }

    @Override
    protected SourceProvider createSourceProvider() {
        return new RDBSourceProvider(this);
    }

    public RDBDriver driver() {
        return driver;
    }

    public String getIndexTableName() {
        return indexTableName;
    }

    public String getSourceTableName() {
        return sourceTableName;
    }

    public boolean isAutoCreateTable() {
        return autoCreateTable;
    }
}
