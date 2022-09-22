package io.ffit.carbon.i18n4j.core.monitor.listener;

/**
 * Monitor Listener
 *
 * @author Lay
 * @date 2022/9/9
 */
public interface MonitorListener<T> {
    /**
     * on change
     * @param data event data
     */
    void onChange(T data);
}
