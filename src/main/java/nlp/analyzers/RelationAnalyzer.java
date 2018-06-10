package nlp.analyzers;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import nlp.DefaultNlpSentence;
import nlp.NlpSentence;
import nlp.words.MorphWord;
import nlp.words.RelationWord;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class RelationAnalyzer implements NlpAnalyzer<RelationWord> {
    private RelationModel model = null;
    private NlpAnalyzer<MorphWord> morphAnalyzer;

    @Inject
    public RelationAnalyzer(@Named("RelationModelPath") String modelPath,
                            @Named("MorphAnalyzer") NlpAnalyzer morphAnalyzer) throws IOException, URISyntaxException, NlpParseException {
        this.morphAnalyzer = morphAnalyzer;
        setModel(modelPath);
    }

    public void setModel(String modelPath) throws IOException, URISyntaxException, NlpParseException {
        this.model = new DefaultRelationModel(modelPath);
        List<String> keys = model.getKeys();
        for (String key : keys) {
            List<String> values = new ArrayList<>();
            for (String value : model.getValues(key)) {
                values.add(String.join(" ", Lists.transform(
                        morphAnalyzer.parse(new DefaultNlpSentence(value)), elem -> elem.getInitial())));
            }
            model.putRelations(key, values);
        }
    }

    public List<RelationWord> parse(NlpSentence sentence) throws NlpParseException {
        return parse(morphAnalyzer.parse(sentence));
    }

    public List<RelationWord> parse(List<MorphWord> morphWords) throws NlpParseException {
        List<RelationWord> words = new ArrayList<>();
        List<String> keys = model.getKeys();
        for (int i = 0; i < morphWords.size(); i++) {
            int index = i;
            RelationWord equalsWord = new RelationWord();
            keys.forEach(key -> {
                model.getValues(key).forEach(value -> {
                    String[] valueWords = value.split(" ");
                    if (valueWords.length > equalsWord.getIndexes().size()) {
                        List<Integer> indexes = getIndexes(valueWords, morphWords, index);
                        if (indexes != null) {
                            equalsWord.setIndexes(indexes);
                            equalsWord.setText(value);
                            equalsWord.setType(key);
                        }
                    }
                });
            });
            if (!equalsWord.IsEmpty()) {
                words.add(equalsWord);
                i += equalsWord.getIndexes().size() - 1;
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
            if (!valueWords[i].toUpperCase().equals(morphWord.getInitial().toUpperCase()))
                return null;
            else indexes.add(morphWord.getIndex());
        }
        return indexes;
    }
}
