package nlp;

import nlp.analyzers.*;
import nlp.tree.*;
import nlp.words.MorphWord;
import nlp.words.NamedWord;
import nlp.words.RelationWord;
import nlp.words.SyntaxWord;

import java.io.IOException;

public interface NlpController {
    NlpDependencyTree getNlpDependencyTree();
    void setNlpDependencyTree(NlpDependencyTree nlpDependencyTree);
    NlpAnalyzer<MorphWord> getMorphAnalyzer();
    void setMorphAnalyzer(NlpAnalyzer<MorphWord> morphAnalyzer) throws NlpParseException;
    NlpAnalyzer<SyntaxWord> getSyntaxAnalyzer();
    void setSyntaxAnalyzer(NlpAnalyzer<SyntaxWord> syntaxAnalyzer) throws NlpParseException;
    NlpAnalyzer<RelationWord> getRelationAnalyzer();
    void setRelationAnalyzer(NlpAnalyzer<RelationWord> relationAnalyzer) throws NlpParseException;
    NlpSentence getSentence();
    void setSentence(NlpSentence sentence);
    void process() throws IOException, NlpParseException;
    SyntaxWord getSyntaxWord(int index);
    MorphWord getMorphWord(int index);
    NamedWord getNamedWord(int index);
    RelationWord getRelationWord(int index);
}
