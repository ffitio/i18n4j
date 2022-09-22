package io.ffit.carbon.i18n4j.rdb.monitor;

import io.ffit.carbon.i18n4j.core.monitor.Monitor;
import io.ffit.carbon.i18n4j.core.monitor.listener.MonitorListener;
import io.ffit.carbon.i18n4j.entity.Index;
import io.ffit.carbon.i18n4j.entity.Source;
import io.ffit.carbon.i18n4j.rdb.datasource.RDBDataSource;
import io.ffit.carbon.i18n4j.rdb.datasource.RDBDriver;
import io.ffit.carbon.i18n4j.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * MySQL Source Monitor
 *
 * @author Lay
 * @date 2022/9/16
 */
public class RDBSourceMonitor implements Monitor<Source>, Runnable {

    private final RDBDataSource dataSource;
    private final RDBDriver driver;
    private final Thread th;
    private final AtomicBoolean running;
    private final Map<SourceKey, Long> sourceUpdateTimeMap;
    private MonitorListener<Source> listener;

    public RDBSourceMonitor(RDBDataSource dataSource) {
        this.dataSource = dataSource;
        this.driver = dataSource.driver();
        this.th = new Thread(this);
        this.running = new AtomicBoolean(false);
        this.sourceUpdateTimeMap = new ConcurrentHashMap<>();
    }

    @Override
    public void run() {
        while (running.get()) {
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException ignored) {}

            Map<String, Set<Locale>> cachedIndex = sourceKeysToIndex(sourceUpdateTimeMap.keySet());
            List<Map<String, Object>> list = getSources(cachedIndex, false);

            Set<SourceKey> sourceKeysUpdated = new HashSet<>();
            list.forEach(m -> {
                SourceKey key = getSourceKey(m);
                if (Long.parseLong(m.get("update_time").toString()) != sourceUpdateTimeMap.get(key)) {
                    sourceKeysUpdated.add(key);
                }
            });

            if (CollectionUtils.isNotEmpty(sourceKeysUpdated)) {
                List<Map<String, Object>> sources = getSources(sourceKeysToIndex(sourceKeysUpdated), true);
                sources.forEach(m -> {
                    Source source = new Source(m.get("tag").toString(), Locale.forLanguageTag(m.get("locale").toString()), dataSource.getSourceCodec().decode(m.get("props").toString()));
                    Optional.ofNullable(listener).ifPresent(l -> l.onChange(source));
                });
            }
        }
    }

    private Map<String, Set<Locale>> sourceKeysToIndex(Set<SourceKey> sourceKeys) {
        Map<String, Set<Locale>> map = new HashMap<>();
        sourceKeys.forEach(key -> {
            Set<Locale> locales = map.computeIfAbsent(key.tag, k -> new HashSet<>());
            locales.add(Locale.forLanguageTag(key.locale));
        });
        return map;
    }

    private void addSources(Index index) {
        sourceUpdateTimeMap.clear();
        List<Map<String, Object>> res = getSources(index.toMap(), false);
        res.forEach(m -> sourceUpdateTimeMap.put(getSourceKey(m), Long.parseLong(m.get("update_time").toString())));
    }

    private SourceKey getSourceKey(Map<String, Object> record) {
        return new SourceKey(record.get("tag").toString(), record.get("locale").toString());
    }

    private List<Map<String, Object>> getSources(Map<String, Set<Locale>> map, boolean withProps) {
        StringBuilder where = new StringBuilder();
        List<Map.Entry<String, Set<Locale>>> list = new ArrayList<>(map.entrySet());
        int size = list.size();
        for (int i = 0; i < size; i++) {
            Map.Entry<String, Set<Locale>> entry = list.get(i);
            where.append("(tag = '").append(entry.getKey()).append("' AND FIND_IN_SET(locale,'").append(String.join(",", entry.getValue().stream().map(Locale::toLanguageTag).toArray(String[]::new))).append("'))");
            if (i != size - 1) {
                where.append(" OR ");
            }
        }

        String columns = "tag, locale, update_time";
        if (withProps) {
            columns += ", props";
        }

        String sql = String.format("SELECT %s FROM %s WHERE %s", columns, dataSource.getSourceTableName(), where);
        return driver.queryAll(sql, null);
    }

    @Override
    public void start() {
        Index index = dataSource.indexProvider().mapper().select();
        if (index == null) {
            return;
        }

        if (running.compareAndSet(false, true)) {
            addSources(index);
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
    public void subscribe(MonitorListener<Source> listener) {
        this.listener = listener;
    }

    private static class SourceKey {
        private String tag;
        private String locale;

        public SourceKey(String tag, String locale) {
            this.tag = tag;
            this.locale = locale;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof SourceKey)) return false;
            SourceKey sourceKey = (SourceKey) o;
            return Objects.equals(getTag(), sourceKey.getTag()) && Objects.equals(getLocale(), sourceKey.getLocale());
        }

        @Override
        public int hashCode() {
            return (tag + "#" + locale).hashCode();
        }

        public String getTag() {
            return tag;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }

        public String getLocale() {
            return locale;
        }

        public void setLocale(String locale) {
            this.locale = locale;
        }
    }
}
