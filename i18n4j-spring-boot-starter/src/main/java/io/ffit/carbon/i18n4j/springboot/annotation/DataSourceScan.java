package io.ffit.carbon.i18n4j.springboot.annotation;

import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * Data Source Scan
 *
 * @author Lay
 * @date 2022/9/14
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
@Import(DataSourceScannerRegistrar.class)
public @interface DataSourceScan {
    @AliasFor("basePackages")
    String[] value() default {};

    @AliasFor("value")
    String[] basePackages() default {};
}
