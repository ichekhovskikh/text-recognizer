package nlp.analyzers;

import com.google.common.collect.Lists;
import nlp.NlpSentence;
import nlp.words.MorphWord;
import org.annolab.tt4j.TokenHandler;
import org.annolab.tt4j.TreeTaggerException;
import org.annolab.tt4j.TreeTaggerWrapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TreeTaggerMorphAnalyzer implements NlpAnalyzer<MorphWord> {
    private TreeTaggerWrapper tagger = null;

    public TreeTaggerMorphAnalyzer() throws IOException {
        System.setProperty("treetagger.home", "/treetagger");
        tagger = new TreeTaggerWrapper<String>();
        tagger.setModel("russian-utf8.par:utf8");
    }

    public List<MorphWord> parse(NlpSentence sentence) throws NlpParseException {
        try {
            final List<MorphWord> wordList = new ArrayList<>();
            tagger.setHandler((TokenHandler<String>) (token, pos, lemma) -> {
                wordList.add(new MorphWord(token, lemma, String.valueOf(pos.charAt(0)), pos));
            });
            tagger.process(Lists.newArrayList(sentence.getTokens()));
            return wordList;
        } catch (IOException | TreeTaggerException e) {
            throw new NlpParseException(e);
        }
    }
}
