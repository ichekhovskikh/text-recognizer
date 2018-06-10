package nlp;

import java.util.List;

public interface NlpText {
    String getText();
    NlpSentence getSentence(int index);
    List<NlpSentence> getAllSentences();
    int getCountSentences();
}
