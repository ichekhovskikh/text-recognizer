import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import nlp.NlpSentence;
import nlp.NlpText;
import nlp.NlpUtils;
import nlp.analyzers.*;
import nlp.words.MorphWord;
import nlp.words.NamedWord;
import nlp.words.RelationWord;
import nlp.words.SyntaxWord;
import org.annolab.tt4j.TreeTaggerException;
import org.maltparser.core.exception.MaltChainedException;
import ru.stachek66.nlp.mystem.holding.MyStemApplicationException;
import nlp.texterra.NamedAnnotationEntity;
import nlp.texterra.Texterra;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.util.List;

public class Test {

    public static void main(String[] args) throws MaltChainedException, IOException, URISyntaxException, MyStemApplicationException, TreeTaggerException, NlpParseException {
//        testAdjectiveEnd();
        testSyntax();
    }

    private static void testAdjectiveEnd() throws IOException, URISyntaxException, NlpParseException, MaltChainedException {
        String text = "Самарский область   является  частью  России  .";
        NlpText nlpText = new NlpText(text);
        NlpSentence nlpSentence = nlpText.getSentence(0);

        Texterra texterra = new Texterra();
        List<NamedAnnotationEntity> entities = texterra.getNamedAnnotationEntities(text);

        TreeTaggerMorphAnalyzer morphAnalyzer = new TreeTaggerMorphAnalyzer();
        SyntaxAnalyzer syntaxAnalyzer = new SyntaxAnalyzer(morphAnalyzer);

        List<MorphWord> morphWords = morphAnalyzer.parse(nlpSentence);
        List<SyntaxWord> syntaxWords = syntaxAnalyzer.parse(nlpSentence);
        List<NamedWord> namedWords = NlpUtils.transformNamedAnnotationsEntity(nlpSentence, entities, syntaxWords);

        String nameText = NlpUtils.wordMatching(namedWords.get(0).getIndexes(), syntaxWords, morphWords);

        System.out.println(nameText);
    }

/*    private static void testRelationship() throws IOException, URISyntaxException, NlpParseException, MaltChainedException {
        String text = "Самарская область   является  частью  России  .";
        NlpText nlpText = new NlpText(text);
        NlpSentence nlpSentence = nlpText.getSentence(0);

        Texterra texterra = new Texterra();
        List<NamedAnnotationEntity> entities = texterra.getNamedAnnotationEntities(text);

        TreeTaggerMorphAnalyzer morphAnalyzer = new TreeTaggerMorphAnalyzer();
        SyntaxAnalyzer syntaxAnalyzer = new SyntaxAnalyzer(morphAnalyzer);
        RelationshipAnalyzer analyzer = new RelationshipAnalyzer();

        List<MorphWord> morphWords = morphAnalyzer.parse(nlpSentence);
        List<SyntaxWord> syntaxWords = syntaxAnalyzer.parse(nlpSentence);
        List<NamedWord> namedWords = NlpUtils.transformNamedAnnotationsEntity(nlpSentence, entities, syntaxWords);
        List<RelationWord> relationWords = analyzer.parse(nlpSentence);

        for (RelationWord word : relationWords) {
            System.out.println(NlpUtils.getParentRelationship(word, syntaxWords, namedWords).get(0).getText() + "  "
                    + word.getText() + "  "
                    + NlpUtils.getChildRelationship(word, syntaxWords, namedWords).get(0).getText());
        }
    }*/

    private static void testRelations() throws IOException, URISyntaxException, NlpParseException {
        String text = "Самарская область   является  частью  России  .";
        NlpText nlpText = new NlpText(text);
        NlpSentence nlpSentence = nlpText.getSentence(0);

        RelationshipAnalyzer analyzer = new RelationshipAnalyzer();
        List<RelationWord> relationWords = analyzer.parse(nlpSentence);

        for (RelationWord word : relationWords)
            System.out.println(word.getText());
    }

    private static void testJsonModel() {
        RelationshipModel model = new RelationshipModel();

        model.addRelation("contains", "содержит");
        model.addRelation("contains", "включает");
        model.addRelation("crosses", "пересекает");
        model.addRelation("inside", "имеет внутри");
        System.out.println(new Gson().toJson(model));
    }

    private static void testSyntax() throws MaltChainedException, IOException, URISyntaxException, MyStemApplicationException, TreeTaggerException, NlpParseException {

        String text = "Самарская область   является   частью России  .";

        NlpText nlpText = new NlpText(text);
        NlpSentence nlpSentence = nlpText.getSentence(0);

        TreeTaggerMorphAnalyzer morphAnalyzer = new TreeTaggerMorphAnalyzer();
        SyntaxAnalyzer syntaxAnalyzer = new SyntaxAnalyzer(morphAnalyzer);

        List<MorphWord> morphWords = morphAnalyzer.parse(nlpSentence);
        List<SyntaxWord> syntaxWords = syntaxAnalyzer.parse(nlpSentence);

        for (SyntaxWord syntaxWord : syntaxWords)
            System.out.println(syntaxWord.getIndex() + ") " + syntaxWord.getText() + " - " + syntaxWord.getHeadIndex() + " - " + syntaxWord.getLabel());
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
