package nlp;

import com.google.common.collect.Lists;
import logging.Logger;
import nlp.analyzers.NlpParseException;
import nlp.analyzers.RelationAnalyzer;
import nlp.analyzers.SyntaxAnalyzer;
import nlp.analyzers.TreeTaggerMorphAnalyzer;
import nlp.texterra.NamedAnnotationEntity;
import nlp.texterra.Texterra;
import nlp.tree.NlpTreeDependency;
import nlp.tree.NlpTreeObject;
import nlp.tree.NlpTreeRelation;
import nlp.words.*;
import org.maltparser.core.exception.MaltChainedException;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;

public class NlpController {
    private NlpSentence sentence;

    private Texterra texterra;
    private NlpTreeDependency nlpTreeDependency;

    private TreeTaggerMorphAnalyzer morphAnalyzer;
    private SyntaxAnalyzer syntaxAnalyzer;
    private RelationAnalyzer relationAnalyzer;

    private List<MorphWord> morphWords;
    private List<SyntaxWord> syntaxWords;
    private List<NamedWord> namedWords;
    private List<RelationWord> relationWords;


    public NlpController() throws IOException, URISyntaxException, MaltChainedException, NlpParseException {
        this.texterra = new Texterra();
        this.morphAnalyzer = new TreeTaggerMorphAnalyzer();
        this.syntaxAnalyzer = new SyntaxAnalyzer(morphAnalyzer);
        this.relationAnalyzer = new RelationAnalyzer();
        this.nlpTreeDependency = new NlpTreeDependency();
        setSentence(new NlpSentence(""));
    }

    public NlpController(Texterra texterra,
                         TreeTaggerMorphAnalyzer morphAnalyzer,
                         SyntaxAnalyzer syntaxAnalyzer,
                         RelationAnalyzer relationAnalyzer) throws IOException, NlpParseException {
        this.texterra = texterra;
        this.morphAnalyzer = morphAnalyzer;
        this.syntaxAnalyzer = syntaxAnalyzer;
        this.relationAnalyzer = relationAnalyzer;
        setSentence(new NlpSentence(""));
    }

    public NlpTreeDependency getNlpTreeDependency() {
        return nlpTreeDependency;
    }

    public void setNlpTreeDependency(NlpTreeDependency nlpTreeDependency) {
        this.nlpTreeDependency = nlpTreeDependency;
    }

    public TreeTaggerMorphAnalyzer getMorphAnalyzer() {
        return morphAnalyzer;
    }

    public void setMorphAnalyzer(TreeTaggerMorphAnalyzer morphAnalyzer) throws IOException, NlpParseException {
        morphWords = morphAnalyzer.parse(sentence);
        Logger.logging("Морфологический анализ = " +
                morphWords.stream().map(Object::toString).collect(Collectors.joining(", ")));
        this.morphAnalyzer = morphAnalyzer;
    }

    public SyntaxAnalyzer getSyntaxAnalyzer() {
        return syntaxAnalyzer;
    }

    public void setSyntaxAnalyzer(SyntaxAnalyzer syntaxAnalyzer) throws IOException, NlpParseException {
        syntaxWords = syntaxAnalyzer.parse(sentence);
        Logger.logging("Синтаксический анализ = " +
                syntaxWords.stream().map(Object::toString).collect(Collectors.joining(", ")));
        this.syntaxAnalyzer = syntaxAnalyzer;
    }

    public RelationAnalyzer getRelationAnalyzer() {
        return relationAnalyzer;
    }

    public void setRelationAnalyzer(RelationAnalyzer relationAnalyzer) throws IOException, NlpParseException {
        relationWords = relationAnalyzer.parse(sentence);
        Logger.logging("Отношения = " +
                relationWords.stream().map(Object::toString).collect(Collectors.joining(", ")));
        this.relationAnalyzer = relationAnalyzer;
    }

    public NlpSentence getSentence() {
        return sentence;
    }

    public void setSentence(NlpSentence sentence) {
        this.sentence = sentence;
    }

    public void process() throws IOException, NlpParseException {
        Logger.logging("Предложение = " + sentence);
        List<NamedAnnotationEntity> entities = texterra.getNamedAnnotationEntities(sentence.getText());

        morphWords = morphAnalyzer.parse(sentence);
        Logger.logging("Морфологический анализ = " +
                morphWords.stream().map(Object::toString).collect(Collectors.joining(", ")));

        syntaxWords = syntaxAnalyzer.parse(sentence);
        Logger.logging("Синтаксический анализ = " +
                syntaxWords.stream().map(Object::toString).collect(Collectors.joining(", ")));

        namedWords = NlpUtils.transformNamedAnnotationsEntity(sentence, entities, syntaxWords);
        Logger.logging("Именнованнные слова = " +
                namedWords.stream().map(Object::toString).collect(Collectors.joining(", ")));

        relationWords = relationAnalyzer.parse(sentence);
        Logger.logging("Отношения = " +
                relationWords.stream().map(Object::toString).collect(Collectors.joining(", ")));

        List<NlpTreeObject> nlpTreeObjects = Lists.transform(namedWords, elem -> new NlpTreeObject(
                NlpUtils.wordMatching(elem.getIndexes(), syntaxWords, morphWords),
                NlpUtils.getClassName(elem.getNamedTag())));

        List<NlpTreeRelation> nlpTreeRelations = Lists.transform(relationWords,
                elem -> new NlpTreeRelation(elem.getType()));

        nlpTreeDependency.addAllObjects(nlpTreeObjects);
        nlpTreeDependency.addAllRelations(nlpTreeRelations);
        rebuildNlpTree();
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

    private void rebuildNlpTree() {
        for (RelationWord relationWord : relationWords) {
            NlpTreeRelation relation = new NlpTreeRelation(relationWord.getType());
            for (NamedWord namedWord : namedWords) {
                int headNamedWordIndex = getHeadWordIndex(namedWord.getIndexes());
                SyntaxWord syntaxWord = getSyntaxWord(headNamedWordIndex);
                SyntaxWord headSyntaxWord = getSyntaxWord(syntaxWord.getHeadIndex());
                while (headSyntaxWord.getHeadIndex() != 0) {
                    syntaxWord = headSyntaxWord;
                    headSyntaxWord = getSyntaxWord(headSyntaxWord.getHeadIndex());
                }
                if (isHead(syntaxWord, relationWord)) {
                    NlpTreeObject head = new NlpTreeObject(
                            NlpUtils.wordMatching(namedWord.getIndexes(), syntaxWords, morphWords),
                            NlpUtils.getClassName(namedWord.getNamedTag()));
                    nlpTreeDependency.addDependency(head, relation);
                } else if (isDependent(syntaxWord, relationWord)) {
                    NlpTreeObject dependent = new NlpTreeObject(
                            NlpUtils.wordMatching(namedWord.getIndexes(), syntaxWords, morphWords),
                            NlpUtils.getClassName(namedWord.getNamedTag()));
                    nlpTreeDependency.addDependency(relation, dependent);
                }
            }
        }
    }

    private boolean isHead(SyntaxWord syntaxWord, RelationWord relationWord) {
        return relationWord.getIndexes().contains(syntaxWord.getHeadIndex())
                && syntaxWord.getLabel().equals("предик");
    }

    private boolean isDependent(SyntaxWord syntaxWord, RelationWord relationWord) {
        return relationWord.getIndexes().contains(syntaxWord.getHeadIndex())
                && !syntaxWord.getLabel().equals("предик");
    }

    private int getHeadWordIndex( List<Integer> indexes) {
        for (SyntaxWord head : syntaxWords) {
            if (!indexes.contains(head.getIndex())) {
                continue;
            }
            boolean isHead = true;
            for (int i = 0; isHead && i < syntaxWords.size(); i++) {
                SyntaxWord dependent = syntaxWords.get(i);
                if (head == dependent || !indexes.contains(dependent.getIndex())) {
                    continue;
                }
                isHead = (head.getHeadIndex() != dependent.getIndex());
            }
            if (isHead) return head.getIndex();
        }
        return -1;
    }
}
