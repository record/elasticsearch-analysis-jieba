package org.elasticsearch.index.analysis;

import org.apache.lucene.analysis.TokenStream;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.inject.assistedinject.Assisted;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.env.Environment;
import org.elasticsearch.index.IndexSettings;

import com.huaban.analysis.jieba.WordDictionary;

public class JiebaTokenFilterFactory extends AbstractTokenFilterFactory {

	private String type;

	@Inject
	public JiebaTokenFilterFactory(IndexSettings indexSettings, @Assisted String name,
								   @Assisted Settings settings, String type) {
		super(indexSettings, name, settings);

		this.type = type;

		Environment env = new Environment(indexSettings.getSettings());
		WordDictionary.getInstance().init(env.pluginsFile().resolve("jieba/dic"));
	}

	@Override
	public TokenStream create(TokenStream input) {
		return new JiebaTokenFilter(type, input);
	}

    public static TokenFilterFactory getJiebaSearchTokenFilterFactory(
            IndexSettings indexSettings, Environment environment, String s,
            Settings settings)
    {
        TokenFilterFactory jiebaTokenFilterFactory = new JiebaTokenFilterFactory(
                indexSettings, s, settings,
                "search");

        return jiebaTokenFilterFactory;
    }

    public static TokenFilterFactory getJiebaIndexTokenFilterFactory(
            IndexSettings indexSettings, Environment environment, String s,
            Settings settings)
    {
        TokenFilterFactory jiebaTokenFilterFactory = new JiebaTokenFilterFactory(
                indexSettings, s, settings,
                "index");

        return jiebaTokenFilterFactory;
    }

    public static TokenFilterFactory getJiebaOtherTokenFilterFactory(
            IndexSettings indexSettings, Environment environment, String s,
            Settings settings)
    {
        TokenFilterFactory jiebaTokenFilterFactory = new JiebaTokenFilterFactory(
                indexSettings, s, settings,
                "other");

        return jiebaTokenFilterFactory;
    }
}
