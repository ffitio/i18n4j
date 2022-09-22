package io.ffit.carbon.i18n4j.datasource;

/**
 * Data Source Pack
 *
 * @author Lay
 * @date 2022/9/13
 */
public class DataSourcePack {

    private final DataSource index;
    private final DataSource source;

    public DataSourcePack(Builder builder) {
        if (builder.index == null) {
            throw new RuntimeException("DataSource of index cannot be null");
        }
        if (builder.source == null) {
            throw new RuntimeException("DataSource of source cannot be null");
        }

        this.index = builder.index;
        this.source = builder.source;
    }

    public DataSource getIndex() {
        return index;
    }

    public DataSource getSource() {
        return source;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private DataSource index;
        private DataSource source;

        public Builder index(DataSource index) {
            this.index = index;
            return this;
        }

        public Builder source(DataSource source) {
            this.source = source;
            return this;
        }

        public Builder both(DataSource dataSource) {
            this.index = dataSource;
            this.source = dataSource;
            return this;
        }

        public DataSourcePack build() {
            return new DataSourcePack(this);
        }
    }
}
