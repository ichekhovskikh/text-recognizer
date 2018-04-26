package nlp.analyzers;

import nlp.NlpSentence;

import java.io.IOException;
import java.util.List;

public interface NlpAnalyzer<T> {
    List<T> parse(NlpSentence sentence) throws NlpParseException, IOException;
}
