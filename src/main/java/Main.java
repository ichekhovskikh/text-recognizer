import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import nlp.NlpSentence;
import nlp.NlpText;
import nlp.NlpUtils;
import nlp.TextService;
import nlp.analyzers.MystemMorphAnalyzer;
import nlp.analyzers.NlpParseException;
import nlp.analyzers.SyntaxAnalyzer;
import nlp.analyzers.TreeTaggerMorphAnalyzer;
import nlp.words.MorphWord;
import nlp.words.NamedWord;
import nlp.words.SyntaxWord;
import org.annolab.tt4j.TreeTaggerException;
import org.maltparser.core.exception.MaltChainedException;
import ru.stachek66.nlp.mystem.holding.MyStemApplicationException;
import nlp.texterra.NamedAnnotationEntity;
import nlp.texterra.Texterra;
import ru.stachek66.nlp.mystem.model.Info;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

public class Main {

    public static void main(String[] args) throws MaltChainedException, IOException, URISyntaxException, MyStemApplicationException, TreeTaggerException, NlpParseException {
        String text = "Самарская область является частью России.";

        /*List<Info> infos = new MystemMorphAnalyzer().parse(new NlpSentence(text));
        for (Info info : infos) {
            System.out.println(info.initial() + " " + info.lex() + " " + info.rawResponse());
        }*/

        Texterra texterra = new Texterra();
        List<NamedAnnotationEntity> entities = texterra.getNamedAnnotationEntities(text);

        NlpText nlpText = new NlpText(text);
        NlpSentence nlpSentence = nlpText.getSentence(0);

        TreeTaggerMorphAnalyzer morphAnalyzer = new TreeTaggerMorphAnalyzer();
        SyntaxAnalyzer syntaxAnalyzer = new SyntaxAnalyzer(morphAnalyzer);

        List<MorphWord> morphWords = morphAnalyzer.parse(nlpSentence);
        List<SyntaxWord> syntaxWords = syntaxAnalyzer.parse(nlpSentence);
        List<NamedWord> namedWords = NlpUtils.transformNamedAnnotationsEntity(nlpSentence, entities, syntaxWords);

        for (NamedWord namedWord : namedWords)
            System.out.println(namedWord.getText());
    }
}
