package nlp.analyzers;

import nlp.NlpSentence;

import java.util.List;

public interface NlpAnalyzer<T> {
    List<T> parse(NlpSentence sentence) throws NlpParseException;
}
