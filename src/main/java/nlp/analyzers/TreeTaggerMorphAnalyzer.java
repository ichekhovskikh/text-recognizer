package nlp.analyzers;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.name.Named;
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

    @Inject
    public TreeTaggerMorphAnalyzer(@Named("MorphModelPath")String pathModel) throws IOException {
        System.setProperty("treetagger.home", "/treetagger");
        tagger = new TreeTaggerWrapper<String>();
        tagger.setModel(pathModel);
    }

    public void setModel(String pathModel) throws IOException {
        tagger.setModel(pathModel);
    }

    public List<MorphWord> parse(NlpSentence sentence) throws NlpParseException {
        try {
            final List<MorphWord> wordList = new ArrayList<>();
            tagger.setHandler((TokenHandler<String>) (token, pos, lemma) -> {
                int index = wordList.size() + 1;
                wordList.add(new MorphWord(index, token, lemma, String.valueOf(pos.charAt(0)), pos));
            });
            tagger.process(Lists.newArrayList(sentence.getTokens()));
            return wordList;
        } catch (IOException | TreeTaggerException e) {
            throw new NlpParseException(e);
        }
    }
}
