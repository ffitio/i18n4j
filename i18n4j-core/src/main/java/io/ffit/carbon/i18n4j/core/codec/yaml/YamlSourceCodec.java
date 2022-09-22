package io.ffit.carbon.i18n4j.core.codec.yaml;

import io.ffit.carbon.i18n4j.core.codec.FormatConstants;
import io.ffit.carbon.i18n4j.core.codec.SourceCodec;
import io.ffit.carbon.i18n4j.util.MapUtils;
import io.ffit.carbon.i18n4j.util.StringUtils;
import org.yaml.snakeyaml.Yaml;

import java.util.Map;
import java.util.Properties;

/**
 * Yaml Source Codec implementation of {@link SourceCodec}
 *
 * @author Lay
 * @date 2022/9/6
 */
public class YamlSourceCodec implements SourceCodec {
    @Override
    public Properties decode(String data) {
        if (StringUtils.isBlank(data)) {
            return null;
        }

        Yaml yaml = YamlFactory.yaml();
        Map<String, Object> map = MapUtils.flat(yaml.loadAll(data));
        if (map != null) {
            Properties props = new Properties();
            props.putAll(map);
            return props;
        }
        return null;
    }

    @Override
    public String encode(Properties obj) {
        if (obj == null || obj.isEmpty()) {
            return "";
        }

        Yaml yaml = YamlFactory.yaml();
        return yaml.dump(MapUtils.tree(obj));
    }

    @Override
    public String format() {
        return FormatConstants.YAML;
    }
    

}
