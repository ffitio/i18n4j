package io.ffit.carbon.i18n4j.springboot.exception;

/**
 * provider config not found
 *
 * @author Lay
 * @since 1.0.0
 */
public class DataSourceNotFoundException extends RuntimeException {
    public DataSourceNotFoundException(String type, Throwable e) {
        super(String.format("cannot find DataSource of type %s", type), e);
    }
}
