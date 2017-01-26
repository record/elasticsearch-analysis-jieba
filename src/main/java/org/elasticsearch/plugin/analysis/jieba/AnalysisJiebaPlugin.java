package org.elasticsearch.plugin.analysis.jieba;

import java.util.HashMap;
import java.util.Map;

import org.apache.lucene.analysis.Analyzer;
import org.elasticsearch.index.analysis.AnalyzerProvider;
import org.elasticsearch.index.analysis.JiebaAnalyzerProvider;
import org.elasticsearch.index.analysis.JiebaTokenFilterFactory;
import org.elasticsearch.index.analysis.TokenFilterFactory;
import org.elasticsearch.indices.analysis.AnalysisModule.AnalysisProvider;
import org.elasticsearch.plugins.AnalysisPlugin;
import org.elasticsearch.plugins.Plugin;

public class AnalysisJiebaPlugin extends Plugin implements AnalysisPlugin {

    @Override
    public Map<String, AnalysisProvider<TokenFilterFactory>> getTokenFilters()
    {
        Map<String, AnalysisProvider<TokenFilterFactory>> extra = new HashMap<>();

        extra.put("jieba_search", JiebaTokenFilterFactory::getJiebaSearchTokenFilterFactory);
        extra.put("jieba_index", JiebaTokenFilterFactory::getJiebaIndexTokenFilterFactory);
        extra.put("jieba_other", JiebaTokenFilterFactory::getJiebaOtherTokenFilterFactory);

        return extra;
    }

    @Override
    public Map<String, AnalysisProvider<AnalyzerProvider<? extends Analyzer>>> getAnalyzers()
    {
        Map<String, AnalysisProvider<AnalyzerProvider<? extends Analyzer>>> extra = new HashMap<>();

        extra.put("jieba_search", JiebaAnalyzerProvider::getJiebaSearchAnalyzerProvider);
        extra.put("jieba_index", JiebaAnalyzerProvider::getJiebaIndexAnalyzerProvider);
        extra.put("jieba_other", JiebaAnalyzerProvider::getJiebaOtherAnalyzerProvider);

        return extra;
    }

}
