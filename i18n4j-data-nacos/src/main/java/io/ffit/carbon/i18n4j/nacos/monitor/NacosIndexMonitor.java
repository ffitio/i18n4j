package io.ffit.carbon.i18n4j.nacos.monitor;

import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.api.exception.NacosException;
import io.ffit.carbon.i18n4j.core.monitor.Monitor;
import io.ffit.carbon.i18n4j.core.monitor.listener.MonitorListener;
import io.ffit.carbon.i18n4j.entity.Index;
import io.ffit.carbon.i18n4j.nacos.datasource.NacosDataSource;

import java.util.Optional;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Nacos Index Monitor
 *
 * @author Lay
 * @date 2022/9/14
 */
public class NacosIndexMonitor implements Monitor<Index> {

    private final NacosDataSource dataSource;

    private final ConfigService configService;

    private MonitorListener<Index> listener;

    private final AtomicBoolean running;

    private final Listener nacosListener;

    public NacosIndexMonitor(NacosDataSource dataSource) {
        this.dataSource = dataSource;
        this.configService = dataSource.configService();
        this.running = new AtomicBoolean(false);
        this.nacosListener = new Listener() {
            @Override
            public Executor getExecutor() {
                return null;
            }

            @Override
            public void receiveConfigInfo(String configInfo) {
                Optional.ofNullable(listener).ifPresent(l -> l.onChange(dataSource.getIndexCodec().decode(configInfo)));
            }
        };
    }

    @Override
    public void start() {
        if (running.compareAndSet(false, true)) {
            try {
                configService.addListener(dataSource.getIndex(), dataSource.getGroup(), nacosListener);
            } catch (NacosException e) {
                running.set(false);
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void stop() {
        if (running.compareAndSet(true, false)) {
            configService.removeListener(dataSource.getIndex(), dataSource.getGroup(), nacosListener);
        }
    }

    @Override
    public void subscribe(MonitorListener<Index> listener) {
        this.listener = listener;
    }
}
