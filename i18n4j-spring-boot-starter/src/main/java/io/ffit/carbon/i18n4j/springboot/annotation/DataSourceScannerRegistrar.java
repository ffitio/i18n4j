package io.ffit.carbon.i18n4j.springboot.annotation;

import io.ffit.carbon.i18n4j.springboot.autoconfigure.DataSourceScanner;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Message Config Scanner Registrar
 *
 * @author Lay
 */
public class DataSourceScannerRegistrar implements ImportBeanDefinitionRegistrar {

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        AnnotationAttributes attributes = AnnotationAttributes.fromMap(importingClassMetadata.getAnnotationAttributes(DataSourceScan.class.getName()));
        if (attributes != null) {
            String[] annoPackages = (String[]) attributes.get("basePackages");
            if (annoPackages != null && annoPackages.length > 0) {
                DataSourceScanner.addPackages(Arrays.stream(annoPackages).collect(Collectors.toSet()));
            }
        }
    }
}
