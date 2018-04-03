package nlp.analyzers;

import com.google.common.collect.Lists;
import nlp.NlpSentence;
import nlp.words.MorphWordInfo;
import org.annolab.tt4j.TokenHandler;
import org.annolab.tt4j.TreeTaggerException;
import org.annolab.tt4j.TreeTaggerWrapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TreeTaggerMorphAnalyzer implements NlpAnalyzer<MorphWordInfo> {
    private TreeTaggerWrapper tagger = null;

    public TreeTaggerMorphAnalyzer(String text) throws IOException {
        System.setProperty("treetagger.home", "/treetagger");
        tagger = new TreeTaggerWrapper<String>();
        tagger.setModel("russian-utf8.par:utf8");
    }

    public List<MorphWordInfo> parse(NlpSentence sentence) throws NlpParseException {
        try {
            final List<MorphWordInfo> wordInfoList = new ArrayList<>();
            tagger.setHandler((TokenHandler<String>) (token, pos, lemma) -> {
                wordInfoList.add(new MorphWordInfo(token, lemma, String.valueOf(pos.charAt(0)), pos));
            });
            tagger.process(Lists.newArrayList(sentence.getTokens()));
            return wordInfoList;
        } catch (IOException | TreeTaggerException e) {
            throw new NlpParseException(e);
        }
    }
}
