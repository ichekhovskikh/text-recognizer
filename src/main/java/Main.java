import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import nlp.NlpSentence;
import nlp.NlpText;
import nlp.NlpUtils;
import nlp.analyzers.*;
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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {

    public static void main(String[] args) throws MaltChainedException, IOException, URISyntaxException, MyStemApplicationException, TreeTaggerException, NlpParseException {
        testJsonModel();
    }

    private static void testJsonModel() {
        RelationshipModel model = new RelationshipModel();

        model.addRelation("contains", "содержит");
        model.addRelation("contains", "включает");
        model.addRelation("crosses", "пересекает");
        model.addRelation("inside", "имеет внутри");

/*        Map<String, List<String>> map = new HashMap<>();
        map.put("contains", Collections.singletonList("содержит"));
        map.put("crosses", Collections.singletonList("пересекает"));
        map.put("inside", Collections.singletonList("имеет внутри"));*/

        System.out.println(new Gson().toJson(model));
    }

    private static void test() throws MaltChainedException, IOException, URISyntaxException, MyStemApplicationException, TreeTaggerException, NlpParseException {

        String text = "Самарская область   является   частью России  .";
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
            System.out.println(namedWord.getText() + " - " + namedWord.getNamedTag());
    }
}
