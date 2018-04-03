package nlp.analyzers;

import nlp.NlpSentence;
import nlp.words.MorphWordInfo;
import nlp.words.SyntaxWordInfo;
import org.maltparser.concurrent.ConcurrentMaltParserModel;
import org.maltparser.concurrent.ConcurrentMaltParserService;
import org.maltparser.concurrent.graph.ConcurrentDependencyGraph;
import org.maltparser.core.exception.MaltChainedException;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class SyntaxAnalyzer implements NlpAnalyzer<SyntaxWordInfo> {
    private ConcurrentMaltParserModel maltParser = null;
    private TreeTaggerMorphAnalyzer tagger = null;

    public SyntaxAnalyzer(TreeTaggerMorphAnalyzer tagger) throws URISyntaxException, IOException, MaltChainedException {
        this.tagger = tagger;
        File mcoFile = new File(getClass()
                .getClassLoader()
                .getResource("russian.mco")
                .toURI());
        maltParser = ConcurrentMaltParserService.initializeParserModel(mcoFile);
    }

    public List<SyntaxWordInfo> parse(NlpSentence sentence) throws NlpParseException {
        String[] tokens = sentence.getTokens();
        List<MorphWordInfo> infoList = tagger.parse(sentence);
        String[] coNLLX = new String[tokens.length];
        for (int i = 0; i < tokens.length; i++) {
            coNLLX[i] = buildCoNLLX(i, tokens[i], infoList.get(i).getInitial(),
                    infoList.get(i).getTag(), infoList.get(i).getFeats());
        }
        ConcurrentDependencyGraph graph = null;
        try {
            graph = maltParser.parse(coNLLX);
        } catch (MaltChainedException e) {
            throw new NlpParseException(e);
        }
        return toSyntaxWordList(graph);
    }

    private String buildCoNLLX(int lineNum, String word, String initial, String tag, String feats) {
        return String.format("%d\t%s\t%s\t%s\t%s\t%s\t_\t_", lineNum + 1, word, initial, tag, tag, feats);
    }

    private List<SyntaxWordInfo> toSyntaxWordList(ConcurrentDependencyGraph graph){
        final int INDEX = 0;
        final int TEXT = 1;
        final int INITIAL = 2;
        final int TAG = 3;
        final int FEATS = 5;
        final int HEAD_INDEX = 6;
        final int LABEL = 7;
        String[] coNLLXWords = graph.toString().split("\n");
        List<SyntaxWordInfo> syntaxWords = new ArrayList<>();
        for(String coNLLXWord : coNLLXWords) {
            if (coNLLXWord.equals("")) {
                continue;
            }
            String[] attrs = coNLLXWord.split("\t");
            SyntaxWordInfo info = new SyntaxWordInfo(attrs[TEXT], attrs[INITIAL], attrs[TAG], attrs[FEATS],
                    Integer.parseInt(attrs[INDEX]), Integer.parseInt(attrs[HEAD_INDEX]), attrs[LABEL]);
            syntaxWords.add(info);
        }
        return syntaxWords;
    }
}
