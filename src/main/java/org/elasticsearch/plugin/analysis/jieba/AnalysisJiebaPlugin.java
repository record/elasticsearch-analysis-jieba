package org.elasticsearch.plugin.analysis.jieba;

import org.apache.lucene.analysis.Analyzer;
import org.elasticsearch.index.analysis.*;
import org.elasticsearch.indices.analysis.AnalysisModule;
import org.elasticsearch.plugins.AnalysisPlugin;
import org.elasticsearch.plugins.Plugin;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class AnalysisJiebaPlugin extends Plugin implements AnalysisPlugin {

	public static String PLUGIN_NAME = "analysis-jieba";

	@Override
	public Map<String, AnalysisModule.AnalysisProvider<TokenFilterFactory>> getTokenFilters() {
		return Collections.singletonMap("jieba", JiebaTokenFilterFactory::new);
	}

	@Override
	public Map<String, AnalysisModule.AnalysisProvider<AnalyzerProvider<? extends Analyzer>>> getAnalyzers() {
		Map<String, AnalysisModule.AnalysisProvider<AnalyzerProvider<? extends Analyzer>>> extra = new HashMap<>();
		extra.put("jieba", JiebaAnalyzerProvider::new);
		extra.put("jieba_index", JiebaAnalyzerProvider::createJiebaIndexAnalyzerProvider);
		extra.put("jieba_search", JiebaAnalyzerProvider::createJiebaSearchAnalyzerProvider);
		extra.put("jieba_other", JiebaAnalyzerProvider::createJiebaOtherAnalyzerProvider);
		return extra;
	}
}
