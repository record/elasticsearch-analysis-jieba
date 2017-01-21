package org.elasticsearch.index.analysis;

import org.apache.lucene.analysis.Analyzer;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.inject.assistedinject.Assisted;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.env.Environment;
import org.elasticsearch.index.IndexSettings;

import com.huaban.analysis.jieba.JiebaSegmenter;

public class JiebaAnalyzerProvider
        extends AbstractIndexAnalyzerProvider<JiebaAnalyzer>
{
    private final JiebaAnalyzer analyzer;

    @Inject
    public JiebaAnalyzerProvider(IndexSettings indexSettings, Environment env,
            @Assisted String name, @Assisted Settings settings, String segMode)
    {
        super(indexSettings, name, settings);

        analyzer = new JiebaAnalyzer(settings, env, segMode);
    }

    @Override
    public JiebaAnalyzer get()
    {
        return this.analyzer;
    }

    public static AnalyzerProvider<? extends Analyzer> getJiebaSearchAnalyzerProvider(
            IndexSettings indexSettings, Environment environment, String s,
            Settings settings)
    {
        JiebaAnalyzerProvider jiebaAnalyzerProvider = new JiebaAnalyzerProvider(
                indexSettings, environment, s, settings,
                JiebaSegmenter.SegMode.SEARCH.toString());

        return jiebaAnalyzerProvider;
    }

    public static AnalyzerProvider<? extends Analyzer> getJiebaIndexAnalyzerProvider(
            IndexSettings indexSettings, Environment environment, String s,
            Settings settings)
    {
        JiebaAnalyzerProvider jiebaAnalyzerProvider = new JiebaAnalyzerProvider(
                indexSettings, environment, s, settings,
                JiebaSegmenter.SegMode.INDEX.toString());

        return jiebaAnalyzerProvider;
    }
}
