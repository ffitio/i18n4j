package io.ffit.carbon.i18n4j.local.datasource;

import io.ffit.carbon.i18n4j.core.provider.IndexProvider;
import io.ffit.carbon.i18n4j.core.provider.SourceProvider;
import io.ffit.carbon.i18n4j.datasource.AbstractDataSource;
import io.ffit.carbon.i18n4j.datasource.annotation.I18nDataSource;
import io.ffit.carbon.i18n4j.local.provider.LocalIndexProvider;
import io.ffit.carbon.i18n4j.local.provider.LocalSourceProvider;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;
import java.util.Optional;
import java.util.Properties;

/**
 * Local Data Source
 *
 * @author Lay
 * @date 2022/9/14
 */
@I18nDataSource(LocalDataSource.DATA_TYPE)
public class LocalDataSource extends AbstractDataSource {

    public static final String DATA_TYPE = "local";

    /**
     * root
     */
    public static final String ROOT = "root";

    /**
     * index
     */
    public static final String INDEX = "index";

    /**
     * default root path
     */
    public static final String DEFAULT_ROOT = "classpath:i18n/";

    /**
     * default index filename
     */
    public static final String DEFAULT_INDEX = "index.yaml";

    /**
     * root path
     */
    private String root;

    /**
     * index filename
     */
    private String index;

    /**
     * source put in tag directory
     */
    private boolean useTagDirectory;

    private Driver driver;

    public LocalDataSource(Properties props) {
        super(props);
    }

    @Override
    protected void parseProps() {
        this.root = props.getProperty(ROOT, DEFAULT_ROOT);
        this.index = props.getProperty(INDEX, DEFAULT_INDEX);
        this.useTagDirectory = Optional.ofNullable(props.get("useTagDirectory")).map(t -> (boolean)t).orElse(true);
        this.driver = new Driver(root);
    }

    @Override
    protected IndexProvider createIndexProvider() {
        return new LocalIndexProvider(this);
    }

    @Override
    protected SourceProvider createSourceProvider() {
        return new LocalSourceProvider(this);
    }

    public Driver driver() {
        return driver;
    }

    public String getRoot() {
        return root;
    }

    public String getIndex() {
        return index;
    }

    public boolean isUseTagDirectory() {
        return useTagDirectory;
    }

    public String getSourceFileName(String tag, Locale locale) {
        String filename = sourceNameFormatter.format(tag, locale);
        return useTagDirectory ? Path.of(tag, filename).toString() : filename;
    }

    public static class Driver {

        private final Path root;

        private Driver(String root) {
            if (root.startsWith("classpath:")) {
                String path = root.substring("classpath:".length());
                URL resourcePath = this.getClass().getClassLoader().getResource(path);
                if (resourcePath == null) {
                    throw new RuntimeException("resource path for i18n not found: " + root);
                }
                this.root = Path.of(resourcePath.getPath());
            } else {
                this.root = Path.of(root);
            }
        }

        public void write(String filename, String data) {
            try (FileWriter writer = new FileWriter(wrapPath(filename).toString())) {
                writer.write(data);
                writer.flush();

            } catch (IOException e) {
                throw new RuntimeException("file write failed:", e);
            }
        }

        public boolean create(String filename) {
            try {
                Path path = wrapPath(filename);
                // make parent directories
                path.getParent().toFile().mkdirs();
                // create file
                return path.toFile().createNewFile();
            } catch (IOException e) {
                throw new RuntimeException("file create failed:", e);
            }
        }

        public String read(String filename) {
            try {
                return Files.readString(wrapPath(filename));
            } catch (Exception e) {
                return null;
            }
        }

        public int del(String filename) {
            Path path = wrapPath(filename);
            if (Files.exists(path)) {
                try {
                    Files.delete(path);
                    return 1;
                } catch (IOException e) {
                    return 0;
                }
            }
            return 0;
        }

        public boolean exists(String filename) {
            return Files.exists(wrapPath(filename));
        }

        public Path wrapPath(String filename) {
            return root.resolve(filename);
        }
    }
}
