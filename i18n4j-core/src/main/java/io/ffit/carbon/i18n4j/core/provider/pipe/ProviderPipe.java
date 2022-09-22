package io.ffit.carbon.i18n4j.core.provider.pipe;

import io.ffit.carbon.i18n4j.core.entity.LocaleProperties;
import io.ffit.carbon.i18n4j.core.provider.IndexProvider;
import io.ffit.carbon.i18n4j.core.provider.SourceProvider;

import java.util.Locale;

/**
 * Provider Pipe
 *
 * @author Lay
 * @date 2022/9/13
 */
public interface ProviderPipe {

    /**
     * load
     */
    void load();

    /**
     * get index provider
     * @return {@link IndexProvider}
     */
    IndexProvider indexProvider();

    /**
     * get source provider
     * @return {@link SourceProvider}
     */
    SourceProvider sourceProvider();

    /**
     * get properties of locale
     * @param locale    Locale
     * @return  Properties
     */
    LocaleProperties getProps(Locale locale);
}
