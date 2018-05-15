package nlp.analyzers;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import nlp.NlpSentence;
import nlp.words.MorphWord;
import nlp.words.RelationWord;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class RelationshipAnalyzer implements NlpAnalyzer<RelationWord> {
    private RelationshipModel model = null;
    private TreeTaggerMorphAnalyzer morphAnalyzer;

    public RelationshipAnalyzer() throws IOException, URISyntaxException {
        this("russian.rel");
    }

    public RelationshipAnalyzer(String modelPath) throws IOException, URISyntaxException {
        setModel(modelPath);
        this.morphAnalyzer = new TreeTaggerMorphAnalyzer();
    }

    public RelationshipAnalyzer(RelationshipModel model) throws IOException {
        this.model = model;
        this.morphAnalyzer = new TreeTaggerMorphAnalyzer();
    }

    public void setModel(String modelPath) throws IOException, URISyntaxException {
        this.model = new RelationshipModel(modelPath);
    }

    public List<RelationWord> parse(NlpSentence sentence) throws IOException, NlpParseException {
        return parse(new TreeTaggerMorphAnalyzer().parse(sentence));
    }

    public List<RelationWord> parse(List<MorphWord> morphWords) throws NlpParseException {
        List<RelationWord> words = new ArrayList<>();
        List<String> keys = model.getKeys();
        for (int i = 0; i < morphWords.size(); i++) {
            for (String key : keys) {
                for (String value : model.getValues(key)) {
                    List<String> valueWords = Lists.transform(
                            morphAnalyzer.parse(new NlpSentence(value)), elem -> elem.getInitial());
                    //String[] valueWords = value.split(" ");
                    List<Integer> indexes = getIndexes(valueWords, morphWords, i);
                    if (indexes != null) {
                        words.add(new RelationWord(indexes, value, key));
                        i += indexes.size();
                    }
                }
            }
        }
        return words;
    }

    public void saveModel(String savePath) throws IOException {
        FileWriter writer = new FileWriter(savePath);
        writer.write(new Gson().toJson(model));
        writer.close();
    }

    private List<Integer> getIndexes(List<String> valueWords, List<MorphWord> morphWords, int morphIndex) {
        List<Integer> indexes = new ArrayList<>();
        for (int i = 0; i < valueWords.size(); i++) {
            MorphWord morphWord = morphWords.get(i + morphIndex);
            if (!valueWords.get(i).toUpperCase().equals(morphWord.getInitial().toUpperCase()))
                return null;
            else indexes.add(morphWord.getIndex());
        }
        return indexes;
    }
}
