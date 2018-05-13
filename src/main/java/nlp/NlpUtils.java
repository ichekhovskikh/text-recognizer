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

    public static String getClassName(NamedTag tag) {
        String className = tag.toString();
        int firstIndex = className.indexOf('_');
        if (firstIndex != -1)
            className = className.substring(firstIndex + 1, className.length());
        return className;
    }

    public static NamedTag getNamedTag(String className) {
        NamedTag[] values = NamedTag.values();
        for (NamedTag value : values) {
            if (value.toString().toUpperCase().contains(className.toUpperCase())){
                return value;
            }
        }
        return null;
    }

    public static List<NamedWord> transformNamedAnnotationsEntity(
            NlpSentence sentence,
            List<NamedAnnotationEntity> entities,
            List<SyntaxWord> syntaxWords) {
        List<NamedWord> namedWords = new ArrayList<>();
        for (NamedAnnotationEntity entity : entities) {
            for (NamedAnnotationEntity.NamedEntity namedEntity : entity.getAnnotations().getNamedEntities()) {
                List<Integer> indexes = getWordIndexes(sentence, namedEntity.getStart(), namedEntity.getEnd());
                namedWords.add(createNamedWordByIndexes(syntaxWords, indexes, namedEntity.getValue()));
            }
        }
        return namedWords;
    }

    public static String getInitialNamedWord(NamedWord namedWord, List<MorphWord> morphWords) {
        return getInitialWord(morphWords, namedWord.getIndexes());
    }

    public static String getInitialRelationWord(RelationWord relationWord, List<MorphWord> morphWords) {
        return getInitialWord(morphWords, relationWord.getIndexes());
    }

    public static NamedWord wordMatching(NamedWord namedWord, List<SyntaxWord> syntaxWords, List<MorphWord> morphWords){
        NamedWord word = (NamedWord) namedWord.clone();
        List<Integer> indexes = namedWord.getIndexes();
        StringBuilder text = new StringBuilder();
        for (int index : indexes) {
            MorphWord morphWord = getMorphWord(morphWords, index);
            if (morphWord.getCategory() == MorphWord.Category.A_ADJECTIVE) {
                SyntaxWord syntaxWord = getSyntaxWord(syntaxWords, index);
                text.append(replaceAdjectiveEnd(morphWord.getInitial(), getMorphWord(morphWords, syntaxWord.getHeadIndex())));
            }
            else text.append(morphWord.getInitial());

            if (indexes.indexOf(index) != indexes.size() - 1) {
                text.append(" ");
            }
        }
        word.setText(text.toString());
        return word;
    }

    public static List<Integer> getWordIndexes(NlpSentence sentence, int start, int end) {
        List<Integer> indexes = new ArrayList<>();
        List<String> words = sentence.getTokens();
        int prevLength = 0;
        for (int i = 0; i < words.size(); i++) {
            prevLength += words.get(i).length();
            if (start <= prevLength && prevLength <= end) {
                indexes.add(i + 1);
            }
            prevLength++;
        }
        return indexes;
    }

    public static String getInitialWord(List<MorphWord> morphWords, List<Integer> indexes) {
        StringBuilder namedInitial = new StringBuilder();
        for (int i = 0; i < indexes.size(); i++) {
            int index = indexes.get(i);
            MorphWord morphWord = (MorphWord) morphWords
                    .stream()
                    .filter(morph -> morph.getIndex() == index)
                    .toArray()[0];
            namedInitial.append(morphWord.getInitial());
            if (i != indexes.size() - 1) {
                namedInitial.append(" ");
            }
        }
        return namedInitial.toString();
    }

    private static String replaceAdjectiveEnd(String text, MorphWord parent) {
        int last = text.length() - 2;
        char gender = parent.getFeats().charAt(2);
        String end = text.substring(last);

        if (gender == '-' || gender == 'm' || !(end.equals("ый") || end.equals("ий") || end.equals("ой")))
            return text;

        end = text.substring(last - 1);
        StringBuilder builder = new StringBuilder();
        if (gender == 'f' && end.equals("ний"))
            builder.append(text.substring(0, last)).append("яя");
        else if (gender == 'f' && !end.equals("ний"))
            builder.append(text.substring(0, last)).append("ая");
        else if (gender == 'n' && end.equals("ний"))
            builder.append(text.substring(0, last)).append("ее");
        else if (gender == 'n' && !end.equals("ний"))
            builder.append(text.substring(0, last)).append("ое");
        return builder.toString();
    }

    private static NamedWord createNamedWordByIndexes(List<SyntaxWord> syntaxWords, List<Integer> indexes, NamedAnnotationEntity.NamedType value) {
        List<SyntaxWord> namedSyntaxWords = syntaxWords.stream()
                .filter(syntaxWord -> indexes.contains(syntaxWord.getIndex()))
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
            if (i != namedSyntaxWords.size() - 1) {
                namedText.append(" ");
            }
        }
        return new NamedWord(indexes,
                namedText.toString(),
                value.getTag(),
                value.getType());
    }

    private static SyntaxWord getSyntaxWord(List<SyntaxWord> syntaxWords, int index) {
        return syntaxWords
                .stream()
                .filter(elem -> elem.getIndex() == index)
                .findFirst()
                .get();
    }

    private static MorphWord getMorphWord(List<MorphWord> morphWords, int index) {
        return morphWords
                .stream()
                .filter(elem -> elem.getIndex() == index)
                .findFirst()
                .get();
    }

    public static String getlocalizeName(NamedTag tag){
        String name;
        switch (tag) {
            case GPE_COUNTRY: {
                name = "СТРАНА";
                break;
            }
            case GPE_CITY: {
                name = "ГОРОД";
                break;
            }
            case GPE_STATE_PROVINCE: {
                name = "ГОСУДАРСТВЕННАЯ ПРОВИНЦИЯ";
                break;
            }
            case LOCATION_RIVER: {
                name = "РЕКА";
                break;
            }
            case LOCATION_LAKE_SEA_OCEAN: {
                name = "ОЗЕРО/МОРЕ/ОКЕАН";
                break;
            }
            case LOCATION_REGION: {
                name = "РЕГИОН";
                break;
            }
            case LOCATION_CONTINENT: {
                name = "КОНТИНЕНТ";
                break;
            }
            default: {
                name = "ПРОЧЕЕ";
                break;
            }
        }
        return name;
    }
}
