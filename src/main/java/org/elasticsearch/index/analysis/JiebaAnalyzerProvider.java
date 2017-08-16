package org.elasticsearch.index.analysis;

import org.apache.lucene.analysis.Analyzer;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.env.Environment;
import org.elasticsearch.index.IndexSettings;

public class JiebaAnalyzerProvider extends AbstractIndexAnalyzerProvider<JiebaAnalyzer> {

    private final JiebaAnalyzer analyzer;

    public JiebaAnalyzerProvider(IndexSettings indexSettings, Environment env, String name, Settings settings) {
        super(indexSettings, name, settings);
        analyzer = new JiebaAnalyzer(settings, env);
    }

    private JiebaAnalyzerProvider(String segMode, IndexSettings indexSettings, Environment env, String name, Settings settings) {
        super(indexSettings, name, settings);
        analyzer = new JiebaAnalyzer(segMode, settings, env);
    }

    @Override
    public JiebaAnalyzer get() {
        return this.analyzer;
    }

    public static AnalyzerProvider<? extends Analyzer> createJiebaIndexAnalyzerProvider(
            IndexSettings indexSettings, Environment environment, String name, Settings settings) {

        return new JiebaAnalyzerProvider("index", indexSettings, environment, name, settings);
    }

    public static AnalyzerProvider<? extends Analyzer> createJiebaSearchAnalyzerProvider(
            IndexSettings indexSettings, Environment environment, String name, Settings settings) {

        return new JiebaAnalyzerProvider("search", indexSettings, environment, name, settings);
    }

    public static AnalyzerProvider<? extends Analyzer> createJiebaOtherAnalyzerProvider(
            IndexSettings indexSettings, Environment environment, String name, Settings settings) {

        return new JiebaAnalyzerProvider("other", indexSettings, environment, name, settings);
    }
}
