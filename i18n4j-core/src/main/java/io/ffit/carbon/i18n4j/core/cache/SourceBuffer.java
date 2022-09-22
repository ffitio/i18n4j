package io.ffit.carbon.i18n4j.core.cache;

import io.ffit.carbon.i18n4j.core.entity.LocaleProperties;
import io.ffit.carbon.i18n4j.core.formatter.DefaultMessageFormatter;
import io.ffit.carbon.i18n4j.core.formatter.MessageFormatter;
import io.ffit.carbon.i18n4j.entity.Source;
import io.ffit.carbon.i18n4j.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Source Buffer
 *
 * @author Lay
 * @date 2022/9/13
 */
public class SourceBuffer {
    private final Stack<Source> sourceStack;

    private final AtomicBoolean popping;

    private final SourceCache sourceCache;

    private final Class<? extends MessageFormatter> formatterClass;

    public SourceBuffer(Class<? extends MessageFormatter> formatterClass) {
        if (formatterClass == null) {
            this.formatterClass = DefaultMessageFormatter.class;
        } else {
            this.formatterClass = formatterClass;
        }
        sourceStack = new Stack<>();
        popping = new AtomicBoolean(false);
        sourceCache = new SourceCache();
    }

    public void push(Source source) {
        sourceStack.push(source);
    }

    public void pushAll(List<Source> sources) {
        for (Source source : sources) {
            sourceStack.push(source);
        }
    }

    public void flush(boolean replace) {
        if (popping.compareAndSet(false, true)) {
            List<Source> sources = new ArrayList<>();
            while (!sourceStack.isEmpty()) {
                sources.add(sourceStack.pop());
            }
            popping.set(false);
            buildCache(replace, sources);
        }
    }

    public LocaleProperties cacheOf(Locale locale) {
        return sourceCache.of(locale);
    }

    private synchronized void buildCache(boolean replace, List<Source> sources) {
        if (CollectionUtils.isEmpty(sources)) {
            return;
        }

        Map<Locale, LocaleProperties> cache = process(sources, formatterClass);
        if (replace) {
            sourceCache.replace(cache);
        } else {
            sourceCache.merge(cache);
        }
    }

    private static Map<Locale, LocaleProperties> process(List<Source> sources, Class<? extends MessageFormatter> formatterClass) {
        Map<Locale, LocaleProperties> mergedProps = new HashMap<>();
        for (Source s : sources) {
            mergedProps.merge(s.getLocale(), new LocaleProperties(s.getProps(), s.getLocale(), formatterClass), (o, n) -> {
                o.putAll(n);
                return o;
            });
        }
        return mergedProps;
    }
}
