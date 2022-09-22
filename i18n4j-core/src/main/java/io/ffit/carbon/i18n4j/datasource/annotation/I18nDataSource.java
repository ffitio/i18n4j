package io.ffit.carbon.i18n4j.datasource.annotation;

import java.lang.annotation.*;

/**
 * DataSource Annotation
 *
 * @author Lay
 * @date 2022/9/14
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
public @interface I18nDataSource {
    String value();
}
