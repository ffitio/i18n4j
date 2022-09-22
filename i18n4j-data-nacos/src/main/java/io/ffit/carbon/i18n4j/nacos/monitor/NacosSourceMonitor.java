package io.ffit.carbon.i18n4j.nacos.monitor;

import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.api.exception.NacosException;
import io.ffit.carbon.i18n4j.core.mapper.IndexMapper;
import io.ffit.carbon.i18n4j.core.monitor.Monitor;
import io.ffit.carbon.i18n4j.core.monitor.listener.MonitorListener;
import io.ffit.carbon.i18n4j.entity.Index;
import io.ffit.carbon.i18n4j.entity.Source;
import io.ffit.carbon.i18n4j.nacos.datasource.NacosDataSource;
import io.ffit.carbon.i18n4j.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Nacos Source Monitor
 *
 * @author Lay
 * @date 2022/9/14
 */
public class NacosSourceMonitor implements Monitor<Source> {

    private final NacosDataSource dataSource;

    private final ConfigService configService;

    private final IndexMapper indexMapper;

    private MonitorListener<Source> listener;

    private List<NacosSourceListener> sourceListeners;

    private final AtomicBoolean running;

    public NacosSourceMonitor(NacosDataSource dataSource) {
        this.dataSource = dataSource;
        this.configService = dataSource.configService();
        this.running = new AtomicBoolean(false);
        this.indexMapper = dataSource.indexProvider().mapper();
    }

    @Override
    public void start() {
        Index index = indexMapper.select();
        if (index == null) {
            return;
        }

        if (running.compareAndSet(false, true)) {
            sourceListeners = new ArrayList<>();
            index.forEach((tag, locales) -> locales.forEach(locale -> {
                NacosSourceListener sourceListener = new NacosSourceListener(dataSource.getSourceNameFormatter().format(tag, locale), dataSource.getGroup(), new Listener() {
                    @Override
                    public Executor getExecutor() {
                        return null;
                    }

                    @Override
                    public void receiveConfigInfo(String configInfo) {
                        if (StringUtils.isNotBlank(configInfo)) {
                            Source source = new Source(tag, locale, dataSource.getSourceCodec().decode(configInfo));
                            Optional.ofNullable(listener).ifPresent(l -> l.onChange(source));
                        }
                    }
                });
                try {
                    sourceListener.add(configService);
                    sourceListeners.add(sourceListener);
                } catch (NacosException e) {
                    throw new RuntimeException(e);
                }
            }));
        }
    }

    @Override
    public void stop() {
        if (running.compareAndSet(true, false)) {
            sourceListeners.forEach(sourceListener -> sourceListener.remove(configService));
        }
    }

    @Override
    public void subscribe(MonitorListener<Source> listener) {
        this.listener = listener;
    }

    private static class NacosSourceListener {
        private final String dataId;
        private final String group;
        private final Listener listener;

        public NacosSourceListener(String dataId, String group, Listener listener) {
            this.dataId = dataId;
            this.group = group;
            this.listener = listener;
        }

        public void add(ConfigService configService) throws NacosException {
            configService.addListener(dataId, group, listener);
        }

        public void remove(ConfigService configService) {
            configService.removeListener(dataId, group, listener);
        }
    }
}
