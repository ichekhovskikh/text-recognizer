package nlp;

import com.google.common.collect.Lists;

import java.util.List;

public class DefaultNlpSentence implements NlpSentence {
    private String text = null;
    private List<String> tokens = null;

    public DefaultNlpSentence(String text) {
        this.text = text;
        tokens = tokenize();
    }

    public String getText() {
        return text;
    }

    public List<String> getTokens() {
        return tokens;
    }

    public String getNormalizeText() {
        String normalizeText = text.replaceAll("[^A-Za-zА-Яа-яЁё| ]", "");
        while (normalizeText.contains("  "))
            normalizeText = normalizeText.replaceAll("  ", " ");
        return normalizeText;
    }

    private List<String> tokenize() {
        List<String> tokens = Lists.newArrayList(getNormalizeText().split(" "));
        tokens.removeIf(elem -> elem == null || elem.equals(""));
        return tokens;
    }

    @Override
    public String toString() {
        return text;
    }
}
