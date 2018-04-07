package nlp.analyzers;

import com.google.common.collect.Multimap;
import com.google.gson.Gson;
import nlp.NlpSentence;
import nlp.NlpUtils;
import nlp.words.RelationWord;
import nlp.words.SyntaxWord;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RelationshipAnalyzer implements NlpAnalyzer<RelationWord> {
    private RelationshipModel model = null;

    public RelationshipAnalyzer() {
        model = new RelationshipModel();
    }

    public RelationshipAnalyzer(RelationshipModel model) throws IOException {
        this.model = model;
    }

    public RelationshipAnalyzer(String modelPath) throws IOException {
        setModel(modelPath);
    }

    public void setModel(String modelPath) throws IOException {
        this.model = new RelationshipModel(modelPath);
    }

    public List<RelationWord> parse(NlpSentence sentence) {
        List<RelationWord> words = new ArrayList<>();
        String normalizeText = sentence.getNormalizeText();
        List<String> keys = model.getKeys();
        for (String key : keys) {
            for (String value : model.getValues(key)) {
                int index = normalizeText.indexOf(value);
                while (index != -1) {
                    List<Integer> indexes = NlpUtils.getWordIndexes(sentence, index, index + value.length());
                    words.add(new RelationWord(indexes, value, key));
                    index = normalizeText.indexOf(value, index + value.length());
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
}
