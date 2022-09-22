package io.ffit.carbon.i18n4j.core.provider;

import io.ffit.carbon.i18n4j.core.mapper.SourceMapper;
import io.ffit.carbon.i18n4j.core.monitor.Monitor;
import io.ffit.carbon.i18n4j.entity.Source;

/**
 * Abstract implementation of {@link SourceProvider}
 *
 * @author Lay
 * @date 2022/9/14
 */
public abstract class AbstractSourceProvider implements SourceProvider {

    protected final SourceMapper mapper;
    protected final Monitor<Source> monitor;

    public AbstractSourceProvider(SourceMapper mapper, Monitor<Source> monitor) {
        if (mapper == null) {
            throw new RuntimeException("SourceMapper cannot be null");
        }

        this.mapper = mapper;
        this.monitor = monitor;
    }

    @Override
    public SourceMapper mapper() {
        return mapper;
    }

    @Override
    public Monitor<Source> monitor() {
        return monitor;
    }
}
