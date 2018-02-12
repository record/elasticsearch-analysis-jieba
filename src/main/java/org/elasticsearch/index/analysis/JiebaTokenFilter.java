package org.elasticsearch.index.analysis;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;

import com.huaban.analysis.jieba.JiebaSegmenter;
import com.huaban.analysis.jieba.JiebaSegmenter.SegMode;
import com.huaban.analysis.jieba.SegToken;

public final class JiebaTokenFilter extends TokenFilter {

    JiebaSegmenter segmenter;

    private int currentOffset;
    private Iterator<SegToken> tokenIter;
    private List<SegToken> array;
    private String type;

    private final CharTermAttribute termAtt = addAttribute(CharTermAttribute.class);
    private final OffsetAttribute offsetAtt = addAttribute(OffsetAttribute.class);
    private final TypeAttribute typeAtt = addAttribute(TypeAttribute.class);

    public JiebaTokenFilter(String type, TokenStream input) {
        super(input);
        this.currentOffset = 0;
        this.type = type;
        segmenter = new JiebaSegmenter();
    }

    @Override
    public boolean incrementToken() throws IOException {
        if (tokenIter == null || !tokenIter.hasNext()) {
            if (input.incrementToken()) {
                String token = termAtt.toString();
                if (type.equals("index"))
                    array = segmenter
                            .process(token, SegMode.INDEX);
                else if (type.equals("other")) {
                    array = new ArrayList<SegToken>();
                    char[] ctoken = token.toCharArray();
                    for (int i = 0; i < ctoken.length; i++) {
                        /* 全角=>半角 */
                        if (ctoken[i] > 0xFF00 && ctoken[i] < 0xFF5F)
                            ctoken[i] = (char) (ctoken[i] - 0xFEE0);

                        /* 大写=>小写 */
                        if (ctoken[i] > 0x40 && ctoken[i] < 0x5b)
                            ctoken[i] = (char) (ctoken[i] + 0x20);
                    }
                    token = String.valueOf(ctoken);
                    array.add(new SegToken(token, currentOffset, token.length()));
                } else
                    array = segmenter.process(termAtt.toString(),
                            SegMode.SEARCH);
                for (SegToken tok : array) {
                    tok.startOffset += this.currentOffset;
                    tok.endOffset += this.currentOffset;
                }
                this.currentOffset += token.length();
                array.sort((
                    SegToken lhs, SegToken rhs) -> (
                        lhs.startOffset == rhs.startOffset ? ( lhs.endOffset == rhs.endOffset ? 0
                                                                                              : ( lhs.endOffset < rhs.endOffset ? -1 : 1))
                                                           : ( lhs.startOffset < rhs.startOffset ? -1 : 1 )));

                for (int i = 0; i < array.size(); ++i) {
                    SegToken item = array.get(i);
                }
                tokenIter = array.iterator();
                if (!tokenIter.hasNext())
                    return false;
            } else {
                return false; // no more sentences, end of stream!
            }
        }
        // WordTokenFilter must clear attributes, as it is creating new tokens.
        clearAttributes();

        SegToken token = tokenIter.next();
        offsetAtt.setOffset(token.startOffset, token.endOffset);
        String tokenString = token.word;
        termAtt.copyBuffer(tokenString.toCharArray(), 0, tokenString.length());
        typeAtt.setType("word");
        return true;
    }

    @Override
    public void reset() throws IOException {
        super.reset();
        currentOffset = 0;
        tokenIter = null;
    }

}
