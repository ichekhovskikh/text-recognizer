package nlp;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NlpText {
    private String text = null;
    private List<String> sentences = null;

    public NlpText(String text) {
        this.text = text;
        sentences = splitText(text);
    }

    public String getText() {
        return text;
    }

    public String getSentence(int index) {
        return sentences.get(index);
    }

    public List<String> getAllSentences() {
        return sentences;
    }

    public int getCountSentences() {
        return sentences.size();
    }

    private List<String> splitText(String text) {
        final int MIN_LENGTH = 3;
        Pattern pattern = Pattern.compile(
                "(^|(?<=[.!?]\\s))(\\d+\\.\\s?)*[А-ЯA-Z][^!?]*?[.!?](?=\\s*(\\d+\\.\\s)*[А-ЯA-Z]|$)",
                Pattern.MULTILINE);
        Matcher matcher = pattern.matcher(text);
        List<String> sentences = new ArrayList<>();
        sentences.add("");
        while (matcher.find()) {
            String sentence = matcher.group();
            String lastSentence = sentences.get(sentences.size() - 1);
            String[] tokens = lastSentence.split(" ");
            if (tokens[tokens.length - 1].length() < MIN_LENGTH)
                sentences.set(sentences.size() - 1, lastSentence + " " + sentence);
            else sentences.add(sentence);
        }
        return sentences;
    }
}
