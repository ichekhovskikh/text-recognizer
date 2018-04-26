package nlp.analyzers;

import nlp.NlpSentence;
import nlp.words.MorphWord;
import nlp.words.SyntaxWord;
import org.maltparser.concurrent.ConcurrentMaltParserModel;
import org.maltparser.concurrent.ConcurrentMaltParserService;
import org.maltparser.concurrent.graph.ConcurrentDependencyGraph;
import org.maltparser.core.exception.MaltChainedException;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class SyntaxAnalyzer implements NlpAnalyzer<SyntaxWord> {
    private ConcurrentMaltParserModel maltParser = null;
    private TreeTaggerMorphAnalyzer tagger = null;

    public SyntaxAnalyzer() throws URISyntaxException, IOException, MaltChainedException {
        this(new TreeTaggerMorphAnalyzer(), "russian.mco");
    }

    public SyntaxAnalyzer(TreeTaggerMorphAnalyzer tagger) throws URISyntaxException, IOException, MaltChainedException {
        this(tagger, "russian.mco");
    }

    public SyntaxAnalyzer(TreeTaggerMorphAnalyzer tagger, String pathModel) throws URISyntaxException, IOException, MaltChainedException {
        this.tagger = tagger;
        setModel(pathModel);
    }

    public void setModel(String pathModel) throws IOException, URISyntaxException, MaltChainedException {
        File mcoFile = new File(getClass()
                .getClassLoader()
                .getResource(pathModel)
                .toURI());
        maltParser = ConcurrentMaltParserService.initializeParserModel(mcoFile);
    }

    public List<SyntaxWord> parse(NlpSentence sentence) throws NlpParseException {
        List<String> tokens = sentence.getTokens();
        List<MorphWord> infoList = tagger.parse(sentence);
        String[] coNLLX = new String[tokens.size()];
        for (int i = 0; i < tokens.size(); i++) {
            coNLLX[i] = buildCoNLLX(i, tokens.get(i), infoList.get(i).getInitial(),
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

    private List<SyntaxWord> toSyntaxWordList(ConcurrentDependencyGraph graph){
        final int INDEX = 0;
        final int TEXT = 1;
        final int HEAD_INDEX = 6;
        final int LABEL = 7;
        String[] coNLLXWords = graph.toString().split("\n");
        List<SyntaxWord> syntaxWords = new ArrayList<>();
        for(String coNLLXWord : coNLLXWords) {
            if (coNLLXWord.equals("")) {
                continue;
            }
            String[] attrs = coNLLXWord.split("\t");
            SyntaxWord info = new SyntaxWord(
                    Integer.parseInt(attrs[INDEX]),
                    Integer.parseInt(attrs[HEAD_INDEX]),
                    attrs[TEXT],
                    attrs[LABEL]);
            syntaxWords.add(info);
        }
        return syntaxWords;
    }
}
