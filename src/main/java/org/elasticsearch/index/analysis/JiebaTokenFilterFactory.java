package org.elasticsearch.index.analysis;

import org.apache.lucene.analysis.TokenStream;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.inject.assistedinject.Assisted;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.env.Environment;
import org.elasticsearch.index.IndexSettings;
import org.elasticsearch.plugin.analysis.jieba.AnalysisJiebaPlugin;

import com.huaban.analysis.jieba.WordDictionary;

public class JiebaTokenFilterFactory extends AbstractTokenFilterFactory {
	private String type;

	@Inject
	public JiebaTokenFilterFactory(IndexSettings indexSettings,
								   Environment env, @Assisted String name,
								   @Assisted Settings settings) {
		super(indexSettings, name, settings);
		type = settings.get("seg_mode", "index");
		WordDictionary.getInstance().init(env.configFile().resolve(AnalysisJiebaPlugin.PLUGIN_NAME));
	}

	@Override
	public TokenStream create(TokenStream input) {
		return new JiebaTokenFilter(type, input);
	}

}
