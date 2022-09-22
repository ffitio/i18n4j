package io.ffit.carbon.i18n4j.entity;

import io.ffit.carbon.i18n4j.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Index Entity
 *
 * @author Lay
 * @date 2022/9/6
 */
public class Index extends HashMap<String, Set<Locale>> {

    public Index() {}

    public Index(Map<String, List<String>> map) {
        map.forEach((tag, locales) -> {
            put(tag, locales.stream().map(Locale::forLanguageTag).collect(Collectors.toSet()));
        });
    }

    @Override
    public Set<Locale> put(String tag, Set<Locale> locales) {
        if (StringUtils.isEmpty(tag)) {
            throw new RuntimeException("tag name cannot be empty");
        }

        if (locales == null || locales.isEmpty()) {
            throw new RuntimeException("locales of the tag cannot be empty");
        }
        SortedSet<Locale> sortedLocales = new TreeSet<>(Comparator.comparing(Locale::toLanguageTag));
        sortedLocales.addAll(locales);
        return super.put(tag, sortedLocales);
    }

    @Override
    public void putAll(Map<? extends String, ? extends Set<Locale>> m) {
        m.forEach(this::put);
    }

    public Map<String, List<String>> toStringMap() {
        Map<String, List<String>> map = new HashMap<>();
        forEach((tag, locales) -> map.put(tag, locales.stream().map(Locale::toLanguageTag).collect(Collectors.toList())));
        return map;
    }

    public Map<String, Set<Locale>> toMap() {
        return this;
    }
}
