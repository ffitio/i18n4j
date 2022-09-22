package io.ffit.carbon.i18n4j.core.monitor;

import io.ffit.carbon.i18n4j.core.monitor.listener.MonitorListener;

/**
 * Index Monitor
 *
 * @author Lay
 * @date 2022/9/7
 */
public interface Monitor<T> {
    /**
     * start index monitor
     */
    void start();

    /**
     * stop index monitor
     */
    void stop();

    /**
     * subscribe
     * @param listener {@link MonitorListener}
     */
    void subscribe(MonitorListener<T> listener);
}
