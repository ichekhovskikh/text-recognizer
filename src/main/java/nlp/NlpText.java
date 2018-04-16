package nlp;

import com.google.common.base.Function;
import com.google.common.collect.Lists;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NlpText {
    private String text = null;
    private List<NlpSentence> sentences = null;

    public NlpText(String text) {
        this.text = text;
        sentences = splitText(text);
    }

    public String getText() {
        return text;
    }

    public NlpSentence getSentence(int index) {
        return sentences.get(index);
    }

    public List<NlpSentence> getAllSentences() {
        return sentences;
    }

    public int getCountSentences() {
        return sentences.size();
    }

    private List<NlpSentence> splitText(String text) {
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
        if (sentences.size() == 1 && sentences.get(0).equals(""))
            sentences.set(0, text);
        return Lists.transform(sentences, elem -> new NlpSentence(elem));
    }
}
