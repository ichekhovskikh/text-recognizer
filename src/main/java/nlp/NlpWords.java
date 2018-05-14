package nlp;

import nlp.analyzers.NlpParseException;
import nlp.analyzers.RelationshipAnalyzer;
import nlp.analyzers.SyntaxAnalyzer;
import nlp.analyzers.TreeTaggerMorphAnalyzer;
import nlp.texterra.NamedAnnotationEntity;
import nlp.texterra.Texterra;
import nlp.words.*;
import org.maltparser.core.exception.MaltChainedException;
import org.maltparser.core.syntaxgraph.Sentence;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class NlpWords {
    private NlpSentence sentence;

    private Texterra texterra;
    private TreeTaggerMorphAnalyzer morphAnalyzer;
    private SyntaxAnalyzer syntaxAnalyzer;
    RelationshipAnalyzer relationshipAnalyzer;

    private List<MorphWord> morphWords;
    private List<SyntaxWord> syntaxWords;
    private List<NamedWord> namedWords;
    List<RelationWord> relationWords;


    public NlpWords() throws IOException, URISyntaxException, MaltChainedException, NlpParseException {
        this.texterra = new Texterra();
        this.morphAnalyzer = new TreeTaggerMorphAnalyzer();
        this.syntaxAnalyzer = new SyntaxAnalyzer(morphAnalyzer);
        this.relationshipAnalyzer = new RelationshipAnalyzer();
        setSentence(new NlpSentence(""));
    }

    public NlpWords(Texterra texterra,
                    TreeTaggerMorphAnalyzer morphAnalyzer,
                    SyntaxAnalyzer syntaxAnalyzer,
                    RelationshipAnalyzer relationshipAnalyzer) throws IOException, NlpParseException {
        this.texterra = texterra;
        this.morphAnalyzer = morphAnalyzer;
        this.syntaxAnalyzer = syntaxAnalyzer;
        this.relationshipAnalyzer = relationshipAnalyzer;
        setSentence(new NlpSentence(""));
    }

    public NlpSentence getSentence() {
        return sentence;
    }

    public void setSentence(NlpSentence sentence) throws IOException, NlpParseException {
        this.sentence = sentence;
        List<NamedAnnotationEntity> entities = texterra.getNamedAnnotationEntities(sentence.getText());
        morphWords = morphAnalyzer.parse(sentence);
        syntaxWords = syntaxAnalyzer.parse(sentence);
        namedWords = NlpUtils.transformNamedAnnotationsEntity(sentence, entities, syntaxWords);
        relationWords = relationshipAnalyzer.parse(sentence);
    }

    public SyntaxWord getSyntaxWord(int index) {
        return syntaxWords
                .stream()
                .filter(elem -> elem.getIndex() == index)
                .findFirst()
                .get();
    }

    public MorphWord getMorphWord(int index) {
        return morphWords
                .stream()
                .filter(elem -> elem.getIndex() == index)
                .findFirst()
                .get();
    }

    public NamedWord getNamedWord(int index) {
        return namedWords
                .stream()
                .filter(elem -> elem.getIndexes().contains(index))
                .findFirst()
                .get();
    }

    public RelationWord getRelationWord(int index) {
        return relationWords
                .stream()
                .filter(elem -> elem.getIndexes().contains(index))
                .findFirst()
                .get();
    }

    public List<MorphWord> getMorphWords() {
        return morphWords;
    }

    public List<SyntaxWord> getSyntaxWords() {
        return syntaxWords;
    }

    public List<NamedWord> getNamedWords() {
        return namedWords;
    }

    public List<RelationWord> getRelationWords() {
        return relationWords;
    }

    public int getParentNamedWordIndex(int namedWordIndex) {
        return getParentNamedWordIndex(getNamedWord(namedWordIndex));
    }

    public int getParentRelationWordIndex(int relationWordIndex) {
        return getParentRelationWordIndex(getRelationWord(relationWordIndex));
    }

    public int getParentNamedWordIndex(NamedWord namedWord) {
        return getParentWordIndex(syntaxWords, namedWord.getIndexes());
    }

    public int getParentRelationWordIndex(RelationWord relationWord) {
        return getParentWordIndex(syntaxWords, relationWord.getIndexes());
    }

    public List<NamedWord> getParentRelationship(int relationWordIndex) {
        return getParentRelationship(getRelationWord(relationWordIndex));
    }

    public List<NamedWord> getParentRelationship(RelationWord relationWord) {
        List<NamedWord> predicates = new ArrayList<>();
        for (NamedWord namedWord : namedWords) {
            int parentNamedWordIndex = getParentNamedWordIndex(namedWord);
            SyntaxWord syntaxWord = getSyntaxWord(parentNamedWordIndex);
            if (relationWord.getIndexes().contains(syntaxWord.getHeadIndex()) && syntaxWord.getLabel().equals("предик")){
                predicates.add(namedWord);
            }
        }
        return predicates;
    }

    public List<NamedWord> getChildRelationship(int relationWordIndex) {
        return getChildRelationship(getRelationWord(relationWordIndex));
    }

    public List<NamedWord> getChildRelationship(RelationWord relationWord) {
        List<NamedWord> quasiagents = new ArrayList<>();
        for (NamedWord namedWord : namedWords) {
            int parentNamedWordIndex = getParentNamedWordIndex(namedWord);
            SyntaxWord syntaxWord = getSyntaxWord(parentNamedWordIndex);
            if (relationWord.getIndexes().contains(syntaxWord.getHeadIndex()) && !syntaxWord.getLabel().equals("предик")){
                quasiagents.add(namedWord);
            }
        }
        return quasiagents;
    }

    private int getParentWordIndex(List<SyntaxWord> syntaxWords, List<Integer> indexes) {
        for (SyntaxWord parent : syntaxWords) {
            if (!indexes.contains(parent.getIndex())) {
                continue;
            }
            boolean isParent = true;
            for (int i = 0; isParent && i < syntaxWords.size(); i++) {
                SyntaxWord child = syntaxWords.get(i);
                if (parent == child || !indexes.contains(child.getIndex())) {
                    continue;
                }
                isParent = (parent.getHeadIndex() != child.getIndex());
            }
            if (isParent) return parent.getIndex();
        }
        return -1;
    }
}
