package io.ffit.carbon.i18n4j.rdb.monitor;

import io.ffit.carbon.i18n4j.core.monitor.Monitor;
import io.ffit.carbon.i18n4j.core.monitor.listener.MonitorListener;
import io.ffit.carbon.i18n4j.entity.Index;
import io.ffit.carbon.i18n4j.rdb.datasource.RDBDataSource;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

/**
 * MySQL Index Monitor
 *
 * @author Lay
 * @date 2022/9/16
 */
public class RDBIndexMonitor implements Monitor<Index>, Runnable {

    private final RDBDataSource dataSource;
    private final AtomicBoolean running;
    private final Thread th;

    private MonitorListener<Index> listener;

    private final AtomicReference<Index> cachedIndex;

    public RDBIndexMonitor(RDBDataSource dataSource) {
        this.dataSource = dataSource;
        this.running = new AtomicBoolean(false);
        this.th = new Thread(this);
        this.cachedIndex = new AtomicReference<>(null);
    }

    @Override
    public void run() {
        while (running.get()) {
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException ignored) {}

            // select index table
            Index index = getIndex();
            if (isIndexChanged(index)) {
                listener.onChange(index);
            }
        }
    }

    private boolean isIndexChanged(Index index) {
        Index cached = cachedIndex.get();
        if (index != null && index.equals(cached)) {
            return false;
        }
        return cachedIndex.compareAndSet(cached, index);
    }

    private Index getIndex() {
        return dataSource.indexProvider().mapper().select();
    }

    @Override
    public void start() {
        if (running.compareAndSet(false, true)) {
            cachedIndex.set(getIndex());
            th.start();
        }
    }

    @Override
    public void stop() {
        if (running.compareAndSet(true, false)) {
            th.interrupt();
        }
    }

    @Override
    public void subscribe(MonitorListener<Index> listener) {
        this.listener = listener;
    }
}
