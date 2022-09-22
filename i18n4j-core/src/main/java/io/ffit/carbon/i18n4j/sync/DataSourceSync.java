package io.ffit.carbon.i18n4j.sync;

import io.ffit.carbon.i18n4j.core.mapper.IndexMapper;
import io.ffit.carbon.i18n4j.core.mapper.SourceMapper;
import io.ffit.carbon.i18n4j.datasource.DataSource;
import io.ffit.carbon.i18n4j.datasource.DataSourcePack;
import io.ffit.carbon.i18n4j.entity.Index;
import io.ffit.carbon.i18n4j.entity.Source;

import java.util.List;

/**
 * Data Source Sync
 *
 * @author Lay
 * @date 2022/9/20
 */
public class DataSourceSync {

    private final IndexMapper fromIndexMapper;
    private final IndexMapper targetIndexMapper;
    private final SourceMapper fromSourceMapper;
    private final SourceMapper targetSourceMapper;

    public DataSourceSync(DataSourcePack from, DataSourcePack target) {
        this.fromIndexMapper = from.getIndex().indexProvider().mapper();
        this.fromSourceMapper = from.getSource().sourceProvider().mapper();
        this.targetIndexMapper = target.getIndex().indexProvider().mapper();
        this.targetSourceMapper = target.getSource().sourceProvider().mapper();
    }

    public DataSourceSync(DataSource from, DataSource target) {
        this.fromIndexMapper = from.indexProvider().mapper();
        this.fromSourceMapper = from.sourceProvider().mapper();
        this.targetIndexMapper = target.indexProvider().mapper();
        this.targetSourceMapper = target.sourceProvider().mapper();
    }

    public void sync() {
        syncSources();
        syncIndex();
    }

    public void syncIndex() {
        Index index = fromIndexMapper.select();

        // sync index
        targetIndexMapper.delete();
        targetIndexMapper.insert(index);
    }

    public void syncSources() {
        Index index = fromIndexMapper.select();

        // sync source
        List<Source> sources = fromSourceMapper.selectAll(index.toMap());
        index.forEach((tag, locales) -> {
            locales.forEach(locale -> {
                targetSourceMapper.delete(tag, locale);
            });
        });
        sources.forEach(targetSourceMapper::insert);
    }
}
