package nlp.analyzers;

import com.google.gson.Gson;
import nlp.NlpSentence;
import nlp.words.RelationWord;
import nlp.words.SyntaxWord;

import java.io.FileWriter;
import java.io.IOException;
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

    //todo реализовать поиск отношений
    public List<RelationWord> parse(NlpSentence sentence) {
        return null;
    }

    public void saveModel(String savePath) throws IOException {
        FileWriter writer = new FileWriter(savePath);
        writer.write(new Gson().toJson(model));
        writer.close();
    }
}
