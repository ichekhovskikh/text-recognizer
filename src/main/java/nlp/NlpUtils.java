package nlp;

import nlp.texterra.NamedAnnotationEntity;
import nlp.words.MorphWord;
import nlp.words.NamedWord;
import nlp.words.RelationWord;
import nlp.words.SyntaxWord;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class NlpUtils {

    private NlpUtils() {
    }

    public static List<NamedWord> transformNamedAnnotationsEntity(
            NlpSentence sentence,
            List<NamedAnnotationEntity> entities,
            List<SyntaxWord> syntaxWords) {
        List<NamedWord> namedWords = new ArrayList<>();
        for (NamedAnnotationEntity entity : entities) {
            for (NamedAnnotationEntity.NamedEntity namedEntity : entity.getAnnotation().getNamedEntities()) {
                List<Integer> indexes = getWordIndexes(sentence, namedEntity.getStart(), namedEntity.getEnd());
                namedWords.add(getNamedWord(syntaxWords, indexes, namedEntity.getValue()));
            }
        }
        return namedWords;
    }

    public static int getParentNamedWord(List<SyntaxWord> syntaxWords, NamedWord namedWord) {
        return getParentWord(syntaxWords, namedWord.getIndexes());
    }

    public static int getParentRelationWord(List<SyntaxWord> syntaxWords, RelationWord relationWord) {
        return getParentWord(syntaxWords, relationWord.getIndexes());
    }

    public static String getInitialNamedWord(List<MorphWord> morphWords, NamedWord namedWord) {
        return getInitialWord(morphWords, namedWord.getIndexes());
    }

    public static String getInitialRelationWord(List<MorphWord> morphWords, RelationWord relationWord) {
        return getInitialWord(morphWords, relationWord.getIndexes());
    }

    //todo сделать поиск инициаторов отошения
    public static List<NamedWord> getParentRelationship(List<NamedWord> namedWord, RelationWord relationWord) {
        return null;
    }

    //todo сделать поиск зависимых слов в отошение
    public static List<NamedWord> getChildRelationship(List<NamedWord> namedWord, RelationWord relationWord) {
        return null;
    }

    public static List<Integer> getWordIndexes(NlpSentence sentence, int start, int end) {
        List<Integer> indexes = new ArrayList<>();
        List<String> words = sentence.getTokens();
        int prevLength = 0;
        for (int i = 0; i < words.size(); i++) {
            prevLength += words.get(i).length();
            if (start <= prevLength && prevLength < end) {
                indexes.add(i);
            }
            prevLength++;
        }
        return indexes;
    }

    private static String getInitialWord(List<MorphWord> morphWords, List<Integer> indexes) {
        StringBuilder namedInitial = new StringBuilder();
        for (int i = 0; i < indexes.size(); i++) {
            int index = indexes.get(i);
            MorphWord morphWord = morphWords
                    .stream()
                    .findFirst()
                    .filter(morph -> morph.geIndex() == index)
                    .get();
            namedInitial.append(morphWord.getInitial());
            if (i == indexes.size() - 1) {
                namedInitial.append(" ");
            }
        }
        return namedInitial.toString();
    }

    private static int getParentWord(List<SyntaxWord> syntaxWords, List<Integer> indexes) {
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

    private static NamedWord getNamedWord(List<SyntaxWord> syntaxWords, List<Integer> indexes, NamedAnnotationEntity.NamedType value) {
        List<SyntaxWord> namedSyntaxWords = syntaxWords.stream()
                .filter(syntaxWord -> indexes.contains(syntaxWord.getIndex() - 1))
                .collect(Collectors.toList());

        if (namedSyntaxWords.isEmpty()) {
            throw new IllegalArgumentException("'SyntaxWords' is invalid!");
        }
        if (namedSyntaxWords.size() == 1) {
            SyntaxWord syntaxWord = namedSyntaxWords.get(0);
            return new NamedWord(indexes, syntaxWord.getText(), value.getTag(), value.getType());
        }

        StringBuilder namedText = new StringBuilder();
        for (int i = 0; i < namedSyntaxWords.size(); i++) {
            namedText.append(namedSyntaxWords.get(i).getText());
            if (i == namedSyntaxWords.size() - 1) {
                namedText.append(" ");
            }
        }
        return new NamedWord(indexes,
                namedText.toString(),
                value.getTag(),
                value.getType());
    }
}
