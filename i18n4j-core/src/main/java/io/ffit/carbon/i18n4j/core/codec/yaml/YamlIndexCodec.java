package io.ffit.carbon.i18n4j.core.codec.yaml;

import io.ffit.carbon.i18n4j.core.codec.FormatConstants;
import io.ffit.carbon.i18n4j.core.codec.IndexCodec;
import io.ffit.carbon.i18n4j.entity.Index;
import io.ffit.carbon.i18n4j.util.CollectionUtils;
import io.ffit.carbon.i18n4j.util.StringUtils;
import org.yaml.snakeyaml.Yaml;

import java.util.List;
import java.util.Map;

/**
 * Yaml Index Codec implementation of {@link IndexCodec}
 *
 * @author Lay
 * @date 2022/9/6
 */
public class YamlIndexCodec implements IndexCodec {

    @Override
    public Index decode(String data) {
        if (StringUtils.isBlank(data)) {
            return null;
        }
        Yaml yaml = YamlFactory.yaml();
        Map<String, List<String>> map = yaml.load(data);
        if (CollectionUtils.isNotEmpty(map)) {
            return new Index(map);
        }
        return null;
    }

    @Override
    public String encode(Index obj) {
        if (obj == null || obj.isEmpty()) {
            return "";
        }

        Map<String, List<String>> map = obj.toStringMap();
        Yaml yaml = YamlFactory.yaml();
        return yaml.dump(map);
    }

    @Override
    public String format() {
        return FormatConstants.YAML;
    }
}
