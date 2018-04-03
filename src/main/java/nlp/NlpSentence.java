package nlp;

import static java.util.Arrays.stream;

public class NlpSentence {
    private String text = null;

    public NlpSentence(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public String[] getTokens() {
        return stream(text.replaceAll("[^A-Za-zА-Яа-яЁё| ]", "").split(" "))
                .filter(elem -> elem != null && !elem.equals(""))
                .toArray(String[]::new);
    }
}
