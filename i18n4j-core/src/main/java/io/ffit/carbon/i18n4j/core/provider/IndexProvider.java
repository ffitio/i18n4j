package io.ffit.carbon.i18n4j.core.provider;

import io.ffit.carbon.i18n4j.core.mapper.IndexMapper;
import io.ffit.carbon.i18n4j.core.monitor.Monitor;
import io.ffit.carbon.i18n4j.entity.Index;

/**
 * Index Provider
 *
 * @author Lay
 * @date 2022/9/11
 */
public interface IndexProvider {
    /**
     * get mapper
     * @return {@link IndexMapper}
     */
    IndexMapper mapper();

    /**
     * get monitor
     * @return {@link Monitor <Index>}
     */
    Monitor<Index> monitor();
}
