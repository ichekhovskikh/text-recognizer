package nlp.words;

import com.google.gson.Gson;

public class MorphWord implements InfoWord {
    protected String text;
    protected String initial;
    protected String tag;
    protected String feats;

    public MorphWord(String rawResponse){
        this(new Gson().fromJson(rawResponse, SerializedWord.class));
    }

    public MorphWord(SerializedWord serializeWordInfo) {
        this.text = serializeWordInfo.getText();
        SerializedWord.Analyse analyse = (serializeWordInfo.getAnalyse() == null) ?
                new SerializedWord.Analyse(text, "S,муж,неод=(вин,ед|им,ед)") : serializeWordInfo.getAnalyse();
        this.initial = analyse.getLex();
        setGr(analyse.getGr());
    }

    public MorphWord(String text, String initial, String tag, String feats) {
        this.text = text;
        this.initial = initial;
        this.tag = tag;
        this.feats = feats;
    }

    public String getText() {
        return text;
    }

    public String getInitial() {
        return initial;
    }

    public String getTag() {
        return tag;
    }

    public String getFeats() {
        return feats;
    }

    private void setGr(String gr){
        tag = gr.split(",|=")[0];
        feats = gr.replaceFirst(tag, "").substring(1);
    }
}
