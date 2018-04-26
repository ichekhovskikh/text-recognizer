package nlp.analyzers;

import com.google.gson.Gson;
import nlp.NlpSentence;
import nlp.NlpUtils;
import nlp.words.MorphWord;
import nlp.words.RelationWord;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class RelationshipAnalyzer implements NlpAnalyzer<RelationWord> {
    private RelationshipModel model = null;

    public RelationshipAnalyzer() throws IOException, URISyntaxException {
        setModel("russian.rel");
    }

    public RelationshipAnalyzer(RelationshipModel model) throws IOException {
        this.model = model;
    }

    public RelationshipAnalyzer(String modelPath) throws IOException, URISyntaxException {
        setModel(modelPath);
    }

    public void setModel(String modelPath) throws IOException, URISyntaxException {
        this.model = new RelationshipModel(modelPath);
    }

    public List<RelationWord> parse(NlpSentence sentence) throws IOException, NlpParseException {
        return parse(new TreeTaggerMorphAnalyzer().parse(sentence));
    }

    public List<RelationWord> parse(List<MorphWord> morphWords) {
        List<RelationWord> words = new ArrayList<>();
        List<String> keys = model.getKeys();
        for (int i = 0; i < morphWords.size(); i++) {
            for (String key : keys) {
                for (String value : model.getValues(key)) {
                    String[] valueWords = value.split(" ");
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

    private List<Integer> getIndexes(String[] valueWords, List<MorphWord> morphWords, int morphIndex) {
        List<Integer> indexes = new ArrayList<>();
        for (int i = 0; i < valueWords.length; i++) {
            MorphWord morphWord = morphWords.get(i + morphIndex);
            if (!valueWords[i].equals(morphWord.getInitial()))
                return null;
            else indexes.add(morphWord.getIndex());
        }
        return indexes;
    }
}
