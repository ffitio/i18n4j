package io.ffit.carbon.i18n4j.core.provider;

import io.ffit.carbon.i18n4j.core.mapper.IndexMapper;
import io.ffit.carbon.i18n4j.core.mapper.SourceMapper;
import io.ffit.carbon.i18n4j.core.monitor.Monitor;
import io.ffit.carbon.i18n4j.entity.Source;

/**
 * Source Provider
 *
 * @author Lay
 * @date 2022/9/11
 */
public interface SourceProvider {
    /**
     * get mapper
     * @return {@link IndexMapper}
     */
    SourceMapper mapper();

    /**
     * get monitor
     * @return {@link Monitor <Source>}
     */
    Monitor<Source> monitor();
}
