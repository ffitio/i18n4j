package io.ffit.carbon.i18n4j.core.provider;

import io.ffit.carbon.i18n4j.datasource.DataSource;

/**
 * Abstract implementation of {@link IndexProvider}
 *
 * @author Lay
 * @date 2022/9/13
 */
public abstract class AbstractProvider implements IndexProvider {

    protected final DataSource dataSource;

    public AbstractProvider(DataSource dataSource) {
        this.dataSource = dataSource;
    }
}
