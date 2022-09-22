package io.ffit.carbon.i18n4j.entity;

import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.Properties;

/**
 * Source Entity
 *
 * @author Lay
 * @date 2022/9/6
 */
public class Source {
    /**
     * tag name
     */
    private String tag;

    /**
     * locale
     */
    private Locale locale;

    /**
     * properties
     */
    private Properties props;

    public Source(String tag, Locale locale, Properties props) {
        if (tag == null || tag.isEmpty()) {
            throw new RuntimeException("tag name cannot be empty");
        }

        if (locale == null) {
            throw new RuntimeException("locale cannot be null");
        }

        this.tag = tag;
        this.locale = locale;
        this.props = Optional.ofNullable(props).orElse(new Properties());
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public Properties getProps() {
        return props;
    }

    public void setProps(Properties props) {
        this.props = props;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Source)) return false;
        Source source = (Source) o;
        return tag.equals(source.getTag()) && locale.equals(source.getLocale()) && getProps().equals(props);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTag(), getLocale(), getProps());
    }
}
