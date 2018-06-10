package nlp;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import logging.Logger;
import nlp.analyzers.*;
import nlp.texterra.NamedAnnotationEntity;
import nlp.texterra.Texterra;
import nlp.tree.*;
import nlp.words.*;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class BasicNlpController implements NlpController {
    private NlpSentence sentence;

    private Texterra texterra;
    private NlpDependencyTree nlpDependencyTree;

    private NlpAnalyzer<MorphWord> morphAnalyzer;
    private NlpAnalyzer<SyntaxWord> syntaxAnalyzer;
    private NlpAnalyzer<RelationWord> relationAnalyzer;

    private List<MorphWord> morphWords;
    private List<SyntaxWord> syntaxWords;
    private List<NamedWord> namedWords;
    private List<RelationWord> relationWords;

    @Inject
    public BasicNlpController(Texterra texterra,
                              @Named("MorphAnalyzer") NlpAnalyzer morphAnalyzer,
                              @Named("SyntaxAnalyzer") NlpAnalyzer syntaxAnalyzer,
                              @Named("RelationAnalyzer") NlpAnalyzer relationAnalyzer,
                              NlpDependencyTree nlpDependencyTree) throws IOException, NlpParseException {
        this.texterra = texterra;
        this.morphAnalyzer = morphAnalyzer;
        this.syntaxAnalyzer = syntaxAnalyzer;
        this.relationAnalyzer = relationAnalyzer;
        this.nlpDependencyTree = nlpDependencyTree;
        this.sentence = new DefaultNlpSentence("");
    }

    public NlpDependencyTree getNlpDependencyTree() {
        return nlpDependencyTree;
    }

    public void setNlpDependencyTree(NlpDependencyTree nlpDependencyTree) {
        this.nlpDependencyTree = nlpDependencyTree;
    }

    public NlpAnalyzer<MorphWord> getMorphAnalyzer() {
        return morphAnalyzer;
    }

    public void setMorphAnalyzer(NlpAnalyzer<MorphWord> morphAnalyzer) throws NlpParseException {
        morphWords = morphAnalyzer.parse(sentence);
        Logger.logging("Морфологический анализ = " +
                morphWords.stream().map(Object::toString).collect(Collectors.joining(", ")));
        this.morphAnalyzer = morphAnalyzer;
    }

    public NlpAnalyzer<SyntaxWord> getSyntaxAnalyzer() {
        return syntaxAnalyzer;
    }

    public void setSyntaxAnalyzer(NlpAnalyzer<SyntaxWord> syntaxAnalyzer) throws NlpParseException {
        syntaxWords = syntaxAnalyzer.parse(sentence);
        Logger.logging("Синтаксический анализ = " +
                syntaxWords.stream().map(Object::toString).collect(Collectors.joining(", ")));
        this.syntaxAnalyzer = syntaxAnalyzer;
    }

    public NlpAnalyzer<RelationWord> getRelationAnalyzer() {
        return relationAnalyzer;
    }

    public void setRelationAnalyzer(NlpAnalyzer<RelationWord> relationAnalyzer) throws NlpParseException {
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
        namedWords = NlpUtils.getOnlyGeoObjects(namedWords);

        relationWords = relationAnalyzer.parse(sentence);
        Logger.logging("Отношения = " +
                relationWords.stream().map(Object::toString).collect(Collectors.joining(", ")));

        List<NlpTreeObject> nlpTreeObjects = Lists.transform(namedWords, elem -> new NlpTreeObjectImpl(
                NlpUtils.wordMatching(elem.getIndexes(), syntaxWords, morphWords),
                NlpUtils.getClassName(elem.getNamedTag())));

        List<NlpTreeRelation> nlpTreeRelations = Lists.transform(relationWords,
                elem -> new NlpTreeRelationImpl(elem.getType()));

        nlpDependencyTree.addAllObjects(nlpTreeObjects);
        nlpDependencyTree.addAllRelations(nlpTreeRelations);
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
        relationWords.forEach(relationWord -> {
            NlpTreeRelation relation = new NlpTreeRelationImpl(relationWord.getType());
            namedWords.forEach(namedWord -> {
                int headNamedWordIndex = getHeadWordIndex(namedWord.getIndexes());
                SyntaxWord syntaxWord = getSyntaxWord(headNamedWordIndex);
                SyntaxWord headSyntaxWord = getSyntaxWord(syntaxWord.getHeadIndex());
                while (headSyntaxWord.getHeadIndex() != 0) {
                    syntaxWord = headSyntaxWord;
                    headSyntaxWord = getSyntaxWord(headSyntaxWord.getHeadIndex());
                }
                if (isHead(syntaxWord, relationWord)) {
                    NlpTreeObject head = new NlpTreeObjectImpl(
                            NlpUtils.wordMatching(namedWord.getIndexes(), syntaxWords, morphWords),
                            NlpUtils.getClassName(namedWord.getNamedTag()));
                    nlpDependencyTree.addDependency(head, relation);
                } else if (isDependent(syntaxWord, relationWord)) {
                    NlpTreeObject dependent = new NlpTreeObjectImpl(
                            NlpUtils.wordMatching(namedWord.getIndexes(), syntaxWords, morphWords),
                            NlpUtils.getClassName(namedWord.getNamedTag()));
                    nlpDependencyTree.addDependency(relation, dependent);
                }
            });
        });
    }

    private boolean isHead(SyntaxWord syntaxWord, RelationWord relationWord) {
        return relationWord.getIndexes().contains(syntaxWord.getHeadIndex())
                && syntaxWord.getLabel().equals("предик");
    }

    private boolean isDependent(SyntaxWord syntaxWord, RelationWord relationWord) {
        return relationWord.getIndexes().contains(syntaxWord.getHeadIndex())
                && !syntaxWord.getLabel().equals("предик");
    }

    private int getHeadWordIndex(List<Integer> indexes) {
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
