package io.ffit.carbon.i18n4j.core.codec.properties;

import io.ffit.carbon.i18n4j.core.codec.FormatConstants;
import io.ffit.carbon.i18n4j.core.codec.SourceCodec;

import java.util.Properties;

/**
 * Properties Source Codec implementation of {@link SourceCodec}
 *
 * @author Lay
 * @date 2022/9/6
 */
public class PropertiesSourceCodec implements SourceCodec {
    @Override
    public Properties decode(String data) {
        return PropertiesFactory.load(data);
    }

    @Override
    public String encode(Properties obj) {
        return PropertiesFactory.dump(obj);
    }

    @Override
    public String format() {
        return FormatConstants.PROPERTIES;
    }
}
