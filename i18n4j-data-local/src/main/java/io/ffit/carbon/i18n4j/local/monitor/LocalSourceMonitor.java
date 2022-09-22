package io.ffit.carbon.i18n4j.local.monitor;

import io.ffit.carbon.i18n4j.core.codec.SourceCodec;
import io.ffit.carbon.i18n4j.core.monitor.Monitor;
import io.ffit.carbon.i18n4j.core.monitor.listener.MonitorListener;
import io.ffit.carbon.i18n4j.entity.Index;
import io.ffit.carbon.i18n4j.entity.Source;
import io.ffit.carbon.i18n4j.local.datasource.LocalDataSource;

import java.nio.file.Path;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Local Source Monitor
 *
 * @author Lay
 * @date 2022/9/7
 */
public class LocalSourceMonitor implements Monitor<Source> {

    private final LocalDataSource dataSource;
    private final LocalDataSource.Driver driver;
    private final SourceCodec sourceCodec;

    private LocalFileWatcher watcher;
    private MonitorListener<Source> listener;

    private Index cachedIndex;

    public LocalSourceMonitor(LocalDataSource datasource) {
        this.dataSource = datasource;
        this.driver = datasource.driver();
        this.sourceCodec = dataSource.getSourceCodec();
    }

    @Override
    public void start() {
        if (watcher != null) {
            watcher.stop();
        }

        Index index = dataSource.indexProvider().mapper().select();
        if (index == null) {
            return;
        }

        watcher = new LocalFileWatcher();

        // register path
        if (dataSource.isUseTagDirectory()) {
            Set<Path> paths = index.keySet().stream().map(driver::wrapPath).collect(Collectors.toSet());;
            paths.forEach(watcher::register);
        } else {
            watcher.register(Path.of(dataSource.getRoot()));
        }

        // add listeners
        index.forEach((tag, locales) -> locales.forEach(locale -> watcher.addListener(dataSource.getSourceNameFormatter().format(tag, locale), filename -> Optional.ofNullable(listener).ifPresent(l -> l.onChange(new Source(tag, locale, sourceCodec.decode(driver.read(dataSource.getSourceFileName(tag, locale)))))))));
        watcher.start();

        cachedIndex = index;
    }

    @Override
    public void stop() {
        watcher.stop();
        Set<Path> paths = cachedIndex.keySet().stream().map(driver::wrapPath).collect(Collectors.toSet());

        // remove listeners
        cachedIndex.forEach((tag, locales) -> locales.forEach(locale -> watcher.removeListener(dataSource.getSourceNameFormatter().format(tag, locale))));

        // unregister path
        paths.forEach(watcher::unregister);
    }

    @Override
    public void subscribe(MonitorListener<Source> listener) {
        this.listener = listener;
    }
}
