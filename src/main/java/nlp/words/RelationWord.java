package nlp.words;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class RelationWord implements InfoWord {
    private String text;
    private List<Integer> indexes;
    private String type;

    public RelationWord() {
        this.indexes = new ArrayList<>();
    }

    public RelationWord(String text, String type) {
        this.text = text;
        this.indexes = new ArrayList<>();
        this.type = type;
    }

    public RelationWord(int index, String text, String type) {
        this(Collections.singletonList(index), text, type);
    }

    public RelationWord(int[] indexes, String text, String type) {
        this(Arrays.stream(indexes).boxed().collect(Collectors.toList()), text, type);
    }

    public RelationWord(List<Integer> indexes, String text, String type) {
        this.text = text;
        this.indexes = indexes;
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

    public Boolean IsEmpty() {
        return indexes.size() == 0;
    }

    @Override
    public String toString() {
        return text + " " + type;
    }
}
