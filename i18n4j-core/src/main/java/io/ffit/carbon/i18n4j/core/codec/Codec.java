package io.ffit.carbon.i18n4j.core.codec;

/**
 * Codec Interface
 *
 * @author Lay
 * @date 2022/9/6
 */
public interface Codec<T> {
    /**
     * encode
     * @param data  binary data
     * @return  Object
     */
    T decode(String data);

    /**
     * decode
     * @param obj Object
     * @return binary data
     */
    String encode(T obj);

    /**
     * supported extensions
     * @return extensions
     */
    String format();
}
