package nlp;

import com.google.common.collect.Lists;

import java.util.List;

public class NlpSentence {
    private String text = null;
    private List<String> tokens = null;

    public NlpSentence(String text) {
        this.text = text;
        tokens = getTokens(text);
    }

    public String getText() {
        return text;
    }

    public List<String> getTokens() {
        return tokens;
    }

    public String getNormalizeText() {
        return text.replaceAll("[^A-Za-zА-Яа-яЁё| ]", "");
    }

    public List<String> getTokens(String text) {
        List<String> tokens = Lists.newArrayList(text.replaceAll("[^A-Za-zА-Яа-яЁё| ]", "").split(" "));
        tokens.removeIf(elem -> elem != null && !elem.equals(""));
        return tokens;
    }
}
