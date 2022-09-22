package io.ffit.carbon.i18n4j.core.codec.yaml;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

/**
 * YAML Options
 *
 * @author Lay
 * @date 2022/9/6
 */
public class YamlFactory {
    public final static DumperOptions OPTIONS = new DumperOptions();

    static{
        OPTIONS.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        OPTIONS.setDefaultScalarStyle(DumperOptions.ScalarStyle.PLAIN);
        OPTIONS.setIndicatorIndent(2);
        OPTIONS.setIndent(4);
        OPTIONS.setPrettyFlow(true);
    }

    public static Yaml yaml() {
        return new Yaml(OPTIONS);
    }
}
