package io.ffit.carbon.i18n4j.core.cache;

import io.ffit.carbon.i18n4j.core.entity.LocaleProperties;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Source Cache
 *
 * @author Lay
 * @date 2022/9/13
 */
public class SourceCache {
    private Map<Locale, LocaleProperties> cache = new HashMap<>();

    public void replace(Map<Locale, LocaleProperties> target) {
        Map<Locale, LocaleProperties> old = cache;
        cache = target;
        old.clear();
    }

    public void merge(Map<Locale, LocaleProperties> target) {
        cache.putAll(target);
    }

    public LocaleProperties of(Locale locale) {
        return cache.get(locale);
    }
}
