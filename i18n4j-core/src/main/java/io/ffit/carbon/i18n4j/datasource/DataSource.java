package io.ffit.carbon.i18n4j.datasource;

import io.ffit.carbon.i18n4j.core.provider.IndexProvider;
import io.ffit.carbon.i18n4j.core.provider.SourceProvider;

/**
 * Data Source
 *
 * @author Lay
 * @date 2022/9/13
 */
public interface DataSource {
    /**
     * get index provider
     * @return {@link IndexProvider}
     */
    IndexProvider indexProvider();

    /**
     * get source provider
     * @return {@link SourceProvider}
     */
    SourceProvider sourceProvider();
}
