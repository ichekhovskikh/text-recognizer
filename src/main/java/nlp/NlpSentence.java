package nlp;

import java.util.List;

public interface NlpSentence {
    String getText();
    List<String> getTokens();
    String getNormalizeText();
}
