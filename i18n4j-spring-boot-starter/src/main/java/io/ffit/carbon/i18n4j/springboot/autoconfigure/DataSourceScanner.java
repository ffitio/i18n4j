package io.ffit.carbon.i18n4j.springboot.autoconfigure;

import io.ffit.carbon.i18n4j.datasource.AbstractDataSource;
import io.ffit.carbon.i18n4j.datasource.DataSource;
import io.ffit.carbon.i18n4j.datasource.annotation.I18nDataSource;
import io.ffit.carbon.i18n4j.springboot.exception.DataSourceNotFoundException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.type.ClassMetadata;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.TypeFilter;

import java.util.*;

/**
 * Data Source Scanner
 *
 * @author Lay
 * @date 2022/9/14
 */
public class DataSourceScanner {
    private static final Set<String> scanPackages = new HashSet<>();
    public static void addPackages(Set<String> packages) {
        scanPackages.addAll(packages);
    }

    public static DataSource scan(String dataType, Properties props) {
        scanPackages.add(String.format("io.ffit.carbon.i18n4j.%s", dataType));

        List<BeanDefinition> beans = getBeanDefinitions();

        try {
            for (BeanDefinition bean : beans) {
                I18nDataSource dataSourceType = AnnotationUtils.findAnnotation(Class.forName(bean.getBeanClassName()), I18nDataSource.class);
                if (dataSourceType != null && dataType.equals(dataSourceType.value())) {
                    return (DataSource) Class.forName(bean.getBeanClassName()).getDeclaredConstructor(Properties.class).newInstance(props);
                }
            }
        } catch (Exception e) {
            throw new DataSourceNotFoundException(dataType, e);
        }
        throw new DataSourceNotFoundException(dataType, null);
    }

    public static List<BeanDefinition> getBeanDefinitions()  {
        LinkedHashSet<BeanDefinition> candidateComponents = new LinkedHashSet<>();
        ClassPathScanningCandidateComponentProvider pathScanningCandidateComponentProvider = new ClassPathScanningCandidateComponentProvider(false);
        pathScanningCandidateComponentProvider.addIncludeFilter(new DataSourceClassFilter());
        for (String basePackage : scanPackages) {
            candidateComponents.addAll(pathScanningCandidateComponentProvider.findCandidateComponents(basePackage));
        }

        return new ArrayList<>(candidateComponents);
    }

    static class DataSourceClassFilter implements TypeFilter {
        @Override
        public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory) {
            ClassMetadata metadata = metadataReader.getClassMetadata();
            return AbstractDataSource.class.getName().equals(metadata.getSuperClassName());
        }
    }
}
