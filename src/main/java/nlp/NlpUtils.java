package nlp;

import nlp.texterra.NamedAnnotationEntity;
import nlp.words.NamedWord;
import nlp.words.SyntaxWord;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class NlpUtils {

    public static List<NamedWord> transformNamedAnnotationsEntity(
            NlpSentence sentence,
            List<NamedAnnotationEntity> entities,
            List<SyntaxWord> syntaxWords) {
        List<NamedWord> namedWords = new ArrayList<>();
        for (NamedAnnotationEntity entity : entities) {
            for (NamedAnnotationEntity.NamedEntity namedEntity : entity.getAnnotation().getNamedEntities()) {
                List<Integer> indexes = getNamedWordIndexes(sentence, namedEntity.getStart(), namedEntity.getEnd());
                namedWords.add(getNamedWord(syntaxWords, indexes, namedEntity.getValue()));
            }
        }
        return namedWords;
    }

    private static List<Integer> getNamedWordIndexes(NlpSentence sentence, int start, int end) {
        List<Integer> indexes = new ArrayList<Integer>();
        String[] words = sentence.getNormalizeText().split(" ");
        int prevLength = 0;
        for (int i = 0; i < words.length; i++) {
            prevLength += words[i].length();
            if (start <= prevLength && prevLength < end) {
                indexes.add(i);
            }
            prevLength++;
        }
        return indexes;
    }

    private static NamedWord getNamedWord(List<SyntaxWord> syntaxWords, List<Integer> indexes, NamedAnnotationEntity.NamedType value) {
        List<SyntaxWord> namedSyntaxWords = syntaxWords.stream()
                .filter(syntaxWord -> indexes.contains(syntaxWord.getIndex() - 1))
                .collect(Collectors.toList());

        if (namedSyntaxWords.isEmpty()) {
            throw new IllegalArgumentException("'SyntaxWords' is invalid!");
        }
        if (namedSyntaxWords.size() == 1) {
            return new NamedWord(namedSyntaxWords.get(0), value.getTag(), value.getType());
        }
        SyntaxWord mainNamedWord = namedSyntaxWords.stream()
                .findFirst()
                .filter(parent -> namedSyntaxWords.stream().allMatch(
                        child -> parent != child && parent.getHeadIndex() != child.getIndex()))
                .get();

        StringBuilder namedText = new StringBuilder();
        StringBuilder namedInitial = new StringBuilder();
        for (int i = 0; i < namedSyntaxWords.size(); i++) {
            namedText.append(namedSyntaxWords.get(i).getText());
            namedInitial.append(namedSyntaxWords.get(i).getInitial());
            if (i == namedSyntaxWords.size() - 1) {
                namedText.append(" ");
                namedInitial.append(" ");
            }
        }
        return new NamedWord(
                namedText.toString(),
                namedInitial.toString(),
                mainNamedWord.getTag(),
                mainNamedWord.getFeats(),
                mainNamedWord.getIndex(),
                mainNamedWord.getHeadIndex(),
                mainNamedWord.getLabel(),
                value.getTag(),
                value.getType());
    }
}
