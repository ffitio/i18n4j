package io.ffit.carbon.i18n4j.core.provider.pipe;

import io.ffit.carbon.i18n4j.config.I18nConfig;
import io.ffit.carbon.i18n4j.core.cache.SourceBuffer;
import io.ffit.carbon.i18n4j.core.entity.LocaleProperties;
import io.ffit.carbon.i18n4j.core.monitor.Monitor;
import io.ffit.carbon.i18n4j.core.provider.IndexProvider;
import io.ffit.carbon.i18n4j.core.provider.SourceProvider;
import io.ffit.carbon.i18n4j.entity.Index;
import io.ffit.carbon.i18n4j.entity.Source;
import io.ffit.carbon.i18n4j.util.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

/**
 * Implementation of {@link ProviderPipe}
 *
 * @author Lay
 * @date 2022/9/11
 */
public class ProviderPipeImpl implements ProviderPipe {
    private static final Logger logger = LoggerFactory.getLogger(ProviderPipe.class);

    protected final I18nConfig config;
    protected final IndexProvider indexProvider;
    protected final SourceProvider sourceProvider;

    protected final SourceBuffer buffer;

    public ProviderPipeImpl(I18nConfig config) {
        this.config = config;
        this.indexProvider = config.getDataSourcePack().getIndex().indexProvider();
        this.sourceProvider = config.getDataSourcePack().getSource().sourceProvider();
        this.buffer = new SourceBuffer(config.getMessageFormatterClass());

        // init
        init();
    }

    private void init() {
        // init monitor
        if (config.isAutoReload()) {
            Optional.ofNullable(indexProvider.monitor()).ifPresent(m -> m.subscribe(this::onIndexChanged));
            Optional.ofNullable(sourceProvider.monitor()).ifPresent(m -> m.subscribe(this::onSourceChanged));
        }

        // load
        if (config.isAutoLoad()) {
            load();
        }
    }

    @Override
    public void load() {
        load(this.indexProvider.mapper().select());
    }

    @Override
    public IndexProvider indexProvider() {
        return indexProvider;
    }

    @Override
    public SourceProvider sourceProvider() {
        return sourceProvider;
    }

    private void load(Index index) {
        if (index == null || index.isEmpty()) {
            logger.warn("no index loaded");
            return;
        }

        List<Source> sources = sourceProvider.mapper().selectAll(index.toMap());
        if (CollectionUtils.isEmpty(sources)) {
            logger.warn("no source loaded");
            return;
        }

        // push sources to stack
        buffer.pushAll(sources);
        buffer.flush(true);

        watch();
    }

    private void watch() {
        if (config.isAutoReload()) {
            Optional.ofNullable(indexProvider.monitor()).ifPresent(Monitor::start);
            Optional.ofNullable(sourceProvider.monitor()).ifPresent(Monitor::start);
        }
    }

    protected void onIndexChanged(Index index) {
        load(index);
    }

    protected void onSourceChanged(Source source) {
        buffer.push(source);
        buffer.flush(false);
    }

    @Override
    public LocaleProperties getProps(Locale locale) {
        return buffer.cacheOf(locale);
    }
}
