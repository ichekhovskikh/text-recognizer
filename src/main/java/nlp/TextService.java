package nlp;

import nlp.words.MorphWord;
import org.maltparser.concurrent.*;
import ru.stachek66.nlp.mystem.holding.*;
import org.annolab.tt4j.*;
import com.google.common.collect.Lists;
import org.maltparser.concurrent.graph.ConcurrentDependencyGraph;
import org.maltparser.core.exception.MaltChainedException;
import ru.stachek66.nlp.mystem.model.Info;
import scala.Option;
import scala.collection.JavaConversions;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.*;

import static java.util.Arrays.*;

@Deprecated
public class TextService {

    private String text = null;
    private List<String> sentences = null;
    private ConcurrentMaltParserModel maltParser = null;
    private TreeTaggerWrapper tagger = null;
    private MyStem mystemAnalyzer = null;

    public TextService(String text) throws URISyntaxException, IOException, MaltChainedException {
        this.text = text;
        sentences = splitText(text);

        File mcoFile = new File(getClass()
                .getClassLoader()
                .getResource("russian.mco")
                .toURI());
        maltParser = ConcurrentMaltParserService.initializeParserModel(mcoFile);

        System.setProperty("treetagger.home", "/treetagger");
        tagger = new TreeTaggerWrapper<String>();
        tagger.setModel("russian-utf8.par:utf8");

        mystemAnalyzer = new Factory("-igd --format json")
                .newMyStem("3.0", Option.empty()).get();
    }

    public String getText() {
        return text;
    }

    public String getSentence(int index) {
        return sentences.get(index);
    }

    public List<String> getAllSentences() {
        return sentences;
    }

    public List<Info> morphAnalysis(int index) throws MyStemApplicationException {
        return morphAnalysis(sentences.get(index));
    }

    public List<Info> morphAnalysis(String sentence) throws MyStemApplicationException {
        return Lists.newArrayList(
                JavaConversions.asJavaIterable(mystemAnalyzer
                        .analyze(Request.apply(sentence))
                        .info()
                        .toIterable()));
    }

    public void syntaxParsingSentence(int index) throws MaltChainedException, MyStemApplicationException, IOException, TreeTaggerException {
        syntaxParsingSentence(sentences.get(index));
    }

    public void syntaxParsingSentence(String sentence) throws MaltChainedException, MyStemApplicationException, IOException, TreeTaggerException {
        String[] tokens = getTokens(sentence);
        List<MorphWord> infoList = treeTaggerMorphAnalysis(tokens);
        String[] coNLLX = new String[tokens.length];
        for (int i = 0; i < tokens.length; i++) {
            coNLLX[i] = buildCoNLLX(i, tokens[i], infoList.get(i).getInitial(),
                    infoList.get(i).getTag(), infoList.get(i).getFeats());
        }
        syntaxParsingSentence(coNLLX);
    }

    public static List<String> splitText(String text) {
        final int MIN_LENGTH = 3;
        Pattern pattern = Pattern.compile(
                "(^|(?<=[.!?]\\s))(\\d+\\.\\s?)*[А-ЯA-Z][^!?]*?[.!?](?=\\s*(\\d+\\.\\s)*[А-ЯA-Z]|$)",
                Pattern.MULTILINE);
        Matcher matcher = pattern.matcher(text);
        List<String> sentences = new ArrayList<>();
        sentences.add("");
        while (matcher.find()) {
            String sentence = matcher.group();
            String lastSentence = sentences.get(sentences.size() - 1);
            String[] tokens = lastSentence.split(" ");
            if (tokens[tokens.length - 1].length() < MIN_LENGTH)
                sentences.set(sentences.size() - 1, lastSentence + " " + sentence);
            else sentences.add(sentence);
        }
        return sentences;
    }

    public static String[] getTokens(String sentence) {
        return stream(sentence.replaceAll("[^A-Za-zА-Яа-яЁё| ]", "").split(" "))
                .filter(elem -> elem != null && !elem.equals(""))
                .toArray(String[]::new);
    }

    private List<MorphWord> treeTaggerMorphAnalysis(String[] words) throws IOException, TreeTaggerException {
        final List<MorphWord> wordInfoList = new ArrayList<>();
        tagger.setHandler((TokenHandler<String>)(token, pos, lemma) -> {
            wordInfoList.add(new MorphWord(token, lemma, String.valueOf(pos.charAt(0)), pos));
        });
        tagger.process(Lists.newArrayList(words));
        return wordInfoList;
    }

    private String buildCoNLLX(int lineNum, String word, String initial, String tag, String feats) {
        return String.format("%d\t%s\t%s\t%s\t%s\t%s\t_\t_", lineNum + 1, word, initial, tag, tag, feats);
    }

    private void syntaxParsingSentence(String[] coNLLX) throws MaltChainedException {
        ConcurrentDependencyGraph graph = maltParser.parse(coNLLX);


        //====test
        String s = graph.toString();
        int k = s.length();
        System.out.println(graph);
        //====test
    }
}
