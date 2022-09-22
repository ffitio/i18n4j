package io.ffit.carbon.i18n4j.local.monitor;

import io.ffit.carbon.i18n4j.core.codec.IndexCodec;
import io.ffit.carbon.i18n4j.core.monitor.Monitor;
import io.ffit.carbon.i18n4j.core.monitor.listener.MonitorListener;
import io.ffit.carbon.i18n4j.entity.Index;
import io.ffit.carbon.i18n4j.local.datasource.LocalDataSource;

import java.nio.file.Path;
import java.util.Optional;

/**
 * Local Index Monitor
 *
 * @author Lay
 * @date 2022/9/7
 */
public class LocalIndexMonitor implements Monitor<Index> {

    private final IndexCodec indexCodec;

    private final LocalDataSource dataSource;

    private final LocalDataSource.Driver driver;

    private final LocalFileWatcher watcher;

    private MonitorListener<Index> listener;

    public LocalIndexMonitor(LocalDataSource dataSource) {
        this.indexCodec = dataSource.getIndexCodec();
        this.driver = dataSource.driver();
        this.watcher = new LocalFileWatcher();
        this.dataSource = dataSource;
    }

    @Override
    public void start() {
        // register path
        watcher.register(Path.of(dataSource.getRoot()));

        // add listener
        watcher.addListener(dataSource.getIndex(), filename -> Optional.ofNullable(listener).ifPresent(l -> l.onChange(indexCodec.decode(driver.read(filename)))));
        watcher.start();
    }

    @Override
    public void stop() {
        watcher.stop();

        // remove listener
        watcher.removeListener(dataSource.getIndex());

        // unregister path
        watcher.unregister(Path.of(dataSource.getRoot()));
    }

    @Override
    public void subscribe(MonitorListener<Index> listener) {
        this.listener = listener;
    }
}
