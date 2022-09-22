package io.ffit.carbon.i18n4j.datasource;

import io.ffit.carbon.i18n4j.core.codec.FormatConstants;
import io.ffit.carbon.i18n4j.core.codec.IndexCodec;
import io.ffit.carbon.i18n4j.core.codec.SourceCodec;
import io.ffit.carbon.i18n4j.core.codec.yaml.YamlIndexCodec;
import io.ffit.carbon.i18n4j.core.codec.yaml.YamlSourceCodec;
import io.ffit.carbon.i18n4j.core.formatter.DefaultSourceNameFormatter;
import io.ffit.carbon.i18n4j.core.formatter.SourceNameFormatter;
import io.ffit.carbon.i18n4j.core.provider.IndexProvider;
import io.ffit.carbon.i18n4j.core.provider.SourceProvider;

import java.util.Objects;
import java.util.Optional;
import java.util.Properties;

/**
 * Abstract implementation of {@link DataSource}
 *
 * @author Lay
 * @date 2022/9/13
 */
public abstract class AbstractDataSource implements DataSource {

    protected final Properties props;

    public static final String FORMAT = "format";

    public static final String SOURCE_NAME_FORMATTER = "sourceNameFormatter";
    public static final String INDEX_CODEC = "indexCodec";
    public static final String SOURCE_CODEC = "sourceCodec";

    /**
     * source format
     */
    protected String format;

    /**
     * source name formatter
     */
    protected SourceNameFormatter sourceNameFormatter;

    /**
     * index codec
     */
    protected IndexCodec indexCodec;

    /**
     * source codec
     */
    protected SourceCodec sourceCodec;

    protected IndexProvider indexProvider;
    protected SourceProvider sourceProvider;

    public AbstractDataSource(Properties props) {
        this.props = Objects.requireNonNullElseGet(props, Properties::new);

        // parse props
        this.format = Optional.ofNullable(this.props.getProperty(FORMAT)).orElse(FormatConstants.YAML);
        this.sourceNameFormatter = parseSourceNameFormatter(this.props.get(SOURCE_NAME_FORMATTER));
        this.indexCodec = parseIndexCodec(this.props.get(INDEX_CODEC));
        this.sourceCodec = parseSourceCodec(this.props.get(SOURCE_CODEC));
        parseProps();

        // new IndexProvider
        this.indexProvider = createIndexProvider();

        // new SourceProvider
        this.sourceProvider = createSourceProvider();
    }

    protected DataSourceProperties getDataSourceProps() {
        DataSourceProperties props = new DataSourceProperties();
        props.putAll(this.props);
        props.remove(FORMAT);
        props.remove(SOURCE_NAME_FORMATTER);
        props.remove(INDEX_CODEC);
        props.remove(SOURCE_CODEC);
        return props;
    }

    @SuppressWarnings("unchecked")
    private SourceNameFormatter parseSourceNameFormatter(Object obj) {
        if (obj != null) {
            if (obj instanceof SourceNameFormatter) {
                return (SourceNameFormatter) obj;
            } else if (obj instanceof Class && ((Class<?>) obj).isAssignableFrom(SourceNameFormatter.class)) {
                return createSourceNameFormatter((Class<SourceNameFormatter>)obj);
            } else {
                try {
                    Class<SourceNameFormatter> cls = (Class<SourceNameFormatter>) Class.forName(obj.toString());
                    return createSourceNameFormatter(cls);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return new DefaultSourceNameFormatter(format);
    }

    private SourceNameFormatter createSourceNameFormatter(Class<SourceNameFormatter> cls) {
        try {
            return cls.getDeclaredConstructor(String.class).newInstance(format);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    private IndexCodec parseIndexCodec(Object obj) {
        if (obj != null) {
            if (obj instanceof IndexCodec) {
                return (IndexCodec) obj;
            } else if (obj instanceof Class && ((Class<IndexCodec>) obj).isAssignableFrom(IndexCodec.class)) {
                try {
                    return ((Class<IndexCodec>) obj).getDeclaredConstructor().newInstance();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            } else {
                try {
                    Class<IndexCodec> cls = (Class<IndexCodec>) Class.forName(obj.toString());
                    return cls.getDeclaredConstructor().newInstance();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return new YamlIndexCodec();
    }

    @SuppressWarnings("unchecked")
    private SourceCodec parseSourceCodec(Object obj) {
        if (obj != null) {
            if (obj instanceof SourceCodec) {
                return (SourceCodec) obj;
            } else if (obj instanceof Class && ((Class<SourceCodec>) obj).isAssignableFrom(IndexCodec.class)) {
                try {
                    return ((Class<SourceCodec>) obj).getDeclaredConstructor().newInstance();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            } else {
                try {
                    Class<SourceCodec> cls = (Class<SourceCodec>) Class.forName(obj.toString());
                    return cls.getDeclaredConstructor().newInstance();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return new YamlSourceCodec();
    }

    protected abstract void parseProps();

    protected abstract IndexProvider createIndexProvider();
    protected abstract SourceProvider createSourceProvider();

    public SourceNameFormatter getSourceNameFormatter() {
        return sourceNameFormatter;
    }

    public IndexCodec getIndexCodec() {
        return indexCodec;
    }

    public SourceCodec getSourceCodec() {
        return sourceCodec;
    }

    @Override
    public IndexProvider indexProvider() {
        return indexProvider;
    }

    @Override
    public SourceProvider sourceProvider() {
        return sourceProvider;
    }
}
