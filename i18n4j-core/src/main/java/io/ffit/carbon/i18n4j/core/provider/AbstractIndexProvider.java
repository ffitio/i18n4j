package io.ffit.carbon.i18n4j.core.provider;

import io.ffit.carbon.i18n4j.core.mapper.IndexMapper;
import io.ffit.carbon.i18n4j.core.monitor.Monitor;
import io.ffit.carbon.i18n4j.entity.Index;

/**
 * Abstract implementation of {@link IndexProvider}
 *
 * @author Lay
 * @date 2022/9/14
 */
public abstract class AbstractIndexProvider implements IndexProvider {
    protected final IndexMapper mapper;
    protected final Monitor<Index> monitor;

    public AbstractIndexProvider(IndexMapper mapper, Monitor<Index> monitor) {
        if (mapper == null) {
            throw new RuntimeException("IndexMapper cannot be null");
        }

        this.mapper = mapper;
        this.monitor = monitor;
    }

    @Override
    public IndexMapper mapper() {
        return mapper;
    }

    @Override
    public Monitor<Index> monitor() {
        return monitor;
    }
}
