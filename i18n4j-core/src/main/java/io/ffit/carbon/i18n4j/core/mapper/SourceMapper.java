package io.ffit.carbon.i18n4j.core.mapper;

import io.ffit.carbon.i18n4j.entity.Source;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * Source Mapper
 *
 * @author Lay
 * @date 2022/9/6
 */
public interface SourceMapper {
    /**
     * insert source
     * @param source Source
     * @return affected rows
     */
    int insert(Source source);

    /**
     * update source
     * @param source Source
     * @return affected rows
     */
    int update(Source source);

    /**
     * update source or insert if source not exists
     * @param source    Source
     * @return  affected rows
     */
    int insertOrUpdate(Source source);

    /**
     * select source
     * @param tag       tag
     * @param locale    locale
     * @return  Source
     */
    Source selectOne(String tag, Locale locale);

    /**
     * select sources by tag and locales
     * @param map tag and locales
     * @return  Source list
     */
    List<Source> selectAll(Map<String, Set<Locale>> map);

    /**
     * delete source
     * @param tag       tag
     * @param locale    locale
     * @return  affected rows
     */
    int delete(String tag, Locale locale);
}
