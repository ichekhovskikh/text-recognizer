package nlp.words;

import nlp.NamedTag;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class NamedWord implements InfoWord, Cloneable {
    private String text;
    private List<Integer> indexes;
    private String type;
    private NamedTag namedTag;

    public NamedWord() {
        this.indexes = new ArrayList<>();
    }

    public NamedWord(String text, NamedTag namedTag, String type) {
        this.text = text;
        this.indexes = new ArrayList<>();
        this.namedTag = namedTag;
        this.type = type;
    }

    public NamedWord(int index, String text, NamedTag namedTag, String type) {
        this(Collections.singletonList(index), text, namedTag, type);
    }

    public NamedWord(int[] indexes, String text, NamedTag namedTag, String type) {
        this(Arrays.stream(indexes).boxed().collect(Collectors.toList()), text, namedTag, type);
    }

    public NamedWord(List<Integer> indexes, String text, NamedTag namedTag, String type) {
        this.text = text;
        this.indexes = indexes;
        this.namedTag = namedTag;
        this.type = type;
    }

    @Override
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<Integer> getIndexes() {
        return indexes;
    }

    public void setIndexes(List<Integer> indexes) {
        this.indexes = indexes;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public NamedTag getNamedTag() {
        return namedTag;
    }

    public void setNamedTag(NamedTag namedTag) {
        this.namedTag = namedTag;
    }

    @Override
    public Object clone() {
        NamedWord word = new NamedWord();
        word.indexes = new ArrayList<>(indexes);
        word.text = new String(text);
        word.type = type;
        word.namedTag = namedTag;
        return word;
    }
}
