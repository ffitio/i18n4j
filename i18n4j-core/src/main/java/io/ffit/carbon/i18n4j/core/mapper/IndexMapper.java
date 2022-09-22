package io.ffit.carbon.i18n4j.core.mapper;

import io.ffit.carbon.i18n4j.entity.Index;

/**
 * Index Mapper
 *
 * @author Lay
 * @date 2022/9/6
 */
public interface IndexMapper {
    /**
     * insert index
     * @param index Index
     * @return affected rows
     */
    int insert(Index index);

    /**
     * update index
     * @param index Index
     * @return  affected rows
     */
    int update(Index index);

    /**
     * select index
     * @return Index
     */
    Index select();

    /**
     * delete index
     * @return  affected rows
     */
    int delete();
}
